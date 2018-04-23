/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_bci_platform;

import com.user.bci.BciPhaseEnum;
import com.user.bci.BciUser;
import com.user.bci.ImageryEnum;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * @author Yves Matanga
 */
public class SceneFrame extends javax.swing.JFrame {
    //database connection details
    String RDBMS = "mysql";
    String DB_NAME = "mtech_research";
    String SQL_USER_NAME = "root";
    String SQL_USER_PASSWORD = "root";
    
    //BCi Phases Panels
    protected ScreeningPanel MainScreeningPanel ;
    protected Control2DPanel MainControl2DPanel;
    protected Control1DHorizontalPanel  MainControl1DHorizontalPanel;
    protected Control1DVerticalPanel MainControl1DVerticalPanel;
    
    protected ImageryEnum ImageryType = ImageryEnum.LEFT_FOOT;
    //UDP Control
    protected final int UDP_LOCAL_PORT = 8890;//local end point    
    protected final int UDP_SIMULINK_PORT = 8889;//end point in simulink
    
    protected final int RECEPTION_BUFFER_SIZE = 1;//smal instruction from scene frame
    protected final int TRANSMISSION_BUFFER_SIZE = 1;//smal instruction to setting frame and simulink
    protected Thread UdpReceptionThread;
    
    protected DatagramSocket MainDatagramSocket;
    protected DatagramSocket MainDatagramSocket2;
    
    protected DatagramPacket ReceptionDatagramPacket;
    protected DatagramPacket TransmissionDatagramPacket;
    protected byte[] udpReceptionBuffer;
    protected byte[] udpTransmissionBuffer;
    
    //SETTINGS FRAME DECODING
    
    protected final byte START_SCREENING = 0x00;
    protected final byte STOP_SCREENING = 0x01;
    
    /**
     * Creates new form SceneFrame
     */
    public SceneFrame() {
        initComponents();
        
        MainScreeningPanel = new ScreeningPanel(); 
        MainControl2DPanel = new Control2DPanel();
        MainControl1DHorizontalPanel = new Control1DHorizontalPanel();
        MainControl1DVerticalPanel = new Control1DVerticalPanel();
        
        this.setContentPane(MainScreeningPanel);
        userInitComponents();        
    }
    
    public void userInitComponents(){
        try {
            //Thread                
                        UdpReceptionThread = new Thread(new UdpReceptionRunnable());
                        //uDP SOCKET CONFIG
                        udpReceptionBuffer = new byte[RECEPTION_BUFFER_SIZE];
                        udpTransmissionBuffer = new byte[TRANSMISSION_BUFFER_SIZE];
                        TransmissionDatagramPacket = new DatagramPacket(udpTransmissionBuffer,udpTransmissionBuffer.length);       
                        ReceptionDatagramPacket = new DatagramPacket(udpReceptionBuffer,udpReceptionBuffer.length);       
                        
                        MainDatagramSocket = null;//new DatagramSocket(UDP_LOCAL_PORT);       
                                                
                        //Start Udp Wiat
                        UdpReceptionThread.start();
        } catch (/*Socket*/Exception ex) {
            Logger.getLogger(SceneFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
     class UdpReceptionRunnable implements Runnable{
            @Override
            public void run() {         
            }            
    }
     
     public void startScenario(BciUser DefaultBciUser,BciPhaseEnum DefaultBciPhase){ 
           switch(DefaultBciPhase){
               case SCREENING_PHASE:     
                     getContentPane().removeAll();
                     setContentPane(MainScreeningPanel);
                     getContentPane().revalidate();
                     getContentPane().repaint();                                       
                     MainScreeningPanel.startScenario(MainDatagramSocket,DefaultBciUser.getCurrentImagery());                     
                   break;
                   
               case CONTROL_2D_PHASE:      
                      getContentPane().removeAll();
                      setContentPane(MainControl2DPanel);
                      getContentPane().revalidate();
                      getContentPane().repaint();                      
                      MainControl2DPanel.startScenario(MainDatagramSocket,DefaultBciUser);                      
                   break;
                   
               case HORIZONTAL_1D_PHASE:
                      getContentPane().removeAll();
                      setContentPane(MainControl1DHorizontalPanel);
                      getContentPane().revalidate();
                      getContentPane().repaint(); 
                      MainControl1DHorizontalPanel.startScenario(MainDatagramSocket,DefaultBciUser);        
                    break;
               
               case VERTICAL_1D_PHASE:
                     getContentPane().removeAll();
                     setContentPane(MainControl1DVerticalPanel);
                     getContentPane().revalidate();
                     getContentPane().repaint();
                     MainControl1DVerticalPanel.startScenario(MainDatagramSocket,DefaultBciUser);
                   break;
                   
               default:                   
                   break;
           }         
     }
     
     public void stopScenario(BciPhaseEnum DefaultBciPhase){
           switch(DefaultBciPhase){
               case SCREENING_PHASE:
                     //MainScreeningPanel.stopScenario();
                   break;
                   
               default:                   
                   break;
           }         
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Scene Frame");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SceneFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SceneFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SceneFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SceneFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SceneFrame MainSceneFrame1 = new SceneFrame();
                MainSceneFrame1.setVisible(true);
                MainSceneFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                BciUser BciUser1 = new BciUser();
                BciUser1.setTrialId(173);
                BciUser1.setSessionId(15);
                BciUser1.setControl2DTargetNumber(1);
                MainSceneFrame1.startScenario(BciUser1,BciPhaseEnum.CONTROL_2D_PHASE);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
