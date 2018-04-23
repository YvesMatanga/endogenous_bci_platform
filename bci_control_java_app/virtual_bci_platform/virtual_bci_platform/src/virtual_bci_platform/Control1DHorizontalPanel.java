/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_bci_platform;

import com.user.bci.BciUser;
import com.user.bci.Control2DPanelStateEnum;
import static com.user.bci.Control2DPanelStateEnum.CURSOR_STATE;
import static com.user.bci.Control2DPanelStateEnum.IDLE_STATE;
import static com.user.bci.Control2DPanelStateEnum.SCREEN_BLANK_STATE;
import static com.user.bci.Control2DPanelStateEnum.TARGET_APPEAR_STATE;
import static com.user.bci.Control2DPanelStateEnum.TARGET_NOT_REACHED_STATE;
import static com.user.bci.Control2DPanelStateEnum.TARGET_REACHED_STATE;
import com.user.sql.SqlConnection;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.realtime.RealtimeThread;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Yves Matanga
 */
public class Control1DHorizontalPanel extends javax.swing.JPanel {
   
   //database connection details
    String RDBMS = "mysql";
    String DB_NAME = "mtech_research";
    String SQL_USER_NAME = "root";
    String SQL_USER_PASSWORD = "root";
    //Bci
    BciUser DefaultBciUser;
            
    int FrameHeight;
    int FrameWidth;    
    
    protected final int UDP_LOCAL_PORT = 8890;//local end point 
    //UDP Control
            /*This Udp Socket will only respond to the simulink and settings 
             * frame to notify on end of scene*/
    //protected final int UDP_LOCAL_PORT = 8890;//local end point 
    protected final int UDP_SIMULINK_PORT_FOR_SCENEFRAME = 8889;//end point in simulink
    protected final int UDP_CONTROLFRAME_PORT_FOR_SCENEFRAME = 8887;//end point in setting frame frame
    protected final int SIMULINK_TRANSMISSION_BUFFER_SIZE = 1;//small info start /stop     only 1 byte 
    protected final int CONTROLFRAME_TRANSMISSION_BUFFER_SIZE = 1;//small info start /stop     only 1 byte     
    
    protected final int SIMULINK_RECEPTION_BUFFER_SIZE = 1500000;//2s trial coordinates (Ts = 50ms) => 300 samples (4bytes/per samples :x ,y  int) : 600byte
    //protected Thread UdpReceptionThread;
    protected DatagramSocket PanelDataSocket;    
    protected DatagramSocket PanelDataSocket2;  //for timeout
    protected Socket PanelTcpSocket;//for tcp connection
    OutputStream TcpOutputStream;
    
    protected DatagramPacket SimulinkTransmissionDatagramPacket;
    protected DatagramPacket ControlFrameTransmissionDatagramPacket;
    protected DatagramPacket SimulinkReceptionDatagramPacket;
    
    protected byte[] udpSimulinkTransmissionBuffer;
    protected byte[] udpControlFrameTransmissionBuffer;
    protected byte[] udpSimulinkReceptionBuffer;
    
    protected Thread UdpSimulinkReceptionThread;
    private int count=0;
    
    //Random Numbers
    protected Random randomGen;
    int upperMillisecond = 2500;
    int lowerMillisecond = 500;
    //state
    protected Control2DPanelStateEnum PanelState = Control2DPanelStateEnum.IDLE_STATE;
    protected RealtimeThread MainScreenScenarioThread;
    
    private volatile boolean timeoutOccured = false;
    private volatile boolean targetReached = false;
    private volatile boolean boundReached = false;    
            //Cursor Position
    protected  int DELTA_X=0,DELTA_Y=0,CURSOR_X=0,CURSOR_Y=0;
    protected  double targetWorkSpacePercentage = 25;    
    protected  double cursorWorkSpacePercentage = 1.3;
    protected Timer TimeoutTimer;
    protected RealtimeThread MainTimeoutThread;
    
            //Settings
    protected int mode = -1;//easy mode Mode (-1) = Calibration KBCI Mode(0) : Easy Mode Mode(1) : Normal Horizon : Self-Awareness (2)
    protected int SCREENING_TIME_OUT = 60000;//1 minute
    protected  int CURSOR_TIMEOUT_TIME = (mode != -1)?15000:SCREENING_TIME_OUT;//for 15s
    protected int SCORE = 0;
    /**  
     * Creates new form Control1DHorizontalPanel
     */
    public Control1DHorizontalPanel() {
        initComponents();
        userInitComponents();
    }

    protected void userInitComponents(){
        //UDP config
        try {
            
            //uDP SOCKET CONFIG            
            InetAddress receiverAddress = InetAddress.getLocalHost();//local host
            //BUFFERS
            udpSimulinkReceptionBuffer = new byte[SIMULINK_RECEPTION_BUFFER_SIZE];
            udpSimulinkTransmissionBuffer = new byte[SIMULINK_TRANSMISSION_BUFFER_SIZE];
            udpControlFrameTransmissionBuffer = new byte[CONTROLFRAME_TRANSMISSION_BUFFER_SIZE];                                    
            //DATAPACKET 
            SimulinkTransmissionDatagramPacket = new DatagramPacket(udpSimulinkTransmissionBuffer,udpSimulinkTransmissionBuffer.length,
                    receiverAddress,UDP_SIMULINK_PORT_FOR_SCENEFRAME);
            ControlFrameTransmissionDatagramPacket = new DatagramPacket(udpControlFrameTransmissionBuffer,
                    udpControlFrameTransmissionBuffer.length,receiverAddress,UDP_CONTROLFRAME_PORT_FOR_SCENEFRAME);
            SimulinkReceptionDatagramPacket = new DatagramPacket(udpSimulinkReceptionBuffer,udpSimulinkReceptionBuffer.length); 
            //Scenario Thread
            MainScreenScenarioThread = new Control1DHorizontalPanel.ScreenScenarioThread();
            MainScreenScenarioThread.setPriority(RealtimeThread.MAX_PRIORITY);//max priority due to timing that must be preserved.    
            //Cursor Position           
        } catch (Exception ex) {
            Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void paint(Graphics g){//scene 2d graphics
        Graphics2D G2d = (Graphics2D) g;                    
        //get parent frame width
        FrameHeight = ((JFrame)SwingUtilities.getWindowAncestor(this)).getContentPane().getHeight();
        FrameWidth = ((JFrame)SwingUtilities.getWindowAncestor(this)).getContentPane().getWidth();
        //System.out.println("WxH : "+ FrameWidth + "x" + FrameHeight);       //1920x1018
        switch(PanelState){
            case IDLE_STATE :
                screenIdleState(G2d,FrameWidth,FrameHeight);                 
                break;
            case TARGET_APPEAR_STATE:
                targetAppearState(G2d,FrameWidth,FrameHeight,DefaultBciUser.getControl1DHorizontalTargetNumber(),mode);
                break;
            case CURSOR_STATE:        
                cursorState(G2d,FrameWidth,FrameHeight,CURSOR_X,CURSOR_Y,DefaultBciUser.getControl1DHorizontalTargetNumber(),mode);
                break;
            case TARGET_REACHED_STATE:
                targetReachedState(G2d,FrameWidth,FrameHeight,DefaultBciUser.getControl1DHorizontalTargetNumber(),mode);
                break;
            case TARGET_NOT_REACHED_STATE:
                targetNotReachedState(G2d,FrameWidth,FrameHeight);
                break;
            case SCREEN_BLANK_STATE:
                screenBlankState(G2d,FrameWidth,FrameHeight);
                break;
        }                      
       // System.out.println("STATE : "+ PanelState.name());
    }

    private void screenIdleState(Graphics2D g2d,int frameWidth,int frameHeight){
        //set background color to black
        g2d.setColor(Color.black);
        //fill screen
        g2d.fillRect(0,0,FrameWidth,FrameHeight);
        g2d.setColor(Color.GREEN);
        //put green cross
        centerCross(g2d,frameWidth,frameHeight);   
        //display score
        scoreDisplay(g2d,FrameWidth,FrameHeight,SCORE,DefaultBciUser.getNumberOfTrialsPerRun());
    }
        
    private void targetAppearState(Graphics2D g2d,int frameWidth,int frameHeight,int targetNbr,int mode){
        //set background color to black
        g2d.setColor(Color.black);
        //fill screen
        g2d.fillRect(0,0,FrameWidth,FrameHeight);
        //target position
        g2d.setColor(Color.green);
                
        Point Target = getTargetLocation(frameWidth,frameHeight,targetNbr);
        if(mode == 0){
            MoveRectangle(g2d,(int)Target.getX(),0,frameWidth,frameHeight,targetWorkSpacePercentage,mode);
        }else{//main mode
            MoveRectangle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight,targetWorkSpacePercentage,mode);
        }      
        scoreDisplay(g2d,FrameWidth,FrameHeight,SCORE,DefaultBciUser.getNumberOfTrialsPerRun());
    }
    
    private void cursorState(Graphics2D g2d,int frameWidth,int frameHeight,int cursor_x,int cursor_y,int targetNbr,int mode){
        //set background color to black
        g2d.setColor(Color.black);
        //fill screen
        g2d.fillRect(0,0,frameWidth,frameHeight); 
        
        //target position
        g2d.setColor(Color.green);
        Point Target = getTargetLocation(frameWidth,frameHeight,targetNbr);
        if(mode == 0){
            MoveRectangle(g2d,(int)Target.getX(),0,frameWidth,frameHeight,targetWorkSpacePercentage,mode);
        }else if(mode == -1){
            //do nothing dont display target
        }
        else{//main mode
            MoveRectangle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight,targetWorkSpacePercentage,mode);
        }
     /*   
        Target = getTargetLocation(frameWidth,frameHeight,2);
        MoveCircle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight);
        
        Target = getTargetLocation(frameWidth,frameHeight,3);
        MoveCircle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight);
        
        Target = getTargetLocation(frameWidth,frameHeight,4);
        MoveCircle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight);
        
        Target = getTargetLocation(frameWidth,frameHeight,5);
        MoveCircle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight);
        
        Target = getTargetLocation(frameWidth,frameHeight,6);
        MoveCircle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight);
        
        Target = getTargetLocation(frameWidth,frameHeight,7);
        MoveCircle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight);
        
        Target = getTargetLocation(frameWidth,frameHeight,8);
        MoveCircle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight);
      */     
        //cursor blue
        g2d.setColor(Color.blue);
        MoveCircle(g2d,cursor_x,cursor_y,frameWidth,frameHeight,cursorWorkSpacePercentage);
        
        scoreDisplay(g2d,FrameWidth,FrameHeight,SCORE,DefaultBciUser.getNumberOfTrialsPerRun());
    }
    
    private void targetReachedState(Graphics2D g2d,int frameWidth,int frameHeight,int targetNbr,int mode){
        //set background color to black
        g2d.setColor(Color.black);
        //fill screen
        g2d.fillRect(0,0,FrameWidth,FrameHeight);
        //target position
        g2d.setColor(Color.yellow);//target turn yellow
        Point Target = getTargetLocation(frameWidth,frameHeight,targetNbr);
        if(mode == 0){
            MoveRectangle(g2d,(int)Target.getX(),0,frameWidth,frameHeight,targetWorkSpacePercentage,mode);
        }else{//main mode
            MoveRectangle(g2d,(int)Target.getX(),(int)Target.getY(),frameWidth,frameHeight,targetWorkSpacePercentage,mode);
        }
        
        scoreDisplay(g2d,FrameWidth,FrameHeight,SCORE,DefaultBciUser.getNumberOfTrialsPerRun());
    }
    
    private void scoreDisplay(Graphics2D g2d,int frameWidth,int frameHeight,int score,int total){
        int x=frameWidth/2 - 250;
        int y=-frameHeight/2 + 80;
        g2d.setColor(Color.red);
        int fontSize = 80;
        g2d.setFont(new Font("Arial",Font.BOLD,fontSize));
        g2d.drawString(String.valueOf(score)+"/"+String.valueOf(total),(frameWidth/2)+x,(frameHeight/2)-y);
    }
    private void targetNotReachedState(Graphics2D g2d,int frameWidth,int frameHeight){
        //set background color to black
        g2d.setColor(Color.white);//target not reached negative feedback
        //fill screen
        g2d.fillRect(0,0,FrameWidth,FrameHeight);  
        scoreDisplay(g2d,FrameWidth,FrameHeight,SCORE,DefaultBciUser.getNumberOfTrialsPerRun());
    }
    
    private void screenBlankState(Graphics2D g2d,int frameWidth,int frameHeight){
        //set background color to black
        g2d.setColor(Color.black);
        //fill screen
        g2d.fillRect(0,0,FrameWidth,FrameHeight);   
        scoreDisplay(g2d,FrameWidth,FrameHeight,SCORE,DefaultBciUser.getNumberOfTrialsPerRun());
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
    
    private void MoveCircle(Graphics2D g2d,int cursor_x,int cursor_y,int frameWidth,int frameHeight,double pointWorkSpacePercentage ){
        //double diameter = 2*Math.sqrt(targetWorkSpacePercentage*0.01*frameWidth*frameHeight*7/22);
        double diameter = 2*Math.sqrt(pointWorkSpacePercentage*0.01*9*frameHeight*frameHeight*7/(22*16));
        //System.out.println("Dc : "+diameter);
        //center coordinates
        g2d.fillOval((frameWidth/2)+cursor_x-(int)(diameter/2),(frameHeight/2)-cursor_y-(int)(diameter/2),(int)diameter,(int)diameter);
    }
    
    private void MoveRectangle(Graphics2D g2d,int cursor_x,int cursor_y,int frameWidth,int frameHeight,double pointWorkSpacePercentage,int mode ){        
        //System.out.println("Dc : "+diameter);
        //center coordinates
        if(mode == 0){//easy mode            
            double diameter = 2*Math.sqrt(pointWorkSpacePercentage*0.01*9*frameHeight*frameHeight*7/(22*16));
            int width = (int)(frameHeight*0.09);//(int)(frameWidth*rectWorkspacePercentage_w);
            int height = (int)(diameter);
            g2d.fillRect((frameWidth/2)+cursor_x-(int)(width/2),(frameHeight/2)-cursor_y-(int)(height/2),width,height);             
        }else{//main mode
            double diameter = 2*Math.sqrt(pointWorkSpacePercentage*0.01*9*frameHeight*frameHeight*7/(22*16));
            int height = (int)(frameHeight*0.09);
            int width = (int)(diameter);//(int)(frameWidth*rectWorkspacePercentage_h);
            g2d.fillRect((frameWidth/2)+cursor_x-(int)(width/2),(frameHeight/2)-cursor_y-(int)(height/2),width,height);        
        }
    }
    
    //draw center green cross 
    private void centerCross(Graphics2D g2d,int frameWidth,int frameHeight){        
        g2d.drawLine(frameWidth/4,frameHeight/2,frameWidth*3/4,frameHeight/2);
        g2d.drawLine(frameWidth/2,frameHeight/4,frameWidth/2,frameHeight*3/4);
    } 
    
    protected Point getTargetLocation(int frameWidth,int frameHeight,int targetNumber){
        Point Point1 = new Point(0,0);
        
        switch(targetNumber){          
           case 1:
                    Point1.setLocation(-(int)(frameHeight*3/8),(int)(3*frameHeight/8));
                break;
                
           case 2:
                    Point1.setLocation(-(int)(frameHeight/8),(int)(3*frameHeight/8));
                break;  

           case 3:
                    Point1.setLocation((int)(frameHeight/8),(int)(3*frameHeight/8));
                break;
                
           case 4:
                    Point1.setLocation((int)(frameHeight*3/8),(int)(3*frameHeight/8));
                break;
               
           default:
                    Point1.setLocation(-(int)(frameHeight*3/8),(int)(3*frameHeight/8));
                break;
        }
        
        return Point1;
    }
    
    public void startScenario(DatagramSocket ComSocket,BciUser BciUser1){
        DefaultBciUser = BciUser1;//pass the bci user
        PanelDataSocket = ComSocket;     
        
        if(MainScreenScenarioThread.isAlive()){//interrupt if still running
            MainScreenScenarioThread.interrupt();
        }
        MainScreenScenarioThread =  new Control1DHorizontalPanel.ScreenScenarioThread();
        MainScreenScenarioThread.setPriority(RealtimeThread.MAX_PRIORITY);
        MainScreenScenarioThread.start();
    }
    
     class ScreenScenarioThread extends RealtimeThread{
        @Override
        public void run() {           
            try {                 
               //init
                repaint();
                       //CURSOR AT (0,0)
                DELTA_X=0;
                DELTA_Y=0;
                CURSOR_X=0;           
                
                if(mode == 0 || mode == -1){
                    CURSOR_Y=0;
                }else{
                    CURSOR_Y=-(int)(FrameHeight*3/8);
                    if(CURSOR_Y == 0){//prevention on first trial
                        CURSOR_Y = -381;
                    }
                }
                        //
                count = 0;//buffer count at 0                
                timeoutOccured=false;
                targetReached=false;
                boundReached = false;

                startBeep();
                PanelState = Control2DPanelStateEnum.IDLE_STATE;  
                repaint();
                //RealtimeThread.sleep(2500);
                RealtimeThread.sleep(2000);
                
                //target appear
                if(mode != -1){
                    PanelState = Control2DPanelStateEnum.TARGET_APPEAR_STATE;                 
                    repaint();
                }
                initiateBeep();//beep
                RealtimeThread.sleep(1000);                        
                               
                PanelState = Control2DPanelStateEnum.CURSOR_STATE;                
                PanelDataSocket = new DatagramSocket(UDP_LOCAL_PORT); 
                //PanelDataSocket2 = new DatagramSocket(UDP_LOCAL_PORT_TIMEOUT);                
              
                MainTimeoutThread = new Control1DHorizontalPanel.TimeoutThread();
                MainTimeoutThread.setPriority(RealtimeThread.MAX_PRIORITY);
                
                udpSimulinkReceptionBuffer = new byte[SIMULINK_RECEPTION_BUFFER_SIZE];                      
                UdpSimulinkReceptionThread = new Thread(new Control1DHorizontalPanel.UdpSimulinkReceptionRunnable());                
               
                        //sending target number
                udpSimulinkTransmissionBuffer = String.valueOf(DefaultBciUser.getControl1DHorizontalTargetNumber()+2).getBytes();//any number from (3-10)
                SimulinkTransmissionDatagramPacket.setData(udpSimulinkTransmissionBuffer);
                PanelDataSocket.send(SimulinkTransmissionDatagramPacket);
                        //sending start
                udpSimulinkTransmissionBuffer = "1".getBytes();
                SimulinkTransmissionDatagramPacket.setData(udpSimulinkTransmissionBuffer);
                PanelDataSocket.send(SimulinkTransmissionDatagramPacket);
                                
                UdpSimulinkReceptionThread.start();//start simulink Reception Thread to receive coordinates               
                MainTimeoutThread.start();                
                repaint(); 
                
                while(!timeoutOccured && !(targetReached && (mode!=-1))&& !boundReached){};//wait               
              
                MainTimeoutThread.interrupt();
                UdpSimulinkReceptionThread.interrupt();//interrupt reception of datapacket
                int length = count;
                //send end of frame to simulink
                try {
                    udpSimulinkTransmissionBuffer = "2".getBytes();
                    SimulinkTransmissionDatagramPacket.setData(udpSimulinkTransmissionBuffer);                    
                    PanelDataSocket.send(SimulinkTransmissionDatagramPacket);                    
                } catch (IOException ex) {
                    Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
                if(targetReached && mode != -1){
                    PanelState = Control2DPanelStateEnum.TARGET_REACHED_STATE;
                    repaint();            
                    RealtimeThread.sleep(1000);                   
                }else if( (timeoutOccured || boundReached)){
                    PanelState = Control2DPanelStateEnum.TARGET_NOT_REACHED_STATE; 
                    repaint();           
                    RealtimeThread.sleep(1000);                    
                }             
               
                PanelState = Control2DPanelStateEnum.SCREEN_BLANK_STATE;  
                repaint();                
                //RealtimeThread.sleep(4500);                     
                RealtimeThread.sleep(2000);                     
                //Data retrieval and sql connection to database                
                SqlConnection SqlConnection1 = new SqlConnection();
                PreparedStatement PreparedStatement1;                
                
                long startTime = System.currentTimeMillis();
                if(targetReached || timeoutOccured || boundReached){ 
                    
                    SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access                        
                                
                     SqlConnection1.getConn().setAutoCommit(false);//disable autocommit
                     PreparedStatement1=SqlConnection1.getConn().prepareStatement("INSERT INTO trial_coordinates(coord_x,coord_y,trial_id)"
                                + " VALUES(?,?,?)");                  
                    
                    byte[] temp = new byte[4];
                    int j = 0;
                    for(int i = 0 ;i < length;i++ ){                       
                           temp[j++]=udpSimulinkReceptionBuffer[i];                                              
                        if(j==4){
                            //CHANGE CURSOR COORDINATES
                            int unsigned_dx = (((temp[1]>=0)?temp[1]:(temp[1]+256))<<8) + ((temp[0]>=0)?temp[0]:(temp[0]+256));
                            int unsigned_dy = (((temp[3]>=0)?temp[3]:(temp[3]+256))<<8) + ((temp[2]>=0)?temp[2]:(temp[2]+256));                        
                            int dx = ((unsigned_dx&0x8000)==0x8000)?(-65536+unsigned_dx):unsigned_dx;                                         
                            int dy = ((unsigned_dy&0x8000)==0x8000)?(-65536+unsigned_dy):unsigned_dy;                        
                            
                            PreparedStatement1.setInt(1,dx);//load dX Value
                            PreparedStatement1.setInt(2,dy);//load dY Value
                            PreparedStatement1.setInt(3,DefaultBciUser.getTrialId());//load dY Value
                            PreparedStatement1.addBatch();
                            j = 0;
                        }
                    } 
                        int[] len = PreparedStatement1.executeBatch();
                        SqlConnection1.getConn().commit();
                        //System.out.println("EXC : "+ len.length);
                             
                   SqlConnection SqlConnection2 = new SqlConnection();
                   SqlConnection2.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access            
                
                    if(targetReached){
                        SqlConnection2.executeUpdate("UPDATE bci_trial "
                                + "SET trial_pass_or_fail=TRUE WHERE id="+DefaultBciUser.getTrialId()+";");                    
                    }else{
                        SqlConnection2.executeUpdate("UPDATE bci_trial "
                                + "SET trial_pass_or_fail=FALSE WHERE id="+DefaultBciUser.getTrialId()+";");                    
                    }
                
                    SqlConnection2.disconnect();
                    SqlConnection1.disconnect();
                }
                
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                System.out.println(elapsedTime + "ms");
                
                 //send udp packet to settings frame for end of scene
              
                 PanelTcpSocket = new Socket(InetAddress.getLocalHost(),UDP_CONTROLFRAME_PORT_FOR_SCENEFRAME);
                 TcpOutputStream = PanelTcpSocket.getOutputStream();
                 udpControlFrameTransmissionBuffer = "2".getBytes();             
                 TcpOutputStream.write(udpControlFrameTransmissionBuffer);
                 TcpOutputStream.flush();
                      
                 PanelDataSocket.close();
                 PanelTcpSocket.close();
                 TcpOutputStream.close();
                 //System.out.println("closed!");
                     
            } catch (Exception ex) {
                Logger.getLogger(ScreeningPanel.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }        
    }
    
    
    class TimeoutThread extends RealtimeThread{
        @Override
        public void run() {
            try {
                RealtimeThread.sleep(CURSOR_TIMEOUT_TIME);
                timeoutOccured = true;//Set Flag For TimoutOccured
                 System.out.println("Timeout");
            } catch (InterruptedException ex) {
                Logger.getLogger(Control2DPanel.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }  
    }
    
    class UdpSimulinkReceptionRunnable implements Runnable{
            @Override
            public void run(){ // pause this code due to cpu demands              
                    byte [] tempBuff;// 4bytes = 132 (x2 : prevention) 
                   
                   while(!Thread.interrupted()){
                       try {
                            //listening for as long as thread not interrupted
                            PanelDataSocket.receive(SimulinkReceptionDatagramPacket);
                        } catch (IOException ex) {
                            Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     //System.out.println("COUNT : " + count);
                     tempBuff = SimulinkReceptionDatagramPacket.getData();
                     int lengthReceived = SimulinkReceptionDatagramPacket.getLength();                                                     
                                         
                     for(int i = 0 ; i < lengthReceived;i++){
                       udpSimulinkReceptionBuffer[count++] = tempBuff[i];//save every data in main buffer
                     }                       
                     //CHANGE CURSOR COORDINATES                     
                       //System.out.println("bo : "+tempBuff[0] + "b1  :"+tempBuff[1] + "b2  :"+tempBuff[2]+ "b3  :"+tempBuff[3]);
                     //lsb first  x first then y
                     int unsigned_dx = (((tempBuff[1]>=0)?tempBuff[1]:(tempBuff[1]+256))<<8) + ((tempBuff[0]>=0)?tempBuff[0]:(tempBuff[0]+256));
                     int unsigned_dy = (((tempBuff[3]>=0)?tempBuff[3]:(tempBuff[3]+256))<<8) + ((tempBuff[2]>=0)?tempBuff[2]:(tempBuff[2]+256));
                   
                     
                     DELTA_X = ((unsigned_dx&0x8000)==0x8000)?(-65536+unsigned_dx):unsigned_dx;                                         
                     DELTA_Y = ((unsigned_dy&0x8000)==0x8000)?(-65536+unsigned_dy):unsigned_dy;      
                     
                     //System.out.println("DX : "+DELTA_X + "DY  :"+DELTA_Y);
                     
                     CURSOR_X += DELTA_X;
                     CURSOR_Y += DELTA_Y;
                            //COLLISION DETECT
                     int targetNbr = DefaultBciUser.getControl1DHorizontalTargetNumber();                     
                     Point Target = getTargetLocation(FrameWidth,FrameHeight,targetNbr);
                     //int distance = getEuclideanDistance(CURSOR_X,CURSOR_Y,(int)Target.getX(),(int)Target.getY());
                     int dx = (int) Math.abs(Target.getX()-CURSOR_X);
                     int dy = (int) Math.abs(Target.getY()-CURSOR_Y);
                     
                     //double radiusCursor = Math.sqrt(cursorWorkSpacePercentage*0.01*9*FrameHeight*FrameHeight*7/(22*16));                    
                     
                     repaint();                    
                     
                    if(mode == 0){
                        double widthTarget = (int)(FrameHeight*0.09);
                        //System.out.println("WT : " + widthTarget);
                        if(dx < widthTarget){
                            targetReached = true;
                            SCORE = SCORE + 1;
                        }
                    }else{//main mode
                        double diameter = 2*Math.sqrt(targetWorkSpacePercentage*0.01*9*FrameHeight*FrameHeight*7/(22*16));                    
                        double widthTarget = (int)diameter/2;
                        if( (dy < FrameHeight*0.045) && (dx < widthTarget)){//collision occured
                             targetReached = true;
                             SCORE = SCORE + 1;
                        }
                    }
                    
                    if((CURSOR_X > (FrameWidth/2)) || (CURSOR_X < (-FrameWidth/2)) || (CURSOR_Y > (FrameHeight/2)) || (CURSOR_Y < (-FrameHeight/2))){
                       if(mode != -1) {
                        boundReached = true;
                       }else{//testing mode
                           //CURSOR_X = 0;//(CURSOR_X>0)?FrameWidth/2:-FrameWidth/2;
                           //CURSOR_Y = 0;//(CURSOR_Y>0)?FrameHeight/2:-FrameHeight/2;
                       }
                    }
                  }           
            }            
    }
    
    private int getEuclideanDistance(int x0,int y0,int x1,int y1){
        return (int)Math.sqrt(Math.pow(x0-x1,2) + Math.pow(y0-y1,2));
    }
      
    
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
