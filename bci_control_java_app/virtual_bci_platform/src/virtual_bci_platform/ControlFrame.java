/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_bci_platform;

import com.user.bci.BciPhaseEnum;
import com.user.bci.BciUser;
import com.user.sql.SqlConnection;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;


/**
 *
 * @author Yves Matanga
 */
public class ControlFrame extends javax.swing.JFrame {
    
    //database connection details
    String RDBMS = "mysql";
    String DB_NAME = "mtech_research";
    String SQL_USER_NAME = "root";
    String SQL_USER_PASSWORD = "root";
    
    protected DefaultListModel BciUserListModel = null;
    protected BciUser DefaultBciUser = null;
            //UDP PROTOCOL
    protected final int UDP_LOCAL_PORT_FOR_SIMULINK = 8888;//local end point
    protected final int UDP_LOCAL_PORT_FOR_SCENEFRAME = 8887;//local end point
    protected final int UDP_SIMULINK_PORT = 8889;//end point in simulink
    protected final int UDP_SCENEFRAME_PORT = 8890;//end point in scene frame
    
    protected final int SIMULINK_RECEPTION_BUFFER_SIZE = 50000000;//830000;//15s eeg sig : 33 channels : 4bytes /channel 250 sps (400KB)    (~500KB for control2dpahse)
    protected final int SCENEFRAME_RECEPTION_BUFFER_SIZE = 1;//1 byte only for signaling information
    protected final int SCENEFRAME_TRANSMISSION_BUFFER_SIZE = 1;//smal instruction to setting frame
    protected final int SIMULINK_TRANSMISSION_BUFFER_SIZE = 1;//smal instruction to setting frame
    
    protected Thread UdpSimulinkReceptionThread;
    protected UdpSimulinkReceptionRunnable UdpSimulinkReceptionRunnable1;
    protected boolean StopSimulinkRunnable;
    protected Thread UdpSceneFrameReceptionThread;        
    
    protected DatagramSocket ControlFrameSimulinkDatagramSocket;
    protected DatagramSocket ControlFrameSceneFrameDatagramSocket;
    protected ServerSocket ControlFrameTcpSocket;
    
    protected DatagramPacket SimulinkReceptionDatagramPacket;
    protected DatagramPacket SimulinkTransmissionDatagramPacket;
    
    protected DatagramPacket SceneFrameTransmissionDatagramPacket;
    protected DatagramPacket SceneFrameReceptionDatagramPacket;
    
    protected byte[] udpSimulinkReceptionBuffer;
    protected byte[] udpSimulinkTransmissionBuffer;
    protected byte[] udpSceneFrameTransmissionBuffer; 
    protected byte[] udpSceneFrameReceptionBuffer; 
    
    //database 
    final String eegSignalsQuery = "insert into eeg_signals(fp1,fp2,af3,af4,f7,f3,fz,f4"
                                      + ",f8,fc5,fc1,fc2,fc6,t7,c3,cz,c4,t8,cp5,cp1,cp2,"
                                      + "cp6,p7,p3,pz,p4,p8,po7,po3,po4,po8,oz,trial_id) "+
               "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
           
    //setting frame datas
    protected final byte START_SCREENING = 0x00;
    protected final byte STOP_SCREENING = 0x01;
    
    //other Frames
    protected SceneFrame MainSceneFrame ;//scene frame
    protected EEGDatabaseFrame DatabaseFrame;
    protected EEGChannelSelectorFrame ChannelSelectorFrame;
        //sceneframe random screening generator two class
    private int count=0;    
    /**
     * Creates new form ControlFrame
     */
    public ControlFrame() {      
        initComponents();
        userInitComponents();//user hard coded initialization   
        DatabaseFrame = null;
        ChannelSelectorFrame = null;
        MainSceneFrame = new SceneFrame();
        MainSceneFrame.setVisible(true);          
    }
    
    private void userInitComponents(){        
        try {
                //Event When Winow is Opened
                SqlConnection SqlConnection1 = new SqlConnection();
                SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access
                    //load bci phase
                Statement Statement1 = SqlConnection1.createStatement();
                ResultSet ResultSet1 = Statement1.executeQuery("SELECT phase_name FROM bci_phase;");
                    while(ResultSet1.next()){
                        String phase_name = ResultSet1.getString("phase_name");                        
                        BciPhaseComboBox.addItem(phase_name);
                    }
                ResultSet1.close();
                
                    //load user list                    
                BciUserListModel = new DefaultListModel();
                ResultSet1 = Statement1.executeQuery("SELECT * FROM bci_users;");                
                
                DefaultBciUser = new BciUser();
                
                
                if(ResultSet1.next()){//there is a  user
                        //move cursor backward once to compensate
                        ResultSet1.previous();
                    //select the first element
                      while(ResultSet1.next()){                        
                        String surname = ResultSet1.getString("surname");
                        String initials = ResultSet1.getString("initials");
                        BciUserListModel.addElement(surname + " " + initials);//add user from the database to the list  
                                  
                        if(ResultSet1.getRow() == 1){                            
                            DefaultBciUser.setUserId(ResultSet1.getInt("id"));
                            DefaultBciUser.setSurname(surname);
                            DefaultBciUser.setInitials(initials);
                            DefaultBciUser.setAge(ResultSet1.getInt("age"));
                            DefaultBciUser.setGender(ResultSet1.getString("gender"));
                            DefaultBciUser.setPath(ResultSet1.getString("image_path").replace('-','\\'));/*put back \ character removed in sql*/                            
                        }
                      }
                  
                DefaultBciUser=BciUser.loadSessionFromDb(DefaultBciUser,DefaultBciUser.getUserId(),1);/*
                 * 
                 * load first user session with screening phase information
                 */                   
                        
                BciUserList.setModel(BciUserListModel);//bind to jlist                
                BciUserList.setSelectedIndex(0);//select first element 
                
                //allow change in configuration only when a new session comes in
                     boolean change = false;
                     if(DefaultBciUser.getCurrentRun()==1 && DefaultBciUser.getCurrentTrial()==1){
                         change=true;
                     }else{
                         change=false;
                     }
                     Class1ImageryComboBox.setEnabled(change);
                     Class2ImageryComboBox.setEnabled(change);
                     RunsPerSessionSpinner.setEnabled(change);
                     TrialsPerRunSpinner.setEnabled(change); 
                     SaveButton.setEnabled(change);
                     ChannelSelectButton.setEnabled(change);
                }
                
                viewLoadBciUser(DefaultBciUser);//loadto GUI               
                SqlConnection1.disconnect();
                
                //Thread                     
                    UdpSimulinkReceptionRunnable1 = new UdpSimulinkReceptionRunnable();                                        
                    //UdpSceneFrameReceptionThread = new Thread(new UdpSceneFrameReceptionRunnable());
                    //uDP SOCKET CONFIG
                        //BUFFERS
                    udpSimulinkReceptionBuffer = new byte[SIMULINK_RECEPTION_BUFFER_SIZE];
                    udpSceneFrameReceptionBuffer = new byte[SCENEFRAME_RECEPTION_BUFFER_SIZE];
                    udpSceneFrameTransmissionBuffer = new byte[SCENEFRAME_TRANSMISSION_BUFFER_SIZE];
                    udpSimulinkTransmissionBuffer = new byte[SIMULINK_TRANSMISSION_BUFFER_SIZE];                    
                        //DATAPACKETS
                    SceneFrameTransmissionDatagramPacket = new DatagramPacket(udpSceneFrameTransmissionBuffer,udpSceneFrameTransmissionBuffer.length,InetAddress.getLocalHost(),UDP_SCENEFRAME_PORT);
                    SimulinkTransmissionDatagramPacket = new DatagramPacket(udpSimulinkTransmissionBuffer,udpSimulinkTransmissionBuffer.length,InetAddress.getLocalHost(),UDP_SIMULINK_PORT); 
                    SceneFrameReceptionDatagramPacket = new DatagramPacket(udpSceneFrameReceptionBuffer,udpSceneFrameReceptionBuffer.length);       
                    SimulinkReceptionDatagramPacket = new DatagramPacket(udpSimulinkReceptionBuffer,udpSimulinkReceptionBuffer.length);       
                        //SOCKETS
                    //ControlFrameSimulinkDatagramSocket = new DatagramSocket(UDP_LOCAL_PORT_FOR_SIMULINK);
                    //ControlFrameSceneFrameDatagramSocket = new DatagramSocket(UDP_LOCAL_PORT_FOR_SCENEFRAME);
                    ControlFrameTcpSocket = new ServerSocket(UDP_LOCAL_PORT_FOR_SCENEFRAME);
                        //START UDP THREADS
                        //UdpSimulinkReceptionThread.start();//only when some press start
                        //UdpSceneFrameReceptionThread.start();
            } catch (Exception ex) {
                Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        //bind listeners
               RegisterButton.addActionListener(new RegisterButtonListener());
               DoneButton.addActionListener(new DoneButtonListener());
               LoadButton.addActionListener(new LoadButtonListener());
               ProfileImage.addMouseListener(new ProfileImageListener());
               DeleteButton.addActionListener(new DeleteButtonListener());
               UserListButton.addActionListener(new UserListButtonListener());
               StartScenarioButton.addActionListener(new StartScenarioButtonListener());
               StopScenarioButton.addActionListener(new StopScenarioButtonListener());
               SaveButton.addActionListener(new SaveButtonListener());
               BciPhaseComboBox.addActionListener(new BciPhaseComboBoxListener());
               DatabaseButton.addActionListener(new DatabaseButtonListener());
               ChannelSelectButton.addActionListener(new ChannelSelectButtonListener());
    }
    
    public void viewLoadBciUser(BciUser BciUser1){
        
        this.SurnameEditText.setText(BciUser1.getSurname());
        this.InitialsEditText.setText(BciUser1.getInitials());
        this.AgeEditText.setText(Integer.toString(BciUser1.getAge()));
        this.GenderEditText.setText(BciUser1.getGender());
        
        SessionNumberTextField.setText(Integer.toString(BciUser1.getCurrentSession()));
        RunNumberTextField.setText(Integer.toString(BciUser1.getCurrentRun())+"/"+BciUser1.getNumberOfRunsPerSession());
        TrialNumberTextField.setText(Integer.toString(BciUser1.getCurrentTrial())+"/"+BciUser1.getNumberOfTrialsPerRun());
        
        RunsPerSessionSpinner.setValue(BciUser1.getNumberOfRunsPerSession());
        TrialsPerRunSpinner.setValue(BciUser1.getNumberOfTrialsPerRun());
        
        String Path;
        
        if(BciUser1.getFilePath().equals("")){       
            Path = "D:\\Software Programming\\java\\projects\\virtual_bci_platform\\src\\img\\unknown_profile.jpg";
        }else{
            Path = BciUser1.getFilePath();            
        }
        
        try{
        BufferedImage img = ImageIO.read(new File(Path));
        ProfileImage.setIcon(new ImageIcon( img.getScaledInstance(ProfileImage.getWidth(),                                
                                ProfileImage.getHeight(),Image.SCALE_SMOOTH)));//set icon
        }catch(Exception Ex){
            Logger.getLogger(SceneFrame.class.getName()).log(Level.SEVERE, null, Ex);
        }
    }
    
    /*Listeners*/
    
    class RegisterButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            
           boolean toggle;
           if(RegisterButton.getText().equals("Register")){
              toggle = true;
              RegisterButton.setText("Cancel");//change to cancel should the user wish to cancel the operation
           }else{
              toggle = false;
              RegisterButton.setText("Register");
           }
           
           DoneButton.setEnabled(toggle);//enable done button for user to register
           SurnameEditText.setEditable(toggle);//allow edition
           InitialsEditText.setEditable(toggle);
           AgeEditText.setEditable(toggle);
           GenderEditText.setEditable(toggle);           
        }        
    }
    
    class DoneButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            
            DefaultBciUser.setSurname(SurnameEditText.getText());
            DefaultBciUser.setInitials(InitialsEditText.getText());
            DefaultBciUser.setAge(Integer.valueOf(AgeEditText.getText()));
            DefaultBciUser.setGender(GenderEditText.getText());
            
            //limit value to above 1
           if((int)RunsPerSessionSpinner.getValue() >= 1){
            DefaultBciUser.setNumberOfRunsPerSession((int)RunsPerSessionSpinner.getValue());
           }
           else{
            DefaultBciUser.setNumberOfRunsPerSession(1);
            RunsPerSessionSpinner.setValue(1);
           }
           //limit value to above 1
           if((int)TrialsPerRunSpinner.getValue() >= 1){
            DefaultBciUser.setNumberOfTrialsPerRun((int)TrialsPerRunSpinner.getValue());
           }
           else{
            DefaultBciUser.setNumberOfTrialsPerRun(1);
            TrialsPerRunSpinner.setValue(1);
           }             
            
           if ( !DefaultBciUser.getSurname().equals("")){
               if(!DefaultBciUser.getInitials().equals("")){
                   if(DefaultBciUser.getAge() != 0){
                        if(!DefaultBciUser.getGender().equals("")){
                            try {
                                //insert new user in database
                                SqlConnection SqlConnection1 = new SqlConnection();
                                SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);
                                SqlConnection1.executeUpdate("INSERT INTO bci_users(surname,initials,age,gender,image_path)"
                                       + "values('"+SurnameEditText.getText()+"','"+InitialsEditText.getText()
                                        +"','"+ AgeEditText.getText() +"','"+GenderEditText.getText()+"','"+
                                        DefaultBciUser.getFilePath().replace('\\','-')+"');");//to avoid sql escape of \                            
                                
                                ResultSet ResultSet1 = SqlConnection1.executeQuery("SELECT id FROM bci_users "
                                        + "WHERE surname='"+SurnameEditText.getText()+"' AND"
                                        + " initials='"+InitialsEditText.getText()+"';");
                                
                                ResultSet1.next();
                                DefaultBciUser.setUserId(ResultSet1.getInt("id"));
                                
                                SqlConnection1.disconnect();
                                                    
                                DoneButton.setEnabled(false);
                                SurnameEditText.setEditable(false);
                                InitialsEditText.setEditable(false);
                                AgeEditText.setEditable(false);
                                GenderEditText.setEditable(false);
                                
                                BciUserListModel.addElement(SurnameEditText.getText()+" "+ InitialsEditText.getText());
                            } catch (SQLException ex) {
                                Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                   }
               }
           }
        }        
    }
   
    class LoadButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
          //load selected user in the list
            SqlConnection SqlConnection1 = new SqlConnection();
            
           try{
               
            String temp = (String) BciUserList.getSelectedValue();//made of Surname + initials
            String array[] = temp.split(" ");
            
            String surname = array[0];
            String initials = array[1];
            
            SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);
            ResultSet ResultSet1 = SqlConnection1.executeQuery("SELECT * FROM bci_users WHERE "
                    + "(surname = '"+surname+"' AND initials = '"+initials+"');");           
            
            if(ResultSet1.next()){//take first one found
                   DefaultBciUser.setUserId(ResultSet1.getInt("id"));
                   DefaultBciUser.setSurname(ResultSet1.getString("surname"));
                   DefaultBciUser.setInitials(ResultSet1.getString("initials"));
                   DefaultBciUser.setAge(ResultSet1.getInt("age"));
                   DefaultBciUser.setGender(ResultSet1.getString("gender"));
                   DefaultBciUser.setPath(ResultSet1.getString("image_path").
                           replace("-","\\"));//bring back \ character as avoided in sql eegSignalsQuery                   
                   
                   DefaultBciUser=BciUser.loadSessionFromDb(DefaultBciUser,DefaultBciUser.getUserId(),1);/*
                 * load loaded user session with screening phase information
                 */
                   //allow change in configuration only when a new session comes in
                     boolean change = false;
                     if(DefaultBciUser.getCurrentRun()==1 && DefaultBciUser.getCurrentTrial()==1){
                         change=true;
                     }else{
                         change=false;
                     }
                     Class1ImageryComboBox.setEnabled(change);
                     Class2ImageryComboBox.setEnabled(change);
                     RunsPerSessionSpinner.setEnabled(change);
                     TrialsPerRunSpinner.setEnabled(change);
                     SaveButton.setEnabled(change);
                     ChannelSelectButton.setEnabled(change);
            }
                    //
            SqlConnection1.disconnect();
            BciPhaseComboBox.setSelectedIndex(0);//point to screening phase
            viewLoadBciUser(DefaultBciUser);//load BciUser to GUI
            RunProgressBar.setValue(0);//re-init to zero
            
           }catch(Exception Ex){//in case of exception
               //Ex.printStackTrace(); 
               //Do nothing
           }
        }
    }
   
    class ProfileImageListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
           
        }
        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {
            if(DoneButton.isEnabled()){//if done button is enabled
              try{ 
                    JFileChooser FileChooser1 = new JFileChooser();
                    int result = FileChooser1.showOpenDialog(rootPane);
                    if(result == JFileChooser.APPROVE_OPTION){//if user selected a file
                        File SelectedFile = FileChooser1.getSelectedFile();
                        BufferedImage img = ImageIO.read(SelectedFile);
                        DefaultBciUser.setPath(SelectedFile.getAbsolutePath());//save path to bci user object                       
                        ProfileImage.setIcon(new ImageIcon( img.getScaledInstance(ProfileImage.getWidth(),
                                ProfileImage.getHeight(),Image.SCALE_SMOOTH)));//set icon
                    }
              }catch(Exception Ex){
                  Ex.printStackTrace();
              }
           }
        }
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseExited(MouseEvent e) {}      
    }
    
    class DeleteButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            //delete bci user from list
           try{
            //BciUserList.remove();//remove element selected from list only
            BciUserListModel.remove(BciUserList.getSelectedIndex());
           }catch(Exception Ex){
            //do nothing   
           }
        }    
    }
    
    class UserListButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
           if(DefaultBciUser.getUserId()!=0) {
            try {
                //Event When Winow is Opened
                SqlConnection SqlConnection1 = new SqlConnection();
                SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access                                        
                    //load user list                    
                BciUserListModel = new DefaultListModel();
                ResultSet ResultSet1 = SqlConnection1.executeQuery("SELECT * FROM bci_users;");
                
                DefaultBciUser = new BciUser();//initial default bci user
                
                    //select the first element
                      while(ResultSet1.next()){
                        String surname = ResultSet1.getString("surname");
                        String initials = ResultSet1.getString("initials");
                        BciUserListModel.addElement(surname + " " + initials);//add user from the database to the list  
                                  
                        if(ResultSet1.getRow() == 1){
                            DefaultBciUser.setUserId(ResultSet1.getInt("id"));
                            DefaultBciUser.setSurname(surname);
                            DefaultBciUser.setInitials(initials);
                            DefaultBciUser.setAge(ResultSet1.getInt("age"));
                            DefaultBciUser.setGender(ResultSet1.getString("gender"));
                            DefaultBciUser.setPath(ResultSet1.getString("image_path").replace('-','\\'));/*put back \ character removed in sql*/                            
                        }
                    }
                    
                DefaultBciUser=BciUser.loadSessionFromDb(DefaultBciUser,DefaultBciUser.getUserId(),1);/*
                 * load first user session with screening phase information
                 */
                
                //allow change in configuration only when a new session comes in
                     boolean change = false;
                     if(DefaultBciUser.getCurrentRun()==1 && DefaultBciUser.getCurrentTrial()==1){
                         change=true;
                     }else{
                         change=false;
                     }
                     Class1ImageryComboBox.setEnabled(change);
                     Class2ImageryComboBox.setEnabled(change);
                     RunsPerSessionSpinner.setEnabled(change);
                     TrialsPerRunSpinner.setEnabled(change);
                     SaveButton.setEnabled(change);
                     ChannelSelectButton.setEnabled(change);
                     
                BciUserList.setModel(BciUserListModel);//bind to jlist
                try{
                    BciUserList.setSelectedIndex(0);}//select first element
                catch(Exception ex){
                    ex.printStackTrace();
                }
                viewLoadBciUser(DefaultBciUser);//loadto GUI
                
                SqlConnection1.disconnect();
            } catch (SQLException ex) {
                Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
          } 
       }
    }
   
    class StartScenarioButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(DefaultBciUser.getUserId()!=0 && ((DefaultBciUser.getBciPhaseState()==BciPhaseEnum.SCREENING_PHASE) || (DefaultBciUser.getBciPhaseState()==BciPhaseEnum.CONTROL_2D_PHASE)
                    || (DefaultBciUser.getBciPhaseState()==BciPhaseEnum.HORIZONTAL_1D_PHASE) || (DefaultBciUser.getBciPhaseState()==BciPhaseEnum.VERTICAL_1D_PHASE))){
                try {                   
                    //update Model
                        //init
                   SqlConnection SqlConnection1 = new SqlConnection();
                   SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access
                   ResultSet ResultSet1;
                   
                   //create run in db
                        Date DateNow = new Date();
                        SimpleDateFormat Ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
              
                         SqlConnection1.executeUpdate("insert into bci_run(session_id,run_date)"
                            + " values("+DefaultBciUser.getSessionId()+",'"+Ft.format(DateNow)+"');"); 
                   //Get Run Id
                        ResultSet1 = SqlConnection1.executeQuery("SELECT MAX(id) AS latest_run_id"
                                + "  FROM bci_run;");
                      
                        ResultSet1.next();
                        int latestRunId = ResultSet1.getInt("latest_run_id");                                      
                        
                        ResultSet1.next();                                               
                        DefaultBciUser.setRunId(latestRunId);                                           
                        DefaultBciUser.setCurrentTrial(1);//initial trial
                                                    
                        //create trial                         
                       if(DefaultBciUser.getBciPhaseState().equals(BciPhaseEnum.SCREENING_PHASE) ){
                       DefaultBciUser.generateClassSequence();//generate imagery sequence
                       DefaultBciUser.setCurrentImagery(DefaultBciUser.getImageryClassSequence().get(DefaultBciUser.getCurrentTrial()-1));//set current imagery from random list
                       SqlConnection1.executeUpdate("INSERT INTO bci_trial(run_id,imagery_type_id)"
                                    + " VALUES("+DefaultBciUser.getRunId()+","+
                                BciUser.getImageryTypeId(DefaultBciUser.getCurrentImagery())+");");    
                       }else if(DefaultBciUser.getBciPhaseState().equals(BciPhaseEnum.VERTICAL_1D_PHASE) ){ 
                       DefaultBciUser.generate1DVerticalTargetSequence();
                       DefaultBciUser.setControl1DVerticalTargetNumber(DefaultBciUser.getTarget1DVerticalSequence().get(DefaultBciUser.getCurrentTrial()-1));
                       SqlConnection1.executeUpdate("INSERT INTO bci_trial(run_id,target_number)"
                                    + " VALUES("+DefaultBciUser.getRunId()+","+
                                DefaultBciUser.getControl1DVerticalTargetNumber()+");");
                       }else if(DefaultBciUser.getBciPhaseState().equals(BciPhaseEnum.HORIZONTAL_1D_PHASE) ){
                       DefaultBciUser.generate1DHorizontalTargetSequence();
                       DefaultBciUser.setControl1DHorizontalTargetNumber(DefaultBciUser.getTarget1DHorizontalSequence().get(DefaultBciUser.getCurrentTrial()-1));
                       SqlConnection1.executeUpdate("INSERT INTO bci_trial(run_id,target_number)"
                                    + " VALUES("+DefaultBciUser.getRunId()+","+
                                DefaultBciUser.getControl1DHorizontalTargetNumber()+");");
                       }else if(DefaultBciUser.getBciPhaseState().equals(BciPhaseEnum.CONTROL_2D_PHASE) ){
                       DefaultBciUser.generate2DTargetSequence();//for 2d class control
                       DefaultBciUser.setControl2DTargetNumber(DefaultBciUser.getTarget2DSequence().get(DefaultBciUser.getCurrentTrial()-1));//control 2d control
                       SqlConnection1.executeUpdate("INSERT INTO bci_trial(run_id,target_number)"
                                    + " VALUES("+DefaultBciUser.getRunId()+","+
                                DefaultBciUser.getControl2DTargetNumber()+");");
                       }
                         
                      //get trial id
                       ResultSet1 = SqlConnection1.executeQuery("SELECT MAX(id) AS latest_trial_id FROM bci_trial;");                       
                       int trialId=0;                       
                       ResultSet1.next();
                       trialId = ResultSet1.getInt("latest_trial_id");                   
                       DefaultBciUser.setTrialId(trialId);                       
                                                 
                        ResultSet1.close();
                        SqlConnection1.disconnect();
                    
                    //update View
                    SessionNumberTextField.setText(Integer.toString(DefaultBciUser.getCurrentSession()));
                    RunNumberTextField.setText(Integer.toString(DefaultBciUser.getCurrentRun())+"/"+
                            DefaultBciUser.getNumberOfRunsPerSession());
                    TrialNumberTextField.setText(Integer.toString(DefaultBciUser.getCurrentTrial())+"/"+
                            DefaultBciUser.getNumberOfTrialsPerRun());                    
                    
                    StartScenarioButton.setEnabled(false);//disable button for as long trial running
                    LoadButton.setEnabled(false);
                    UserListButton.setEnabled(false);
                    BciPhaseComboBox.setEnabled(false);
                    Class1ImageryComboBox.setEnabled(false);
                    Class2ImageryComboBox.setEnabled(false);
                    RunsPerSessionSpinner.setEnabled(false);
                    TrialsPerRunSpinner.setEnabled(false);
                    SaveButton.setEnabled(false);
                    DeleteButton.setEnabled(false);
                    LoadButton.setEnabled(false);
                    RegisterButton.setEnabled(false);                    
                    ChannelSelectButton.setEnabled(false);
                    RunProgressBar.setValue(0);              
                    //load bci user to view
                    viewLoadBciUser(DefaultBciUser);
                    //Start Scenario
                    UdpSceneFrameReceptionThread = new Thread(new UdpSceneFrameReceptionRunnable());
                    UdpSceneFrameReceptionThread.start();
                            //Thread Simulink uDp reception
                    try{
                    ControlFrameSimulinkDatagramSocket = new DatagramSocket(UDP_LOCAL_PORT_FOR_SIMULINK);
                    }catch(SocketException ex){
                        System.out.println("Socket Alreay Binded!");
                    }
                        
                    UdpSimulinkReceptionThread = new Thread(UdpSimulinkReceptionRunnable1);
                    //udpSimulinkReceptionBuffer = new byte[SIMULINK_RECEPTION_BUFFER_SIZE];
                    UdpSimulinkReceptionThread.start();                   
                            //Start Scene                    
                    MainSceneFrame.startScenario(DefaultBciUser,DefaultBciUser.getBciPhaseState());
                } catch (SQLException ex) {
                    Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        }        
    }
    
    class StopScenarioButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
           if(DefaultBciUser.getUserId()!=0){//if there is an existing user
            //MainSceneFrame.stopScenario(DefaultBciUser.getBciPhaseState());//stop the current bci phase
            StartScenarioButton.setEnabled(true);//enable start button
            LoadButton.setEnabled(true);
            UserListButton.setEnabled(true);
            BciPhaseComboBox.setEnabled(true);            
            DeleteButton.setEnabled(true);
            LoadButton.setEnabled(true);
            RegisterButton.setEnabled(true);            
            //interrupt simulink recption thread            
            UdpSimulinkReceptionThread.interrupt();
            UdpSceneFrameReceptionThread.interrupt();
           }
        }
        
    }
    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        if(DefaultBciUser.getUserId()!=0){
            //limit value to above 1
           if((int)RunsPerSessionSpinner.getValue() >= 1){
            DefaultBciUser.setNumberOfRunsPerSession((int)RunsPerSessionSpinner.getValue());
           }
           else{
            DefaultBciUser.setNumberOfRunsPerSession(1);
            RunsPerSessionSpinner.setValue(1);
           }
           //limit value to above 1
           if((int)TrialsPerRunSpinner.getValue() >= 1){
            DefaultBciUser.setNumberOfTrialsPerRun((int)TrialsPerRunSpinner.getValue());
           }
           else{
            DefaultBciUser.setNumberOfTrialsPerRun(1);
            TrialsPerRunSpinner.setValue(1);
           }           
           
            //save in database
            SqlConnection SqlConnection1 = new SqlConnection();
            SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);                
            
            //update session settings             
            SqlConnection1.executeUpdate("UPDATE bci_session"
                    + " SET number_of_runs_per_session = "+DefaultBciUser.getNumberOfRunsPerSession()+
                    " , number_of_trials_per_run = "+DefaultBciUser.getNumberOfTrialsPerRun()+
                     " WHERE id = "+DefaultBciUser.getSessionId()+";");           
                                       
            SqlConnection1.disconnect();
            
           DefaultBciUser.setClass1Imagery(BciUser.getImageryPerIndex(Class1ImageryComboBox.getSelectedIndex()));
           DefaultBciUser.setClass2Imagery(BciUser.getImageryPerIndex(Class2ImageryComboBox.getSelectedIndex()));
           viewLoadBciUser(DefaultBciUser);//update view
        }
      }        
    }
    
    class BciPhaseComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {         
          if(DefaultBciUser.getUserId()!=0){
           DefaultBciUser.setBciPhaseState(BciUser.getPhasePerIndex(BciPhaseComboBox.getSelectedIndex()));
           DefaultBciUser=BciUser.loadSessionFromDb(DefaultBciUser,DefaultBciUser.getUserId(),
                   BciUser.getPhaseId(DefaultBciUser.getBciPhaseState()));/*
                 * load first user session with pointed phase information
                 */
           //allow change in configuration only when a new session comes in
                     boolean change = false;
                     if(DefaultBciUser.getCurrentRun()==1 && DefaultBciUser.getCurrentTrial()==1){
                         change=true;
                     }else{
                         change=false;
                     }
                     Class1ImageryComboBox.setEnabled(change);
                     Class2ImageryComboBox.setEnabled(change);
                     RunsPerSessionSpinner.setEnabled(change);
                     TrialsPerRunSpinner.setEnabled(change);
                     SaveButton.setEnabled(change);
                     ChannelSelectButton.setEnabled(change);
            viewLoadBciUser(DefaultBciUser);//loadto GUI
            RunProgressBar.setValue(0);//re-init to zero
          }
        }
    }
    
    class ChannelSelectButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            GraphicsEnvironment GraphicsEnvironment1 = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] GraphicsDevices = GraphicsEnvironment1.getScreenDevices();
     
          if(ChannelSelectorFrame == null){
            ChannelSelectorFrame = new EEGChannelSelectorFrame(DefaultBciUser);  
            try{//try push to second screen if existant
            ChannelSelectorFrame.setLocation(GraphicsDevices[1].getDefaultConfiguration().getBounds().x,
                    GraphicsDevices[1].getDefaultConfiguration().getBounds().y);
            }catch(Exception ex){
                Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            ChannelSelectorFrame.setVisible(true);
          }else{
           if(!ChannelSelectorFrame.isShowing()){
            ChannelSelectorFrame = new EEGChannelSelectorFrame(DefaultBciUser);
            try{//try push to second screen if existant
            ChannelSelectorFrame.setLocation(GraphicsDevices[1].getDefaultConfiguration().getBounds().x,
                    GraphicsDevices[1].getDefaultConfiguration().getBounds().y);
            }catch(Exception ex){
                 Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            ChannelSelectorFrame.setVisible(true);
           }
        }
        }        
    }
    
    class UdpSimulinkReceptionRunnable implements Runnable{
            @Override
            public void run() { // pause this code due to cpu demands              
                    byte [] tempBuff = new byte[264];//33 channels x 4bytes = 132 (x2 : prevention)
                    
                   while(!Thread.interrupted() && !ControlFrameSimulinkDatagramSocket.isClosed()/*UdpSimulinkReceptionThread.isInterrupted()*/){
                       try {
                            //listening for as long as thread not interrupted
                            ControlFrameSimulinkDatagramSocket.receive(SimulinkReceptionDatagramPacket);
                        } catch (IOException ex) {
                            Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                       
                     tempBuff = SimulinkReceptionDatagramPacket.getData();
                     int lengthReceived = SimulinkReceptionDatagramPacket.getLength();                                                           
                                         
                     for(int i = 0 ; i < lengthReceived;i++){
                         udpSimulinkReceptionBuffer[count++] = tempBuff[i];//save every data in main buffer
                     }  
                     //System.out.println("count:"+count);
                   } 
                       //System.out.println("COUNTA:"+count);
                      ControlFrameSimulinkDatagramSocket.close();//close socket
            }            
    }
    
    class UdpSceneFrameReceptionRunnable implements Runnable{//for end of scene information
        @Override
        public void run() {
            try {
                    //this runnable will read data and save them in the database
                    //wait for data
                   while(!UdpSceneFrameReceptionThread.isInterrupted()){//forever thread is not interrupted
                     
                     //ControlFrameSceneFrameDatagramSocket.receive(SceneFrameReceptionDatagramPacket);
                       
                     Socket ClientSocket  = ControlFrameTcpSocket.accept();
                     //InputStream ClientInputStream = ClientSocket.getInputStream();
                     //System.out.println("OUT : "+ ClientInputStream.read());
                     ClientSocket.close();
                     //ClientInputStream.close();
                            //udpSceneFrameReceptionBuffer = SceneFrameReceptionDatagramPacket.getData();                                                
                     //pause thread               
                     UdpSimulinkReceptionThread.interrupt();
                     //System.out.println("STOP UDP SIM !");
                     int simulinkBufferLength = count;                                          
                 
                     //long startTime = System.currentTimeMillis();
                     
                     //read data from buffer and load to an organized matrix
                        int intCount = 0;
                        int channelCount = 0;//0 to 32
                        int eegSample;
                        ArrayList<ArrayList<Integer>> EegSignalsFrameTemp = 
                                new ArrayList<ArrayList<Integer> >();
                        
                        for(int i = 0 ; i < 33;i++){
                              EegSignalsFrameTemp.add(new ArrayList<Integer>());
                        }
                        
                     for(int i = 0 ; i < simulinkBufferLength;i++){
                         intCount++;
                         if(intCount == 4){//an integer has been captured
                             intCount = 0;
                             //convert first to unsigned from two'2 complement(byte)
                             eegSample = (((udpSimulinkReceptionBuffer[i]>=0)?udpSimulinkReceptionBuffer[i]:(udpSimulinkReceptionBuffer[i]+256))<<24) + 
                                         (((udpSimulinkReceptionBuffer[i-1]>=0)?udpSimulinkReceptionBuffer[i-1]:(udpSimulinkReceptionBuffer[i-1]+256))<<16) +
                                         (((udpSimulinkReceptionBuffer[i-2]>=0)?udpSimulinkReceptionBuffer[i-2]:(udpSimulinkReceptionBuffer[i-2]+256))<<8)+
                                         ((udpSimulinkReceptionBuffer[i-3]>=0)?udpSimulinkReceptionBuffer[i-3]:(udpSimulinkReceptionBuffer[i-3]+256));                        
                             
                             EegSignalsFrameTemp.get(channelCount).add(eegSample);
                             channelCount++;
                             if(channelCount == 33){//we have 33 channels
                                 channelCount = 0;
                             }
                         }                         
                     }
                     
                     //SAVE TO DATABASE
                    SqlConnection SqlConnection1 = new SqlConnection();
                    SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access                        
                     
                    SqlConnection SqlConnection3 = new SqlConnection();
                    SqlConnection3.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access                
                    PreparedStatement EegSignalsPreparedStatement=null;         
                         
                     try {
                     SqlConnection3.getConn().setAutoCommit(false);
                     EegSignalsPreparedStatement = SqlConnection3.getConn().prepareStatement(eegSignalsQuery);
                      } 
                     catch (SQLException ex) {
                     Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                     } 
                         
                     boolean eegCaptured;                     
                     DefaultBciUser.setEegSignalsFrame(BciUser.getImageryFrame(EegSignalsFrameTemp));//get screening data from samples
                     
                     int dataSize=0;
                     
                     if(DefaultBciUser.getEegSignalsFrame()==null){
                         System.out.println("Recieved : NOTHING FOUND");                         
                         eegCaptured = false;//failed
                     }else{          
                         dataSize = DefaultBciUser.getEegSignalsFrame().get(0).size();
                         System.out.println("Recieved : FOUND -->SIZE : "+dataSize);                         
                         eegCaptured = true;//passed
                     }                               
                        //save pass or fail in database
                       SqlConnection1.executeUpdate("UPDATE bci_trial"
                                + " SET eeg_captured="+eegCaptured+" WHERE id="+DefaultBciUser.getTrialId()+";");                                                     
                       if(eegCaptured){//if trial passed
                              try{                                                       
                              for(int i = 0 ; i < dataSize;i++){                                  
                                  for(int j = 0 ; j < 32;j++){
                                      EegSignalsPreparedStatement.setInt(j+1,DefaultBciUser.getEegSignalsFrame().get(j).get(i));//get eeg channels                                      
                                  }
                                     EegSignalsPreparedStatement.setInt(33,DefaultBciUser.getTrialId());
                                     EegSignalsPreparedStatement.addBatch();                                     
                              }                             
                            }catch(Exception ex){
                                 Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                       }
                       
                        try {
                             int[] len = EegSignalsPreparedStatement.executeBatch();
                             SqlConnection3.getConn().commit();
                             //System.out.println("EXC : "+ len.length);
                             //System.out.println("done");
                             //long stopTime = System.currentTimeMillis();
                            //long elapsedTime = stopTime - startTime;
                            //System.out.println(elapsedTime + "ms");
                             EegSignalsPreparedStatement.close();
                             SqlConnection3.disconnect();
                     } catch (SQLException ex) {
                             Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                         SqlConnection1.disconnect();                     
                    
                     count = 0 ;//re-init to zero                     
                     //update progress
                     RunProgressBar.setValue(100*DefaultBciUser.getCurrentTrial()/DefaultBciUser.getNumberOfTrialsPerRun());
                     
                  if(DefaultBciUser.getCurrentTrial() == DefaultBciUser.getNumberOfTrialsPerRun()){//if number of trial is reached
                   
                    StartScenarioButton.setEnabled(true);//enable button after end of trial
                    LoadButton.setEnabled(true);
                    UserListButton.setEnabled(true);
                    BciPhaseComboBox.setEnabled(true);
               
                    DeleteButton.setEnabled(true);
                    LoadButton.setEnabled(true);
                    RegisterButton.setEnabled(true);
                    
                    if(DefaultBciUser.getCurrentRun() == DefaultBciUser.getNumberOfRunsPerSession()){
                        DefaultBciUser.setCurrentRun(1);
                        DefaultBciUser.setCurrentSession(DefaultBciUser.getCurrentSession()+1);
                        //SAVE TO DATABASE                        
                              SqlConnection SqlConnection2 = new SqlConnection();
                              SqlConnection2.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access                                                                     
                            
                              //create new session
                             Date DateNow = new Date();
                             SimpleDateFormat Ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                              SqlConnection2.executeUpdate("INSERT INTO bci_session(user_id,phase_id,number_of_trials_per_run"
                                      + ",number_of_runs_per_session,session_date)"
                            + " VALUES("+DefaultBciUser.getUserId()+","+
                                      BciUser.getPhaseId(DefaultBciUser.getBciPhaseState())+","+
                                      DefaultBciUser.getNumberOfTrialsPerRun()+","+DefaultBciUser.getNumberOfRunsPerSession()
                                      +",'"+Ft.format(DateNow)+"');");//create a new session with previous configuration                                                            
                              //get session id
                            ResultSet ResultSet1 = SqlConnection2.executeQuery("SELECT MAX(id) AS highest_session_id "
                               + "FROM bci_session;");                        
                            try {
                                ResultSet1.next();
                                int latestSessionId = ResultSet1.getInt("highest_session_id");
                                DefaultBciUser.setSessionId(latestSessionId);
                                ResultSet1.close();
                            } catch(SQLException ex) {
                                Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        
                              SqlConnection2.disconnect(); 
                                //ControlFrameSimulinkDatagramSocket.close();//close socket
                              //allow change in configuration only when a new session comes in
                                Class1ImageryComboBox.setEnabled(true);
                                Class2ImageryComboBox.setEnabled(true);
                                RunsPerSessionSpinner.setEnabled(true);
                                TrialsPerRunSpinner.setEnabled(true);
                                SaveButton.setEnabled(true);
                                ChannelSelectButton.setEnabled(true);
                    }else{
                       DefaultBciUser.setCurrentRun(DefaultBciUser.getCurrentRun()+1);
                    }                    
                        
                   }else{                   
                       DefaultBciUser.setCurrentTrial(DefaultBciUser.getCurrentTrial()+1);//move to next trial                    
                       
                       SqlConnection SqlConnection4 = new SqlConnection();
                       SqlConnection4.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);                        
                         //create trial                         
                       if(DefaultBciUser.getBciPhaseState().equals(BciPhaseEnum.SCREENING_PHASE) ){                       
                       DefaultBciUser.setCurrentImagery(DefaultBciUser.getImageryClassSequence().get(DefaultBciUser.getCurrentTrial()-1));//set current imagery from random list
                       SqlConnection4.executeUpdate("INSERT INTO bci_trial(run_id,imagery_type_id)"
                                    + " VALUES("+DefaultBciUser.getRunId()+","+
                                BciUser.getImageryTypeId(DefaultBciUser.getCurrentImagery())+");");    
                       }else if(DefaultBciUser.getBciPhaseState().equals(BciPhaseEnum.VERTICAL_1D_PHASE) ){                        
                       DefaultBciUser.setControl1DVerticalTargetNumber(DefaultBciUser.getTarget1DVerticalSequence().get(DefaultBciUser.getCurrentTrial()-1));
                       SqlConnection4.executeUpdate("INSERT INTO bci_trial(run_id,target_number)"
                                    + " VALUES("+DefaultBciUser.getRunId()+","+
                                DefaultBciUser.getControl1DVerticalTargetNumber()+");");
                       }else if(DefaultBciUser.getBciPhaseState().equals(BciPhaseEnum.HORIZONTAL_1D_PHASE) ){                       
                       DefaultBciUser.setControl1DHorizontalTargetNumber(DefaultBciUser.getTarget1DHorizontalSequence().get(DefaultBciUser.getCurrentTrial()-1));
                       SqlConnection4.executeUpdate("INSERT INTO bci_trial(run_id,target_number)"
                                    + " VALUES("+DefaultBciUser.getRunId()+","+
                                DefaultBciUser.getControl1DHorizontalTargetNumber()+");");
                       }else if(DefaultBciUser.getBciPhaseState().equals(BciPhaseEnum.CONTROL_2D_PHASE) ){                       
                       DefaultBciUser.setControl2DTargetNumber(DefaultBciUser.getTarget2DSequence().get(DefaultBciUser.getCurrentTrial()-1));//control 2d control
                       SqlConnection4.executeUpdate("INSERT INTO bci_trial(run_id,target_number)"
                                    + " VALUES("+DefaultBciUser.getRunId()+","+
                                DefaultBciUser.getControl2DTargetNumber()+");");
                       }
                       
                      //get trial id
                       ResultSet ResultSet1 = SqlConnection4.executeQuery("SELECT MAX(id) AS latest_trial_id FROM bci_trial;");                       
                       int trialId=0;            
                         try {                   
                             ResultSet1.next();
                             trialId = ResultSet1.getInt("latest_trial_id");
                         } catch (SQLException ex) {
                             Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE, null, ex);
                         }                         
                       DefaultBciUser.setTrialId(trialId);                       
                       SqlConnection4.disconnect();
                    
                    //update view
                    TrialNumberTextField.setText(Integer.toString(DefaultBciUser.getCurrentTrial())+"/"+
                    DefaultBciUser.getNumberOfTrialsPerRun());
                    
                    //Thread Simulink uDp reception
                        //udpSimulinkReceptionBuffer = new byte[SIMULINK_RECEPTION_BUFFER_SIZE];
                        UdpSceneFrameReceptionThread = new Thread(new UdpSceneFrameReceptionRunnable());
                        UdpSceneFrameReceptionThread.start();
                        try{
                        ControlFrameSimulinkDatagramSocket = new DatagramSocket(UDP_LOCAL_PORT_FOR_SIMULINK);
                        }catch(Exception ex){
                        //UdpSimulinkReceptionThread.interrupt();
                        //ControlFrameSimulinkDatagramSocket.close();                        
                        //ControlFrameSimulinkDatagramSocket = new DatagramSocket(UDP_LOCAL_PORT_FOR_SIMULINK);
                            System.out.println("Socket Bound Already!");
                        }
                        UdpSimulinkReceptionThread = new Thread(UdpSimulinkReceptionRunnable1);
                        UdpSimulinkReceptionThread.start();                                          
                         
                        MainSceneFrame.startScenario(DefaultBciUser,DefaultBciUser.getBciPhaseState());                                            
                    }                            
                   }
                     
                } catch (IOException ex) {
                    Logger.getLogger(SceneFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
    }
    
    class DatabaseButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            
        GraphicsEnvironment GraphicsEnvironment1 = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] GraphicsDevices = GraphicsEnvironment1.getScreenDevices();
     
          if(DatabaseFrame == null){
            DatabaseFrame = new EEGDatabaseFrame();  
            try{//try push to second screen if existant
            DatabaseFrame.setLocation(GraphicsDevices[1].getDefaultConfiguration().getBounds().x,
                    GraphicsDevices[1].getDefaultConfiguration().getBounds().y);
            }catch(Exception ex){
                Logger.getLogger(SceneFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            DatabaseFrame.setVisible(true);
          }else{
           if(!DatabaseFrame.isShowing()){
            DatabaseFrame = new EEGDatabaseFrame();
            try{//try push to second screen if existant
            DatabaseFrame.setLocation(GraphicsDevices[1].getDefaultConfiguration().getBounds().x,
                    GraphicsDevices[1].getDefaultConfiguration().getBounds().y);
            }catch(Exception ex){
                Logger.getLogger(SceneFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            DatabaseFrame.setVisible(true);
           }
        }
        
    }
  }
    
    public BciUser getBciUser(){
         return DefaultBciUser;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        BciPhaseComboBox = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        InitialsEditText = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        ProfileImage = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BciUserList = new javax.swing.JList();
        DeleteButton = new javax.swing.JButton();
        RegisterButton = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        StartScenarioButton = new javax.swing.JButton();
        DoneButton = new javax.swing.JButton();
        TrialNumberTextField = new javax.swing.JTextField();
        SessionNumberTextField = new javax.swing.JTextField();
        RunNumberTextField = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        SurnameEditText = new javax.swing.JTextField();
        GenderEditText = new javax.swing.JTextField();
        RunProgressBar = new javax.swing.JProgressBar();
        jTextField17 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        AgeEditText = new javax.swing.JTextField();
        LoadButton = new javax.swing.JButton();
        UserListButton = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        TrialsPerRunSpinner = new javax.swing.JSpinner();
        RunsPerSessionSpinner = new javax.swing.JSpinner();
        StopScenarioButton = new javax.swing.JButton();
        Class1ImageryComboBox = new javax.swing.JComboBox();
        Class2ImageryComboBox = new javax.swing.JComboBox();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        ChannelSelectButton = new javax.swing.JButton();
        DatabaseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Virtual BCI Control Frame");
        setBackground(new java.awt.Color(0, 0, 0));
        setPreferredSize(new java.awt.Dimension(780, 698));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(51, 153, 0));
        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("BCI Phase");
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 345, 30));

        BciPhaseComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        BciPhaseComboBox.setToolTipText("");
        getContentPane().add(BciPhaseComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 345, 30));

        jTextField2.setEditable(false);
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Class 2 Imagery");
        getContentPane().add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 460, 120, -1));

        jTextField3.setEditable(false);
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("Trials Per Run");
        getContentPane().add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 370, 100, -1));

        jTextField4.setEditable(false);
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setText("Trial Number");
        getContentPane().add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, 100, -1));

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(51, 153, 0));
        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("User Profile");
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 345, 30));

        InitialsEditText.setEditable(false);
        getContentPane().add(InitialsEditText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 192, -1));

        jTextField7.setEditable(false);
        jTextField7.setText("Initials");
        getContentPane().add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 192, -1));

        jTextField8.setEditable(false);
        jTextField8.setText("Gender");
        getContentPane().add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 170, 90, -1));

        ProfileImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ProfileImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/unknown_profile.jpg"))); // NOI18N
        ProfileImage.setText("jLabel1");
        getContentPane().add(ProfileImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 140, 145));

        BciUserList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        BciUserList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(BciUserList);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 345, 270));

        DeleteButton.setText("Delete From List");
        getContentPane().add(DeleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 570, 120, 33));

        RegisterButton.setText("Register");
        getContentPane().add(RegisterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 570, 80, 33));

        SaveButton.setBackground(new java.awt.Color(204, 0, 51));
        SaveButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        SaveButton.setText("Save");
        getContentPane().add(SaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 560, 115, 37));

        StartScenarioButton.setBackground(new java.awt.Color(51, 102, 0));
        StartScenarioButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        StartScenarioButton.setText("Start");
        getContentPane().add(StartScenarioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 200, 115, 37));

        DoneButton.setText("Done");
        DoneButton.setEnabled(false);
        getContentPane().add(DoneButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 120, 30));

        TrialNumberTextField.setEditable(false);
        TrialNumberTextField.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        TrialNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TrialNumberTextField.setText("0/40");
        getContentPane().add(TrialNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 140, 90, 40));

        SessionNumberTextField.setEditable(false);
        SessionNumberTextField.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        SessionNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        SessionNumberTextField.setText("0");
        getContentPane().add(SessionNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 140, 60, 40));

        RunNumberTextField.setEditable(false);
        RunNumberTextField.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        RunNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        RunNumberTextField.setText("0/4");
        getContentPane().add(RunNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, 60, 40));

        jTextField14.setEditable(false);
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField14.setText("Run Progress");
        getContentPane().add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 270, 350, 30));

        SurnameEditText.setEditable(false);
        getContentPane().add(SurnameEditText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 192, -1));

        GenderEditText.setEditable(false);
        getContentPane().add(GenderEditText, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, 90, -1));
        getContentPane().add(RunProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 310, 350, 40));

        jTextField17.setEditable(false);
        jTextField17.setText("Surname");
        getContentPane().add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 192, -1));

        jTextField10.setEditable(false);
        jTextField10.setText("Age");
        getContentPane().add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 90, -1));

        AgeEditText.setEditable(false);
        getContentPane().add(AgeEditText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 90, -1));

        LoadButton.setText("Load");
        getContentPane().add(LoadButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 90, 33));

        UserListButton.setBackground(new java.awt.Color(51, 153, 0));
        UserListButton.setText("User List");
        getContentPane().add(UserListButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 340, 40));

        jTextField6.setEditable(false);
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setText("Session Number");
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 100, 93, -1));

        jTextField9.setEditable(false);
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setText("Run Number");
        getContentPane().add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 100, 100, -1));

        TrialsPerRunSpinner.setValue(40);
        getContentPane().add(TrialsPerRunSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 410, 40, 30));

        RunsPerSessionSpinner.setValue(4);
        getContentPane().add(RunsPerSessionSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 410, 40, 30));

        StopScenarioButton.setBackground(new java.awt.Color(204, 0, 51));
        StopScenarioButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        StopScenarioButton.setText("Stop");
        getContentPane().add(StopScenarioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 200, 115, 37));

        Class1ImageryComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Left Hand", "Right Hand", "Left Foot", "Right Foot", "Rest" }));
        getContentPane().add(Class1ImageryComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 500, 130, -1));

        Class2ImageryComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Left Hand", "Right Hand", "Left Foot", "Right Foot", "Rest" }));
        getContentPane().add(Class2ImageryComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 500, 130, -1));

        jTextField15.setEditable(false);
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField15.setText("Run per Session");
        getContentPane().add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 370, 93, -1));

        jTextField16.setEditable(false);
        jTextField16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField16.setText("Class 1 Imagery");
        getContentPane().add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 460, 130, -1));

        ChannelSelectButton.setText("Channel Select");
        getContentPane().add(ChannelSelectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 410, 110, 40));

        DatabaseButton.setText("Database");
        getContentPane().add(DatabaseButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(653, 610, 100, 40));

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
            java.util.logging.Logger.getLogger(ControlFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControlFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControlFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControlFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JTextField AgeEditText;
    protected javax.swing.JComboBox BciPhaseComboBox;
    protected javax.swing.JList BciUserList;
    protected javax.swing.JButton ChannelSelectButton;
    private javax.swing.JComboBox Class1ImageryComboBox;
    private javax.swing.JComboBox Class2ImageryComboBox;
    protected javax.swing.JButton DatabaseButton;
    protected javax.swing.JButton DeleteButton;
    protected javax.swing.JButton DoneButton;
    protected javax.swing.JTextField GenderEditText;
    protected javax.swing.JTextField InitialsEditText;
    protected javax.swing.JButton LoadButton;
    protected javax.swing.JLabel ProfileImage;
    protected javax.swing.JButton RegisterButton;
    private javax.swing.JTextField RunNumberTextField;
    protected javax.swing.JProgressBar RunProgressBar;
    private javax.swing.JSpinner RunsPerSessionSpinner;
    private javax.swing.JButton SaveButton;
    private javax.swing.JTextField SessionNumberTextField;
    protected javax.swing.JButton StartScenarioButton;
    private javax.swing.JButton StopScenarioButton;
    protected javax.swing.JTextField SurnameEditText;
    private javax.swing.JTextField TrialNumberTextField;
    private javax.swing.JSpinner TrialsPerRunSpinner;
    protected javax.swing.JButton UserListButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
