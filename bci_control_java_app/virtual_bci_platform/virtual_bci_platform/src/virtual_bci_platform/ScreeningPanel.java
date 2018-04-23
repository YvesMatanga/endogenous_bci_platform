/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_bci_platform;

import com.user.bci.ImageryEnum;
import com.user.bci.ScreenPanelStateEnum;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.realtime.RealtimeThread;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


/**
 *
 * @author Yves Matanga
 */
public class ScreeningPanel extends javax.swing.JPanel {
    int FrameHeight;
    int FrameWidth;
    
    //UDP Control
            /*This Udp Socket will only respond to the simulink and settings 
             * frame to notify on end of scene*/
    protected final int UDP_LOCAL_PORT = 8890;//local end point 
    protected final int UDP_SIMULINK_PORT_FOR_SCENEFRAME = 8889;//end point in simulink
    protected final int UDP_CONTROLFRAME_PORT_FOR_SCENEFRAME = 8887;//end point in setting frame frame
    protected final int SIMULINK_TRANSMISSION_BUFFER_SIZE = 1;//small info start /stop     only 1 byte 
    protected final int CONTROLFRAME_TRANSMISSION_BUFFER_SIZE = 1;//small info start /stop     only 1 byte 
    
    //protected Thread UdpReceptionThread;
    protected DatagramSocket PanelDataSocket;
    protected Socket PanelTcpSocket;//for tcp connection
    OutputStream TcpOutputStream;
    protected DatagramPacket SimulinkTransmissionDatagramPacket;
    protected DatagramPacket ControlFrameTransmissionDatagramPacket;
    protected byte[] udpSimulinkTransmissionBuffer;
    protected byte[] udpControlFrameTransmissionBuffer;
    
    //Random Numbers
    protected Random randomGen;
    int upperMillisecond = 2500;
    int lowerMillisecond = 500;
    //state
    protected ScreenPanelStateEnum PanelState = ScreenPanelStateEnum.IDLE_STATE;
    protected RealtimeThread MainScreenScenarioThread;
    protected ImageryEnum ImageryType = ImageryEnum.RIGHT_HAND;
    
    /**
     * Creatthreades new form ScreeningPanel
     */
    public ScreeningPanel() {
        initComponents();        
        userInitComponents();
    }
       
    public void userInitComponents(){
        
        //UDP config
        try {
            //PanelDataSocket = new DatagramSocket();
            InetAddress receiverAddress = InetAddress.getLocalHost();//local host
            
            udpSimulinkTransmissionBuffer = new byte[SIMULINK_TRANSMISSION_BUFFER_SIZE];
            udpControlFrameTransmissionBuffer = new byte[CONTROLFRAME_TRANSMISSION_BUFFER_SIZE];
            
            SimulinkTransmissionDatagramPacket = new DatagramPacket(udpSimulinkTransmissionBuffer,udpSimulinkTransmissionBuffer.length,
                    receiverAddress,UDP_SIMULINK_PORT_FOR_SCENEFRAME);
            ControlFrameTransmissionDatagramPacket = new DatagramPacket(udpControlFrameTransmissionBuffer,
                    udpControlFrameTransmissionBuffer.length,receiverAddress,UDP_CONTROLFRAME_PORT_FOR_SCENEFRAME);
        } catch (Exception ex) {
            Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Random Number
         randomGen = new Random();
        //Scenario Thread
         MainScreenScenarioThread = new ScreenScenarioThread();
         MainScreenScenarioThread.setPriority(RealtimeThread.MAX_PRIORITY);//max priority due to timing that must be preserved.
    }
  
    @Override
    public void paint(Graphics g){//scene 2d graphics
        Graphics2D G2d = (Graphics2D) g;               
        //get parent frame width
        FrameHeight = ((JFrame)SwingUtilities.getWindowAncestor(this)).getContentPane().getHeight();
        FrameWidth = ((JFrame)SwingUtilities.getWindowAncestor(this)).getContentPane().getWidth();
                
        switch(PanelState){
            case IDLE_STATE :
                screenIdleState(G2d,FrameWidth,FrameHeight);                                
                break;
            case CUE_STATE:
                screenCueState(G2d,FrameWidth,FrameHeight,ImageryType);//use globa imagery type
                break;
            case BEEP_STATE:
                screenIdleState(G2d,FrameWidth,FrameHeight); 
                break;
            case IMAGERY_STATE:
                screenIdleState(G2d,FrameWidth,FrameHeight);  
                break;
            case BLANK_STATE:
                screenBlankState(G2d,FrameWidth,FrameHeight); 
                break;
        }                      
       // System.out.println("STATE : "+ PanelState.name());
    }
    
    //draw center green cross 
    private void centerCross(Graphics2D g2d,int frameWidth,int frameHeight){        
        g2d.drawLine(frameWidth/4,frameHeight/2,frameWidth*3/4,frameHeight/2);
        g2d.drawLine(frameWidth/2,frameHeight/4,frameWidth/2,frameHeight*3/4);
    }    
    
    private void screenIdleState(Graphics2D g2d,int frameWidth,int frameHeight){
        //set background color to black
        g2d.setColor(Color.black);
        //fill screen
        g2d.fillRect(0,0,FrameWidth,FrameHeight);
        g2d.setColor(Color.GREEN);
        //put green cross
        centerCross(g2d,frameWidth,frameHeight);       
    }
    
    private void screenCueState(Graphics2D g2d,int frameWidth,int frameHeight,ImageryEnum BciUserImagery){
         //set background color to black
        g2d.setColor(Color.black);
        //fill screen
        g2d.fillRect(0,0,FrameWidth,FrameHeight);
        g2d.setColor(Color.GREEN);
        //put green cross
        centerCross(g2d,frameWidth,frameHeight);   
        //put red color
        g2d.setColor(Color.RED);
        
        int rectX = 0 ,rectY = 0,rectW = 0,rectH = 0;
        int triX1 = 0,triY1 = 0,triX2 = 0,triY2 = 0,triX3 = 0,triY3 = 0;
        int imgX = 0,imgY = 0;
        String imgPath="";
        
        switch(BciUserImagery){
            
            case LEFT_HAND :
                rectX = frameWidth*19/50;
                rectY = frameHeight/2 - frameHeight/40;
                triX1 = rectX;
                triY1 = frameHeight/2 - frameHeight/20;
                triX2 = rectX;
                triY2 = frameHeight/2 + frameHeight/20;
                triX3 = 35*frameWidth/100;
                triY3 = frameHeight/2;
                rectW = 12*frameWidth/100;
                rectH = frameHeight/20;                
                //upper left corner
                imgX = frameHeight/20;
                imgY = frameHeight/20;
                imgPath = "D:\\Software Programming\\java\\projects\\virtual_bci_platform\\src\\img\\left_hand.jpg";
                
                break;
            case RIGHT_HAND:            
                rectX = frameWidth/2;
                rectY = frameHeight/2 - frameHeight/40;
                triX1 = 62*frameWidth/100;
                triY1 = frameHeight/2 - frameHeight/20;
                triX2 = triX1;
                triY2 = frameHeight/2 + frameHeight/20;
                triX3 = 65*frameWidth/100;
                triY3 = frameHeight/2;
                rectW = 12*frameWidth/100;
                rectH = frameHeight/20;                
                //upper right corner
                imgX = frameWidth -15*frameHeight/100;
                imgY = frameHeight/20;
                imgPath = "D:\\Software Programming\\java\\projects\\virtual_bci_platform\\src\\img\\right_hand.jpg";                
                break;
                
            case RIGHT_FOOT:
                rectX = frameWidth/2 - frameHeight/40;
                rectY = frameHeight/2;
                triX1 = frameWidth/2 - frameHeight/20;
                triY1 = frameHeight/2 + 3*frameWidth/25;
                triX2 = frameWidth/2 + frameHeight/20;
                triY2 = triY1;
                triX3 = frameWidth/2;
                triY3 = frameHeight/2 + 15*frameWidth/100;
                rectW = frameHeight/20;
                rectH = 12*frameWidth/100;                
                //lower right  corner
                imgX = frameWidth -15*frameHeight/100;
                imgY = 85*frameHeight/100;
                imgPath = "D:\\Software Programming\\java\\projects\\virtual_bci_platform\\src\\img\\right_foot.jpg";                
                break;
                
            case LEFT_FOOT :
                rectX = frameWidth/2 - frameHeight/40;
                rectY = frameHeight/2 - 12*frameWidth/100;
                triX1 = frameWidth/2 - frameHeight/20;
                triY1 = frameHeight/2 - 3*frameWidth/25;
                triX2 = frameWidth/2 + frameHeight/20;
                triY2 = triY1;
                triX3 = frameWidth/2;
                triY3 = frameHeight/2 - 15*frameWidth/100;
                rectW = frameHeight/20;
                rectH = 12*frameWidth/100;                
                //lower right  corner
                imgX = frameHeight/20;
                imgY = 85*frameHeight/100;
                imgPath = "D:\\Software Programming\\java\\projects\\virtual_bci_platform\\src\\img\\left_foot.jpg";                
                break;
 
             case REST :            
                rectX = frameWidth/2-12*frameWidth/200;;
                rectY = frameHeight/2 - frameHeight/40;
                //triX1 = 62*frameWidth/100;
                //triY1 = frameHeight/2 - frameHeight/20;
                //triX2 = triX1;
                //triY2 = frameHeight/2 + frameHeight/20;
                //triX3 = 65*frameWidth/100;
                //triY3 = frameHeight/2;
                rectW = 12*frameWidth/100;
                rectH = frameHeight/20;                
                //upper right corner
                imgX = frameWidth/2 - frameHeight/20;
                imgY = frameHeight/20;
                imgPath = "D:\\Software Programming\\java\\projects\\virtual_bci_platform\\src\\img\\stop.jpg";                
                break;
                                
            default:
                break;
        }
        //draw tail of arrow
        g2d.fillRect(rectX,rectY,rectW,rectH);
        //draw head of arrow
        int[] xPoints = {triX1,triX2,triX3};
        int[] yPoints = {triY1,triY2,triY3};        
        g2d.fillPolygon(xPoints, yPoints,3);
        //draw image
        try{
            BufferedImage img = ImageIO.read(new File(imgPath));
            g2d.drawImage(img.getScaledInstance(frameHeight/10,                                
                                frameHeight/10,Image.SCALE_SMOOTH),imgX,imgY,this);
        }catch(Exception Ex){
            Ex.printStackTrace();
        }
    }
    private void screenBlankState(Graphics2D g2d,int frameWidth,int frameHeight){
        //set background color to black
        g2d.setColor(Color.black);
        //fill screen
        g2d.fillRect(0,0,FrameWidth,FrameHeight);
    }
    private void initiateBeep(){
        String path = "D:\\Software Programming\\java\\projects\\virtual_bci_platform\\src\\audio\\tone.wav";
        
        try{
            InputStream in = new FileInputStream(path);
            AudioStream audioStream = new AudioStream(in);                 
            AudioPlayer.player.start(audioStream);//play beeep
        }catch(Exception Ex){
            Ex.printStackTrace();
        }      
    }
    
     private void startBeep(){
        String path = "D:\\Software Programming\\java\\projects\\virtual_bci_platform\\src\\audio\\start.wav";
        
        try{
            InputStream in = new FileInputStream(path);
            AudioStream audioStream = new AudioStream(in);                 
            AudioPlayer.player.start(audioStream);//play beeep
        }catch(Exception Ex){
            Ex.printStackTrace();
        }      
    }
     
    class ScreenScenarioThread extends RealtimeThread{
        @Override
        public void run() {
            try {
                //this function will start the trial scenario and return a 1 once done
            //init
                int randomDelay = lowerMillisecond + randomGen.nextInt(upperMillisecond - lowerMillisecond + 1);//between 500ms and 2500ms
                //formula  = rand()%(b-a + 1) + a;
                //init : green cross
                         
                 
                startBeep();
                PanelState = ScreenPanelStateEnum.IDLE_STATE;  
                PanelDataSocket = new DatagramSocket(UDP_LOCAL_PORT); 
                  //send udp packet to simulink for start imagery          
                   try {               
                udpSimulinkTransmissionBuffer = "1".getBytes();
                SimulinkTransmissionDatagramPacket.setData(udpSimulinkTransmissionBuffer);
                PanelDataSocket.send(SimulinkTransmissionDatagramPacket);
                } catch (IOException ex) {
                    Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
                } 
                repaint();
                RealtimeThread.sleep(2000);
                //beep
                PanelState = ScreenPanelStateEnum.BEEP_STATE;
                initiateBeep();             
                
                RealtimeThread.sleep(1000);
                //present cue
                PanelState = ScreenPanelStateEnum.CUE_STATE;                                   
                repaint();
                
                RealtimeThread.sleep(1000);                
                //remove cue put back green cross
                PanelState = ScreenPanelStateEnum.IMAGERY_STATE;            
                repaint();
                
                RealtimeThread.sleep(4000);
                          //send udp packet to simulink for stop imagery
                try {
                udpSimulinkTransmissionBuffer = "2".getBytes();
                SimulinkTransmissionDatagramPacket.setData(udpSimulinkTransmissionBuffer);                    
                PanelDataSocket.send(SimulinkTransmissionDatagramPacket);                    
                } catch (IOException ex) {
                    Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
                } 
                //put blank screen
                              
                PanelState = ScreenPanelStateEnum.BLANK_STATE;         
                repaint();
                
                RealtimeThread.sleep(randomDelay);
                System.out.println("RANDOM : " + randomDelay);
                
                //done
                PanelState = ScreenPanelStateEnum.IDLE_STATE;
                repaint();               
                    //send udp packet to settings frame for end of scene  
                try {
                PanelTcpSocket = new Socket(InetAddress.getLocalHost(),UDP_CONTROLFRAME_PORT_FOR_SCENEFRAME);
                TcpOutputStream = PanelTcpSocket.getOutputStream();                
                udpControlFrameTransmissionBuffer = "2".getBytes();                   
                TcpOutputStream.write(udpControlFrameTransmissionBuffer);
                TcpOutputStream.flush();
                //System.out.println("STOP PANEL!");
                } catch (IOException ex) {
                    Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                PanelDataSocket.close();
                //PanelDataSocket2.close();
                PanelTcpSocket.close();
                TcpOutputStream.close();
                //System.out.println("closed!");  
            } catch (InterruptedException ex) {
                Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
            }catch(Exception ex){
                Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    public void startScenario(DatagramSocket ComSocket,ImageryEnum ImageryTypeUsed){
        PanelDataSocket = ComSocket;        
        ImageryType = ImageryTypeUsed;//change system imagined body part
        
        if(MainScreenScenarioThread.isAlive()){//interrupt if still running
            MainScreenScenarioThread.interrupt();
        }        
        
        MainScreenScenarioThread =  new ScreenScenarioThread();
        MainScreenScenarioThread.start();       
    }
    
    /*public void stopScenario(){
        if(MainScreenScenarioThread.isAlive()){//interrupt if still running
            MainScreenScenarioThread.interrupt();
        }
        //done
                PanelState = ScreenPanelStateEnum.IDLE_STATE;
                repaint();
    }*/
    public static void main(String[] args){
        JFrame Frame = new JFrame("ScreeningPanel");
        Frame.setContentPane(new ScreeningPanel());
        Frame.setSize(300,300);
        Frame.setVisible(true);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setLocationRelativeTo(null);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
