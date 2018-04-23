/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_bci_platform;

import com.user.bci.BciUser;
import com.user.sql.SqlConnection;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Yves Matanga
 */
public class EEGDatabaseFrame extends javax.swing.JFrame {

    /**
     * Creates new form EEGDatabaseFrame
     */
    //database connection details
    String RDBMS = "mysql";
    String DB_NAME = "mtech_research";
    String SQL_USER_NAME = "root";
    String SQL_USER_PASSWORD = "root";
    //model
    final String[] ImageryClasses = new String[]{"LH","RH","LF","RF","R"};
    
    //views
    protected DefaultListModel BciUserListModel = null;
    protected BciUser DefaultBciUser;
    
    protected TableModel TableModel1;
    protected TableModel TableModel2;
    
    
    JComboBox[] PhasesComboBox;
    JButton[] ExportSessionButtons;
    JButton[] ExportPhaseButtons;
    
    //Table Headers
    int PHASES_NUMBER = 5;
    
    public EEGDatabaseFrame() {
        initComponents();
        userInitComponents();
    }
    
    private void userInitComponents(){       
        viewLoadBciUser(1);//load first element from bci user list in dtabas        
        //Comboboxes listeners
        for(int i = 0 ; i < PHASES_NUMBER ; i++){
            PhasesComboBox[i].addItemListener(new PhaseComboBoxItemListener());
            ExportSessionButtons[i].addActionListener(new ExportButtonListener());
            ExportPhaseButtons[i].addActionListener(new ExportButtonListener());
        }
            ExportUserButton.addActionListener(new ExportButtonListener());
        //BciUserList Listener
            BciUserList.addListSelectionListener(new UserListListener());
     }
    
    public void viewLoadBciUser(int position){
         try {            
            //update view
                PhasesComboBox = new JComboBox[5];
                ExportSessionButtons = new JButton[5];
                ExportPhaseButtons = new JButton[5];
                
             for(int i = 0 ; i < PHASES_NUMBER; i++){
                 PhasesComboBox[i] = new JComboBox();
                 ExportSessionButtons[i] = new JButton("Export Session");
                 ExportPhaseButtons[i] = new JButton("Export Phase");
                 //PhasesComboBox[i].setBounds(390 + 120*i,160,100,5);
                 PhasesComboBox[i].setModel(new DefaultComboBoxModel(new String[]{"1","2"}));
                 getContentPane().add(PhasesComboBox[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(390 + 120*i, 265, 100, 30));
                 getContentPane().add(ExportSessionButtons[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(390 + 120*i, 550, 100, 30));
                 getContentPane().add(ExportPhaseButtons[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(390 + 120*i, 595, 100, 30));
                 
             }
            //update BCI USer List
            
            TableModel1 =  UserPanelTable1.getModel();           
            TableModel2 = UserPanelTable2.getModel();
            
            SqlConnection SqlConnection1 = new SqlConnection();
            SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access                                        
            
            ResultSet ResultSet1 = SqlConnection1.executeQuery("SELECT * FROM bci_users;");
            
            DefaultBciUser = new BciUser();//initial default bci user
            BciUserListModel = new DefaultListModel();
                    
                        //select the first element
                          while(ResultSet1.next()){
                            String surname = ResultSet1.getString("surname");
                            String initials = ResultSet1.getString("initials");
                            BciUserListModel.addElement(surname + " " + initials);//add user from the database to the list  
                                      
                            if(ResultSet1.getRow() == position){
                                DefaultBciUser.setUserId(ResultSet1.getInt("id"));
                                DefaultBciUser.setSurname(surname);
                                DefaultBciUser.setInitials(initials);
                                DefaultBciUser.setAge(ResultSet1.getInt("age"));
                                DefaultBciUser.setGender(ResultSet1.getString("gender"));
                                DefaultBciUser.setPath(ResultSet1.getString("image_path").replace('-','\\'));/*put back \ character removed in sql*/                            
                            }
                        }
            ResultSet1.close();
                          
            SqlConnection1.disconnect();
            BciUserList.setModel(BciUserListModel);//bind to jlist                
            BciUserList.setSelectedIndex(0);//select first element 
            
            //DefaultBciUser=BciUser.loadSessionFromDb(DefaultBciUser,DefaultBciUser.getUserId(),1);//get screening phase
            viewLoadProfile(DefaultBciUser);
            viewLoadDatabaseInfo(DefaultBciUser);
            
        } catch (SQLException ex) {
            Logger.getLogger(EEGDatabaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    protected void viewLoadProfile(BciUser BciUser1){
        this.SurnameEditText.setText(BciUser1.getSurname());
        this.InitialsEditText.setText(BciUser1.getInitials());
        this.AgeEditText.setText(Integer.toString(BciUser1.getAge()));
        this.GenderEditText.setText(BciUser1.getGender());
        
        //Image~Picture Load
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
            Ex.printStackTrace();
        }
    }
    
    protected void viewLoadDatabaseInfo(BciUser BciUser1){
        SqlConnection SqlConnection1 = new SqlConnection();
        SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access                                        
        
        ResultSet ResultSet1=null;
        for(int i = 0 ; i < PHASES_NUMBER;i++){
            try {
                //get number of sessions
                ResultSet1 = SqlConnection1.executeQuery("SELECT COUNT(id) AS nbOfSessions FROM bci_session"
                        + " WHERE phase_id = "+(i+1)+ " AND user_id = "+ BciUser1.getUserId()+";");
                ResultSet1.next();
                int nbOfSessions = ResultSet1.getInt("nbOfSessions");
                TableModel1.setValueAt(String.valueOf(nbOfSessions),1,i);
                PhasesComboBox[i].removeAllItems();
                
                //load sessions numbers
                for(int j = 0 ; j < nbOfSessions;j++ ){
                    PhasesComboBox[i].addItem(String.valueOf(j+1));
                }
                //get total number of classes in bci phase
                String Classes="";
                
                if( (i!=1) && (i!=2) && (i!=3) ){//!= from 2d control ,1d vertical & horizontal (phase)
                    ResultSet1 = SqlConnection1.executeQuery("SELECT MIN(bci_trial.id),imagery_type_id FROM bci_trial INNER JOIN "
                        + " bci_run ON bci_trial.run_id = bci_run.id "
                            + "INNER JOIN bci_session ON bci_run.session_id = bci_session.id WHERE phase_id = "+(i+1)+ " AND user_id = "+ BciUser1.getUserId()+
                        " GROUP BY imagery_type_id;");

                    while(ResultSet1.next()){
                      Classes+= ","+ImageryClasses[ResultSet1.getInt("imagery_type_id")-1];//get symbol to cut short the imagery claass id to symbol(string) 
                    }
                }else{
                    ResultSet1 = SqlConnection1.executeQuery("SELECT MIN(bci_trial.id),target_number FROM bci_trial INNER JOIN "
                        + " bci_run ON bci_trial.run_id = bci_run.id "
                            + "INNER JOIN bci_session ON bci_run.session_id = bci_session.id WHERE phase_id = "+(i+1)+ " AND user_id = "+ BciUser1.getUserId()+
                        " GROUP BY target_number;");

                    while(ResultSet1.next()){
                      Classes+= ","+ResultSet1.getString("target_number");//load target number's 
                    }
                }
                Classes = Classes.replaceFirst(","," ");
                TableModel1.setValueAt(Classes,3,i);
                
                //get number of trialsoverall
                ResultSet1 = SqlConnection1.executeQuery("SELECT COUNT(bci_trial.id) AS nbOfTrials"
                        + " FROM bci_trial INNER JOIN bci_run ON"
                        + " bci_trial.run_id = bci_run.id INNER JOIN bci_session ON"
                        + " bci_run.session_id = bci_session.id "
                        + " WHERE phase_id = "+(i+1)+ " AND user_id = "+ BciUser1.getUserId()+";");
                ResultSet1.next();
                TableModel1.setValueAt(ResultSet1.getString("nbOfTrials"),5,i);                
                //
                if(nbOfSessions > 0 )
                viewLoadSessionInfo(BciUser1.getUserId(),(i+1),1);
                
                UserPanelTable1.setModel(TableModel1);                
            } catch (SQLException ex) {
                Logger.getLogger(EEGDatabaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            }        
        }
        try {
            ResultSet1.close();
        } catch (SQLException ex) {
            Logger.getLogger(EEGDatabaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        SqlConnection1.disconnect();
        
    }
    public void viewLoadSessionInfo(int userId,int phaseId,int sessionNbr){
        try {
            SqlConnection SqlConnection1 = new SqlConnection();
            SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);
            
            int sessionId = getSessionId(phaseId,userId,sessionNbr);   
            
            //get total number of classes in session             
            String TargetNumber ="";    
            String Imagery="";
            ResultSet  ResultSet1;
            
            if( (phaseId != 2) && (phaseId != 3) && (phaseId !=4) ){//in case of screening phase and special classificatui          
            ResultSet1 = SqlConnection1.executeQuery("SELECT MIN(bci_trial.id),imagery_type_id FROM bci_trial INNER JOIN "
                    + " bci_run ON bci_trial.run_id = bci_run.id WHERE session_id ="+ sessionId+" "+
                    "GROUP BY imagery_type_id;");
            
            while(ResultSet1.next()){
                  Imagery+= ","+ImageryClasses[ResultSet1.getInt("imagery_type_id")-1];//get symbol to cut short the imagery claass id to symbol(string)                   
            }
            
            Imagery = Imagery.replaceFirst(","," ");     
            TableModel2.setValueAt(Imagery,0,phaseId-1);
            }else {
            ResultSet1 = SqlConnection1.executeQuery("SELECT MIN(bci_trial.id),target_number FROM bci_trial INNER JOIN "
                    + " bci_run ON bci_trial.run_id = bci_run.id WHERE session_id ="+ sessionId+" "+
                    "GROUP BY target_number;");
            
            while(ResultSet1.next()){                  
                  TargetNumber +=","+ResultSet1.getString("target_number");
            }
              TargetNumber = TargetNumber.replaceFirst(","," ");
              TableModel2.setValueAt(TargetNumber,0,phaseId-1);
            }
            
            //get total number of trials in session
            ResultSet1 = SqlConnection1.executeQuery("SELECT COUNT(bci_trial.id) AS nbOfTrials"
                            + " FROM bci_trial INNER JOIN bci_run ON"
                            + " bci_trial.run_id = bci_run.id INNER JOIN bci_session ON"
                            + " bci_run.session_id = bci_session.id "
                            + " WHERE session_id="+sessionId+";");
            ResultSet1.next();
            int nbOfTrials = ResultSet1.getInt("nbOfTrials");
            TableModel2.setValueAt(String.valueOf(nbOfTrials),2,phaseId-1);
            ResultSet1 = SqlConnection1.executeQuery("SELECT COUNT(bci_run.id) AS nbOfRuns"
                            + " FROM bci_run "
                            + " INNER JOIN bci_session ON"
                            + " bci_run.session_id = bci_session.id "
                            + " WHERE session_id="+sessionId+";");
            ResultSet1.next();
            String nbOfRuns = ResultSet1.getString("nbOfRuns");
            
            ResultSet1 = SqlConnection1.executeQuery("SELECT number_of_runs_per_session"
                            + " FROM bci_session "                        
                            + " WHERE id="+sessionId+";");
            ResultSet1.next();
            String nbOfRunsPerSession = ResultSet1.getString("number_of_runs_per_session");
            
            TableModel2.setValueAt(nbOfRuns+"/"+
                    nbOfRunsPerSession,4,phaseId-1);           
            
            ResultSet1 = SqlConnection1.executeQuery("SELECT number_of_trials_per_run"
                            + " FROM bci_session "                        
                            + " WHERE id="+sessionId+";");
            ResultSet1.next();
            String nbOfTrialsPerRun = ResultSet1.getString("number_of_trials_per_run");
            
            TableModel2.setValueAt(nbOfTrialsPerRun,6,phaseId-1);
            
            UserPanelTable2.setModel(TableModel2);
            SqlConnection1.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(EEGDatabaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    class PhaseComboBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            
            int sessionNbr = Integer.parseInt((String)e.getItem());
           for(int i = 0 ; i < PHASES_NUMBER;i++){
                if(e.getSource().equals(PhasesComboBox[i])){
                    if(sessionNbr > 0){
                        viewLoadSessionInfo(DefaultBciUser.getUserId(),(i+1),sessionNbr);
                    }
                }  
           }              
        }        
    }
    
    class UserListListener implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {
            try {
                String temp = (String) BciUserList.getSelectedValue();//made of Surname + initials
                String array[] = temp.split(" ");            
                String surname = array[0];
                String initials = array[1];
                
                SqlConnection SqlConnection1 = new SqlConnection();
                
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
                       
                       //DefaultBciUser=BciUser.loadSessionFromDb(DefaultBciUser,DefaultBciUser.getUserId(),1);/*
                        /* 
                        * }
                        */
                       viewLoadProfile(DefaultBciUser);
                       viewLoadDatabaseInfo(DefaultBciUser);
                }
            } catch (SQLException ex) {
                Logger.getLogger(EEGDatabaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    class ExportButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int phaseId;
            int sessionNbr;
            boolean breakLoop = false;
            for(int i = 0 ; (i < PHASES_NUMBER) /*&& !breakLoop*/;i++){
                if(e.getSource().equals(ExportSessionButtons[i]) ||
                        e.getSource().equals(ExportPhaseButtons[i]) ||
                        e.getSource().equals(ExportUserButton)){
                    phaseId = i+1;//get phase id
                                        
                    sessionNbr = Integer.parseInt((String)PhasesComboBox[i].
                            getSelectedItem()!=null?(String)PhasesComboBox[i].
                            getSelectedItem():"0");//get session nbr
                    
                    if(sessionNbr>0){//not null
                        int sessionId = getSessionId(phaseId,DefaultBciUser.getUserId()
                                ,sessionNbr);
                        //set file name and location
                        JFileChooser FileChooser = new JFileChooser();
                        int retrieval = FileChooser.showSaveDialog(null);
                       if(retrieval == JFileChooser.APPROVE_OPTION){//if process approved
                          System.out.println(FileChooser.getSelectedFile().getName());             
                          String Path = FileChooser.getSelectedFile().getPath();//.getAbsolutePath();
                          String PathCoordinates = Path.substring(0,Path.length()-4) + "Coordinates.csv";
                          //System.out.println(Path.replace('\\','/'));
                          //Path = "/tmp/eeg4.csv";
                          SqlConnection SqlConnection1 = new SqlConnection();
                          SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);
                          
                         String query;
                         String TrialsCoordinatesQuery="";
                         String ClassesQuery="";
                         String ClassesHeader="";
                         
                         if((i!=1) && (i!=2) && (i!=3)){//if phase is not vertical ,control 2d or horinzontal 1d
                             ClassesQuery = "imagery_type.id as mental_task";
                             ClassesHeader = "'\"mental_task_id\"'";
                         }else{
                             ClassesQuery = "target_number as target_number";
                             ClassesHeader = "'\"target_number\"'";
                         }
                         
                         if(e.getSource().equals(ExportSessionButtons[i])) {//if session button                        
                            query = "SELECT '\"fp1\"','\"fp2\"','\"af3\"','\"af4\"','\"f7\"','\"f3\"','\"fz\"','\"f4\"','\"f8\"','\"fc5\"','\"fc1\"','\"fc2\"','\"fc6\"','\"t7\"','\"c3\"','\"" +
                            "cz\"','\"c4\"','\"t8\"','\"cp5\"','\"cp1\"','\"cp2\"','\"cp6\"','\"p7\"','\"p3\"','\"pz\"','\"p4\"','\"p8\"','\"po7\"','\"po3\"','\"po4\"','\"po8\"','\"oz\"',"+ClassesHeader+",'\""
                             + "trial_id\"','\"run_id \"','\"phase_id\"'"+
                            " UNION ALL "
                                    + "SELECT fp1,fp2,af3,af4,f7,f3,fz,f4,f8,fc5,fc1,fc2,fc6,t7,c3," +
                            "cz,c4,t8,cp5,cp1,cp2,cp6,p7,p3,pz,p4,p8,po7,po3,po4,po8,oz,"+ClassesQuery+","
                             + "trial_id AS trial_number,run_id AS run_number,phase_id "+" FROM eeg_signals " +
                            "INNER JOIN bci_trial ON" +
                            " eeg_signals.trial_id = bci_trial.id " +
                            " INNER JOIN imagery_type ON" +
                            " bci_trial.imagery_type_id = imagery_type.id" +
                            " INNER JOIN bci_run ON" +
                            " bci_trial.run_id = bci_run.id" +
                            " INNER JOIN bci_session ON" +
                            " bci_run.session_id = bci_session.id" +
                            " INNER JOIN bci_users ON" +
                            " bci_session.user_id=bci_users.id " +
                            " WHERE session_id="+sessionId+" INTO OUTFILE '"+Path+"' "
                                  + "FIELDS OPTIONALLY ENCLOSED BY '' TERMINATED BY ',' ESCAPED BY '\"'"+
                                  " LINES TERMINATED BY '\\n';";
                            
                            TrialsCoordinatesQuery= "SELECT '\"dx\"','\"dy\"','\"trial_id\"','\"run_id\"'" +
                            " UNION ALL " +
                            "SELECT coord_x,coord_y,trial_id,run_id FROM trial_coordinates  " +
                            "INNER JOIN bci_trial ON" +
                            " trial_coordinates.trial_id = bci_trial.id " +
                            " INNER JOIN imagery_type ON" +
                            " bci_trial.imagery_type_id = imagery_type.id" +
                            " INNER JOIN bci_run ON" +
                            " bci_trial.run_id = bci_run.id" +
                            " INNER JOIN bci_session ON" +
                            " bci_run.session_id = bci_session.id" +
                            " INNER JOIN bci_users ON" +
                            " bci_session.user_id=bci_users.id " +
                            " WHERE session_id="+sessionId+" INTO OUTFILE '"+PathCoordinates+"' "
                                  + "FIELDS OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' ESCAPED BY '\"'"+
                                  " LINES TERMINATED BY '\\n';";
                         }else if(e.getSource().equals(ExportPhaseButtons[i])){//else phase button
                          query = "SELECT '\"fp1\"','\"fp2\"','\"af3\"','\"af4\"','\"f7\"','\"f3\"','\"fz\"','\"f4\"','\"f8\"','\"fc5\"','\"fc1\"','\"fc2\"','\"fc6\"','\"t7\"','\"c3\"','\"" +
                            "cz\"','\"c4\"','\"t8\"','\"cp5\"','\"cp1\"','\"cp2\"','\"cp6\"','\"p7\"','\"p3\"','\"pz\"','\"p4\"','\"p8\"','\"po7\"','\"po3\"','\"po4\"','\"po8\"','\"oz\"',"+ClassesHeader+",'\""
                             + "trial_id\"','\"run_id \"','\"session_id\"'"+
                            " UNION ALL "+                                  
                                  "SELECT fp1,fp2,af3,af4,f7,f3,fz,f4,f8,fc5,fc1,fc2,fc6,t7,c3," +
                            "cz,c4,t8,cp5,cp1,cp2,cp6,p7,p3,pz,p4,p8,po7,po3,po4,po8,oz,"+ClassesQuery+","
                             + "trial_id AS trial_number,run_id AS run_number,session_id AS session_number "+" FROM eeg_signals " +
                            "INNER JOIN bci_trial ON" +
                            " eeg_signals.trial_id = bci_trial.id " +
                            " INNER JOIN imagery_type ON" +
                            " bci_trial.imagery_type_id = imagery_type.id" +
                            " INNER JOIN bci_run ON" +
                            " bci_trial.run_id = bci_run.id" +
                            " INNER JOIN bci_session ON" +
                            " bci_run.session_id = bci_session.id" +
                            " INNER JOIN bci_users ON" +
                            " bci_session.user_id=bci_users.id " +
                            " WHERE bci_session.phase_id="+phaseId+" INTO OUTFILE '"+Path+"' "
                                  + "FIELDS OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' ESCAPED BY '\"'"+
                                  " LINES TERMINATED BY '\\n';";  
                          
                           TrialsCoordinatesQuery= "SELECT '\"dx\"','\"dy\"','\"trial_id\"','\"run_id \"','\"session_id\"'" +
                            " UNION ALL " +
                            "SELECT coord_x,coord_y,trial_id,run_id,session_id FROM trial_coordinates  " +
                            "INNER JOIN bci_trial ON" +
                            " trial_coordinates.trial_id = bci_trial.id " +
                            " INNER JOIN imagery_type ON" +
                            " bci_trial.imagery_type_id = imagery_type.id" +
                            " INNER JOIN bci_run ON" +
                            " bci_trial.run_id = bci_run.id" +
                            " INNER JOIN bci_session ON" +
                            " bci_run.session_id = bci_session.id" +
                            " INNER JOIN bci_users ON" +
                            " bci_session.user_id=bci_users.id " +
                            " WHERE bci_session.phase_id="+phaseId+" INTO OUTFILE '"+PathCoordinates+"' "
                                  + "FIELDS OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' ESCAPED BY '\"'"+
                                  " LINES TERMINATED BY '\\n';"; 
                         }else{//will export coordiantes of phase that have coordinates only
                            query = "SELECT '\"fp1\"','\"fp2\"','\"af3\"','\"af4\"','\"f7\"','\"f3\"','\"fz\"','\"f4\"','\"f8\"','\"fc5\"','\"fc1\"','\"fc2\"','\"fc6\"','\"t7\"','\"c3\"','\"" +
                            "cz\"','\"c4\"','\"t8\"','\"cp5\"','\"cp1\"','\"cp2\"','\"cp6\"','\"p7\"','\"p3\"','\"pz\"','\"p4\"','\"p8\"','\"po7\"','\"po3\"','\"po4\"','\"po8\"','\"oz\"',"
                                    + "'\"mental_task\"','\"target_number\"','\""
                             + "trial_id\"','\"run_id \"','\"session_id\"','\"phase_id\"'"+
                             " UNION ALL "+                                    
                                    "SELECT fp1,fp2,af3,af4,f7,f3,fz,f4,f8,fc5,fc1,fc2,fc6,t7,c3," +
                            "cz,c4,t8,cp5,cp1,cp2,cp6,p7,p3,pz,p4,p8,po7,po3,po4,po8,oz,imagery_type.id,target_number,"
                             + "trial_id AS trial_number,run_id AS run_number,session_id AS session_number,phase_id AS phase_id "+" FROM eeg_signals " +
                            "INNER JOIN bci_trial ON" +
                            " eeg_signals.trial_id = bci_trial.id " +
                            " INNER JOIN imagery_type ON" +
                            " bci_trial.imagery_type_id = imagery_type.id" +
                            " INNER JOIN bci_run ON" +
                            " bci_trial.run_id = bci_run.id" +
                            " INNER JOIN bci_session ON" +
                            " bci_run.session_id = bci_session.id" +
                            " INNER JOIN bci_users ON" +
                            " bci_session.user_id=bci_users.id " +
                            " WHERE user_id="+DefaultBciUser.getUserId()+" INTO OUTFILE '"+Path+"' "
                                  + "FIELDS OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' ESCAPED BY '\"'"+
                                  " LINES TERMINATED BY '\\n';";  
                            
                            TrialsCoordinatesQuery= "SELECT '\"dx\"','\"dy\"','\"trial_id\"','\"run_id \"','\"session_id\"','\"phase_id\"'" +
                            " UNION ALL " +
                            "SELECT coord_x,coord_y,trial_id,run_id,session_id,phase_id FROM trial_coordinates  " +
                            "INNER JOIN bci_trial ON" +
                            " trial_coordinates.trial_id = bci_trial.id " +
                            " INNER JOIN imagery_type ON" +
                            " bci_trial.imagery_type_id = imagery_type.id" +
                            " INNER JOIN bci_run ON" +
                            " bci_trial.run_id = bci_run.id" +
                            " INNER JOIN bci_session ON" +
                            " bci_run.session_id = bci_session.id" +
                            " INNER JOIN bci_users ON" +
                            " bci_session.user_id=bci_users.id " +
                            " WHERE user_id="+DefaultBciUser.getUserId()+" INTO OUTFILE '"+PathCoordinates+"' "
                                  + "FIELDS OPTIONALLY ENCLOSED BY '\"' TERMINATED BY ',' ESCAPED BY '\"'"+
                                  " LINES TERMINATED BY '\\n';";  
                            System.out.println(1);
                         }
                          System.out.println(query);
                          
                          SqlConnection1.executeQuery(query); 
                          
                          if((!e.getSource().equals(ExportSessionButtons[0])) &&   
                                  !e.getSource().equals(ExportPhaseButtons[0])){//do not execute it for screening phase
                          SqlConnection1.executeQuery(TrialsCoordinatesQuery);
                          System.out.println(TrialsCoordinatesQuery);
                          }
                          
                          SqlConnection1.disconnect();                         
                          //breakLoop = true;
                       }     
                       break;//break loop after exporting (or cancelling the export)
                    }else{
                        //Throw exception
                    }                    
                }
            }
        }        
    }
    
    
    public int getSessionId(int phaseId,int userId,int sessionNbr){
        try {
            SqlConnection SqlConnection1 = new SqlConnection();
            SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);
            
            ResultSet ResultSet1 = SqlConnection1.executeQuery("SELECT id AS SessionId FROM bci_session "
                        + "WHERE phase_id="+phaseId + " AND user_id="+userId+" ORDER BY id ASC;");
                for(int i = 0 ; i < sessionNbr;i++){
                  ResultSet1.next();//move cursor to the exact session
                }        //Read data in pointed session
                int sessionId = ResultSet1.getInt("SessionId");            
           SqlConnection1.disconnect();
           return sessionId;
        } catch (SQLException ex) {            
            Logger.getLogger(EEGDatabaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            return 0;//failure
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

        jTextField5 = new javax.swing.JTextField();
        InitialsEditText = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        ProfileImage = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BciUserList = new javax.swing.JList();
        DoneButton = new javax.swing.JButton();
        SurnameEditText = new javax.swing.JTextField();
        GenderEditText = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        AgeEditText = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        UserPanelTable2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        UserPanelTable1 = new javax.swing.JTable();
        ExportUserButton = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1003, 720));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField5.setEditable(false);
        jTextField5.setBackground(new java.awt.Color(51, 153, 0));
        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("User List");
        getContentPane().add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 345, 40));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 345, 330));

        DoneButton.setText("Done");
        DoneButton.setEnabled(false);
        getContentPane().add(DoneButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 120, 30));

        SurnameEditText.setEditable(false);
        getContentPane().add(SurnameEditText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 192, -1));

        GenderEditText.setEditable(false);
        getContentPane().add(GenderEditText, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 200, 90, -1));

        jTextField17.setEditable(false);
        jTextField17.setText("Surname");
        getContentPane().add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 192, -1));

        jTextField10.setEditable(false);
        jTextField10.setText("Age");
        getContentPane().add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 90, -1));

        AgeEditText.setEditable(false);
        getContentPane().add(AgeEditText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 90, -1));

        UserPanelTable2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        UserPanelTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {"Nb of Trials", "Nb of Trials", "Nb of Trials", "Nb of Trials", "Nb of Trials"},
                {null, null, null, null, null},
                {"Nb of Runs", "Nb of Runs", "Nb of Runs", "Nb of Runs", "Nb of Runs"},
                {null, null, null, null, null},
                {"Nb of Trials/Run", "Nb of Trials/Run", "Nb of Trials/Run", "Nb of Trials/Run", "Nb of Trials/Run"},
                {null, null, null, null, null}
            },
            new String [] {
                "Imagery Classes", "Target Classes", "Target Classes", "Target Classes", "Imagery Classes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        UserPanelTable2.setPreferredSize(new java.awt.Dimension(375, 207));
        UserPanelTable2.setRowHeight(30);
        UserPanelTable2.setRowSelectionAllowed(false);
        jScrollPane3.setViewportView(UserPanelTable2);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, 590, 240));

        UserPanelTable1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        UserPanelTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Nb of  Sessions", "Nb of  Sessions", "Nb of  Sessions", "Nb of  Sessions", "Nb of  Sessions"},
                {null, null, null, null, null},
                {"Imagery Classes", "Target Classes", "Target Classes", "Target Classes", "Imagery Classes"},
                {null, null, null, null, null},
                {"Nb of  Trials", "Nb of  Trials", "Nb of  Trials", "Nb of  Trials", "Nb of  Trials"},
                {null, null, null, null, null},
                {"Select Session", "Select Session", "Select Session", "Select Session", "Select Session"}
            },
            new String [] {
                "Screening Phase", "1D Vertical Phase", "1D Horizontal Phase", "2D Control Phase", "Special Classification"
            }
        ));
        UserPanelTable1.setPreferredSize(new java.awt.Dimension(375, 210));
        UserPanelTable1.setRowHeight(30);
        UserPanelTable1.setRowSelectionAllowed(false);
        jScrollPane4.setViewportView(UserPanelTable1);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, 590, 240));

        ExportUserButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ExportUserButton.setText("Export User");
        ExportUserButton.setActionCommand("");
        getContentPane().add(ExportUserButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 640, 110, 30));

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(51, 153, 0));
        jTextField6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setText("User Profile");
        getContentPane().add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 345, 30));

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
            java.util.logging.Logger.getLogger(EEGDatabaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EEGDatabaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EEGDatabaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EEGDatabaseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EEGDatabaseFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JTextField AgeEditText;
    protected javax.swing.JList BciUserList;
    protected javax.swing.JButton DoneButton;
    protected javax.swing.JButton ExportUserButton;
    protected javax.swing.JTextField GenderEditText;
    protected javax.swing.JTextField InitialsEditText;
    protected javax.swing.JLabel ProfileImage;
    protected javax.swing.JTextField SurnameEditText;
    protected javax.swing.JTable UserPanelTable1;
    protected javax.swing.JTable UserPanelTable2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
