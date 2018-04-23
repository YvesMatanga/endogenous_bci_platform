/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.user.bci;

import static com.user.bci.BciPhaseEnum.VERTICAL_1D_PHASE;
import static com.user.bci.ImageryEnum.LEFT_FOOT;
import com.user.sql.SqlConnection;  
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Yves Matanga
 */
public class BciUser {
    //database connection details
    static String RDBMS = "mysql";
    static String DB_NAME = "mtech_research";
    static String SQL_USER_NAME = "root";
    static String SQL_USER_PASSWORD = "root";    
    
    protected int UserId;
    protected int SessionId;
    protected int RunId;
    protected int TrialId;
    
    protected String Surname  ;
    protected String Initials;
    protected int Age;
    protected String Gender;
    protected String FilePath;//absolute path
    protected ArrayList<ArrayList<Integer>> EegSignalsFrame;
    protected BciPhaseEnum BciPhaseState;
                                            /* 32 channels eeg + timing signal*/
    protected int numberOfRunsPerSession;
    protected int numberOfTrialsPerRun;
    
    private int currentSession;
    private double sessionTargetWorkspacePercentage;//percentage of target over screen   
    private double sessionCursorWorkspacePercentage;
    
    private int currentRun;
    private int currentTrial;
    private ImageryEnum CurrentImagery;
    
    protected ImageryEnum Class1Imagery;
    protected ImageryEnum Class2Imagery;
    
    private ArrayList<ImageryEnum> ImageryClassSequence;//determine class sequence
    private ArrayList<Integer> Target2DSequence;
    private ArrayList<Integer> Target1DHorizontalSequence;
    private ArrayList<Integer> Target1DVerticalSequence;
    
    protected EegChannelSelector EEGChannelSelector;
    //control 2d
    protected int Control2DTargetNumber;
    protected int Control1DHorizontalTargetNumber;   
    protected int Control1DVerticalTargetNumber;

    protected int Control2DnbOfTargets;
    protected int Control1DHorizontalNbOfTargets;
    protected int Control1DVerticalNbOfTargets;
    
    /*constructors*/
    
    public BciUser(){
        Surname = "";
        Initials = "";
        Age = 0;
        Gender = "";
        FilePath = "";
        UserId = 0;
        SessionId=0;        
        RunId = 0;
        TrialId = 0;
        Control2DTargetNumber = 1;
        //init EEG FRame        
       EegSignalsFrame = new ArrayList<ArrayList<Integer> >();
       BciPhaseState = BciPhaseEnum.SCREENING_PHASE;
       numberOfRunsPerSession = 0;
       numberOfTrialsPerRun = 0;
       currentSession = 0;
       currentRun = 0;
       currentTrial = 0;              
       //Imagery Classes
       Class1Imagery = ImageryEnum.RIGHT_HAND;
       Class2Imagery = ImageryEnum.RIGHT_FOOT;
       
       //current imagery
       CurrentImagery = Class1Imagery;
       //class sequence generator for screening
       ImageryClassSequence = new ArrayList<ImageryEnum>();
       
       EEGChannelSelector = new EegChannelSelector();//use all the eeg channels
       Control2DnbOfTargets = 8;
       Control1DHorizontalNbOfTargets = 4;
       Control1DVerticalNbOfTargets = 4;
    }
    
    public BciUser(String Surname,String Initials,int Age,String Gender){
        super();//base constructor
        this.Surname = Surname;
        this.Initials = Initials;
        this.Age = Age;
        this.Gender = Gender;       
    }
  //setters
        public void setUserId(int UserId){this.UserId=UserId;}
        public void setSessionId(int SessionId){this.SessionId=SessionId;}
        public void setTrialId(int TrialId){this.TrialId=TrialId;}
        public void setRunId(int RunId){this.RunId=RunId;}
        public void setSurname(String Surname){this.Surname = Surname;}
        public void setInitials(String Initials){this.Initials = Initials;}
        public void setAge(int Age){this.Age = Age;}
        public void setGender(String Gender){this.Gender = Gender;}
        public void setPath(String FilePath){this.FilePath = FilePath;}
        public void setBciPhaseState(BciPhaseEnum BciPhaseState){this.BciPhaseState = BciPhaseState;}
        public void setNumberOfRunsPerSession(int numberOfRunsPerSession){this.numberOfRunsPerSession = numberOfRunsPerSession;}
        public void setNumberOfTrialsPerRun(int numberOfTrialsPerRun){this.numberOfTrialsPerRun = numberOfTrialsPerRun;}
        public void setCurrentSession(int currentSession){this.currentSession= currentSession;}
        public void setCurrentRun(int currentRun){this.currentRun = currentRun;}
        public void setCurrentTrial(int currentTrial){this.currentTrial = currentTrial;}
        public void setCurrentImagery(ImageryEnum CurrentImagery){this.CurrentImagery = CurrentImagery;}
        public void setClass1Imagery(ImageryEnum Class1Imagery){this.Class1Imagery = Class1Imagery;}
        public void setClass2Imagery(ImageryEnum Class2Imagery){this.Class2Imagery = Class2Imagery;}
        public void setEegSignalsFrame(ArrayList<ArrayList<Integer>> EegSignalsFrame){this.EegSignalsFrame = EegSignalsFrame;}
        public void setControl2DTargetNumber(int Control2DTargetNumber) {this.Control2DTargetNumber = Control2DTargetNumber; }        
        public void setSessionTargetWorkspacePercentage(double sessionTargetWorkspacePercentage) {this.sessionTargetWorkspacePercentage = sessionTargetWorkspacePercentage;}        
        public void setSessionCursorWorkspacePercentage(double sessionCursorWorkspacePercentage) {this.sessionCursorWorkspacePercentage = sessionCursorWorkspacePercentage;}
        public void setEEGChannelSelector(EegChannelSelector EEGChannelSelector) {this.EEGChannelSelector = EEGChannelSelector; }
        public void setControl1DHorizontalTargetNumber(int Control1DHorizontalTargetNumber){this.Control1DHorizontalTargetNumber = Control1DHorizontalTargetNumber;   }
        public void setControl1DVerticalTargetNumber(int Control1DVerticalTargetNumber){this.Control1DVerticalTargetNumber = Control1DVerticalTargetNumber;}
        public void setTarget1DHorizontalSequence(ArrayList<Integer> Target1DHorizontalSequence){this.Target1DHorizontalSequence = Target1DHorizontalSequence;    }
        public void setTarget1DVerticalSequence(ArrayList<Integer> Target1DVerticalSequence){this.Target1DVerticalSequence = Target1DVerticalSequence;    }
        
        
    //getters
        public int getUserId(){return this.UserId;}
        public int getSessionId(){return this.SessionId;}
        public int getTrialId(){return this.TrialId;}
        public int getRunId(){return this.RunId;}
        public String getSurname(){return this.Surname;}
        public String getInitials(){return this.Initials;}
        public int getAge(){return this.Age;}
        public String getGender(){return this.Gender;}
        public String getFilePath(){return this.FilePath;}
        public BciPhaseEnum getBciPhaseState(){return this.BciPhaseState;}
        public int getNumberOfRunsPerSession(){return this.numberOfRunsPerSession;}
        public int getNumberOfTrialsPerRun(){return this.numberOfTrialsPerRun;}
        public int getCurrentSession(){return this.currentSession ;}
        public int getCurrentRun(){return this.currentRun ;}
        public int getCurrentTrial(){return this.currentTrial;}
        public ImageryEnum getCurrentImagery(){return this.CurrentImagery;}
        public ImageryEnum getClass1Imagery(){return this.Class1Imagery;}
        public ImageryEnum getClass2Imagery(){return this.Class2Imagery;}
        public ArrayList<ImageryEnum> getImageryClassSequence(){return this.ImageryClassSequence;}//return sequence of imagey for classes
        public ArrayList<ArrayList<Integer>> getEegSignalsFrame(){return this.EegSignalsFrame;}
        public int getControl2DTargetNumber(){ return Control2DTargetNumber;}
        public double getSessionCursorWorkspacePercentage() {return sessionCursorWorkspacePercentage;}
        public double getSessionTargetWorkspacePercentage() { return sessionTargetWorkspacePercentage;}
        public EegChannelSelector getEEGChannelSelector() {return EEGChannelSelector; }
        public int getControl1DHorizontalTargetNumber() {return Control1DHorizontalTargetNumber; }
        public int getControl1DVerticalTargetNumber() {return Control1DVerticalTargetNumber;  }
        public ArrayList<Integer> getTarget1DHorizontalSequence(){return Target1DHorizontalSequence;}
        public ArrayList<Integer> getTarget1DVerticalSequence() {return Target1DVerticalSequence; }     
    
        //methods
        private ArrayList<Integer> generateRandomizedSample(int N){//with data from 1 to N
            ArrayList<Integer> Bank = new ArrayList<Integer>();
            for(int i = 0 ;i < N;i++){//save data from 1 to N
                Bank.add(i+1);
            }
            final int NPerm = 15;//Number of Permutations
            Random randomGen = new Random();
            for(int i=0;i <NPerm;i++){
                int index = randomGen.nextInt(N-1);//generate number between 0 to N-2
                //swap with next value
                int temp = Bank.get(index);
                Bank.set(index,Bank.get(index+1));
                Bank.set(index+1,temp);
            }
            return Bank;
        }
        public ArrayList<Integer> generate2DTargetSequence(){
            int TrialPerRun = this.getNumberOfTrialsPerRun();           
            int sampleBlockMax = (int) Math.ceil(TrialPerRun/Control2DnbOfTargets);
            int sampleBlockMin = (int) Math.floor(TrialPerRun/Control2DnbOfTargets);
            Target2DSequence = new ArrayList<Integer>();
            int i;
             for(i = 0 ; i < sampleBlockMin;i++){//generate random block of nbOfTargets integrs
                 ArrayList<Integer> Sample = generateRandomizedSample(Control2DnbOfTargets);
                 int j=0;
                 for( j = 0 ; j < Control2DnbOfTargets;j++){
                    Target2DSequence.add(Sample.get(j));
                 }
             }
             
             if( i == sampleBlockMax){//more trials left                 
                 ArrayList<Integer> Sample = generateRandomizedSample(Control2DnbOfTargets);
                 int j=0;
                 for( j = 0 ; j < (TrialPerRun-Control2DnbOfTargets*sampleBlockMin);j++){//generate random block of nbOfTargets integrs
                     Target2DSequence.add(Sample.get(j));//pass only a part of the data
                 }
             }
            return Target2DSequence;
        }
        
        public ArrayList<Integer> generate1DHorizontalTargetSequence(){
            int TrialPerRun = this.getNumberOfTrialsPerRun();           
            int sampleBlockMax = (int) Math.ceil(TrialPerRun/Control1DHorizontalNbOfTargets);
            int sampleBlockMin = (int) Math.floor(TrialPerRun/Control1DHorizontalNbOfTargets);
            Target1DHorizontalSequence = new ArrayList<Integer>();
            int i;
             for(i = 0 ; i < sampleBlockMin;i++){//generate random block of nbOfTargets integrs
                 ArrayList<Integer> Sample = generateRandomizedSample(Control1DHorizontalNbOfTargets);
                 int j=0;
                 for( j = 0 ; j < Control1DHorizontalNbOfTargets;j++){
                     int temp = 0;
                     if (Sample.get(j) == 2)
                             temp = 1;
                     else if(Sample.get(j) == 3)
                            temp = 4;
                     else
                            temp = Sample.get(j);
                     
                    Target1DHorizontalSequence.add(temp);
                 }
             }
             
             if( i == sampleBlockMax){//more trials left                 
                 ArrayList<Integer> Sample = generateRandomizedSample(Control1DHorizontalNbOfTargets);
                 int j=0;
                 for( j = 0 ; j < (TrialPerRun-Control1DHorizontalNbOfTargets*sampleBlockMin);j++){//generate random block of nbOfTargets integrs
                                         int temp = 0;
                     if (Sample.get(j) == 2)
                             temp = 1;
                     else if(Sample.get(j) == 3)
                            temp = 4;
                     else
                            temp = Sample.get(j);
                     
                    Target1DHorizontalSequence.add(temp);
                 }
             }            
            return Target1DHorizontalSequence;
        }
        
        public ArrayList<Integer> generate1DVerticalTargetSequence(){
            int TrialPerRun = this.getNumberOfTrialsPerRun();           
            int sampleBlockMax = (int) Math.ceil(TrialPerRun/Control1DVerticalNbOfTargets);
            int sampleBlockMin = (int) Math.floor(TrialPerRun/Control1DVerticalNbOfTargets);
            Target1DVerticalSequence = new ArrayList<Integer>();
            int i;
             for(i = 0 ; i < sampleBlockMin;i++){//generate random block of nbOfTargets integrs
                 ArrayList<Integer> Sample = generateRandomizedSample(Control1DVerticalNbOfTargets);
                 int j=0;
                 for( j = 0 ; j < Control1DVerticalNbOfTargets;j++){
                    Target1DVerticalSequence.add(Sample.get(j));
                 }
             }
             
             if( i == sampleBlockMax){//more trials left                 
                 ArrayList<Integer> Sample = generateRandomizedSample(Control1DVerticalNbOfTargets);
                 int j=0;
                 for( j = 0 ; j < (TrialPerRun-Control1DVerticalNbOfTargets*sampleBlockMin);j++){//generate random block of nbOfTargets integrs
                     Target1DVerticalSequence.add(Sample.get(j));//pass only a part of the data
                 }
             }            
            return Target1DVerticalSequence;
        }
              
        public ArrayList<ImageryEnum> generateClassSequence(){
            int i=0;
            int class1N = this.getNumberOfTrialsPerRun()/2;
            int class2N = this.getNumberOfTrialsPerRun() - class1N;
            //reshuffle clas choice sequence            
            ImageryClassSequence = new ArrayList<ImageryEnum>();
            Random randomGen = new Random();
            
            while(true){
                int x = randomGen.nextInt(2);//generate a number between 0 and 1
                if(x == 1){
                    if(class2N  != 0){
                        ImageryClassSequence.add(Class2Imagery);
                        class2N--;//decrement the remaining spaces
                    }else{
                        if(class1N != 0){
                       ImageryClassSequence.add(Class1Imagery);
                       class1N--;//decrement the remaining spaces
                        }else{
                            break;
                        }
                    }
                }else{//x==0
                    if(class1N  != 0){
                        ImageryClassSequence.add(Class1Imagery);
                        class1N--;//decrement the remaining spaces
                    }else{
                        if(class2N != 0){
                       ImageryClassSequence.add(Class2Imagery);
                       class2N--;//decrement the remaining spaces
                        }else{
                            break;
                        }
                    }
                }
            }
            
            return ImageryClassSequence;
        }
        public static ArrayList<ArrayList<Integer>> getImageryFrame(ArrayList<ArrayList<Integer>> eegf){
        //33 channles last one is timing
            int nbSamplesPerChann = eegf.get(32).size();
            int startIndex=0;
            int stopIndex=0;
            boolean foundStart=false;
            boolean foundStop = false;
            
        for(int i = 0 ; i <nbSamplesPerChann;i++){
            if(eegf.get(32).get(i) == 1){
                startIndex = i;
                foundStart = true;//start sign is found
                //System.out.println("found start");
            }else if(eegf.get(32).get(i) == 2){
                stopIndex = i;
                foundStop = true;//stop sign is found
                //System.out.println("found stop");
            }
        }
        
            if(foundStart && foundStop){
                ArrayList<ArrayList<Integer>> ImageryFrame = new ArrayList<ArrayList<Integer>>();
                //create 32 channel matrix
                for(int i=0;i < 32;i++){
                    ImageryFrame.add(new ArrayList<Integer>());
                }
                //load data 
                for(int i=startIndex;i< stopIndex+1;i++){
                    for(int j=0;j<32;j++){
                        ImageryFrame.get(j).add(eegf.get(j).get(i));//get samples per channels
                    }
                }
                //EegSignalsFrame = ImageryFrame;
                return ImageryFrame;//return screened frame
                
            }else{
                //EegSignalsFrame = null;
                return null;//frame not well synced
            }
    }        
        public static int getPhaseId(BciPhaseEnum BciPhaseEnum1){//static function above scope of object
            switch(BciPhaseEnum1){
                case SCREENING_PHASE:
                    return 1;                
                case VERTICAL_1D_PHASE:
                    return 2;                                    
                case HORIZONTAL_1D_PHASE:
                    return 3;                                
                case CONTROL_2D_PHASE:
                    return 4;                
                default:
                    return 1;                
            }
        }
        public static int getImageryTypeId(ImageryEnum ImageryEnum1){//static function above scope of object
            switch(ImageryEnum1){
                case LEFT_HAND:
                    return 1;                
                case RIGHT_HAND:
                    return 2;                                    
                case LEFT_FOOT:
                    return 3;
                case RIGHT_FOOT:
                    return 4;                                                 
                case REST:
                    return 5;
                default:
                    return 1;                
            }
        }
        public static BciUser loadSessionFromDb(BciUser BciUser1,int user_id,int phase_id){            
        try {
            SqlConnection sqlConnection1 = new SqlConnection();
            sqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);
     
         ResultSet ResultSet1 = sqlConnection1.executeQuery("SELECT MAX(id) AS latest_session ,"
                 + "COUNT(id) AS number_of_sessions FROM bci_session"
                 + " WHERE user_id ="+user_id+" AND phase_id ="+phase_id+";");
         
         ResultSet1.next();
         int nbrOfCurrentPhaseSessions = ResultSet1.getInt("number_of_sessions");
         int latestCurrentPhaseSessionId = ResultSet1.getInt("latest_session"); 
         
         if(nbrOfCurrentPhaseSessions == 0){//no latest  session  for current phase 
               
              Date DateNow = new Date();
              SimpleDateFormat Ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");                             
              
             //create new session timestamped
            sqlConnection1.executeUpdate("INSERT INTO bci_session(user_id,phase_id,session_date)"
                     + " VALUES("+user_id+","+phase_id+",'"+Ft.format(DateNow)+"');");//create a session             
                         
            //get session id
             ResultSet1 = sqlConnection1.executeQuery("SELECT MAX(id) AS highest_session_id "
                     + "FROM bci_session;");
             ResultSet1.next();
             int latestSessionId = ResultSet1.getInt("highest_session_id");                                           
             BciUser1.setSessionId(latestSessionId);   
             BciUser1.setCurrentSession(1);//first session and first run                              
             BciUser1.setCurrentRun(1);
            
             //get settings of session  
             ResultSet1 = sqlConnection1.executeQuery("SELECT number_of_runs_per_session,number_of_trials_per_run"
                    + " FROM bci_session"
                 + " WHERE id = "+BciUser1.getSessionId()+";"); 
            
            ResultSet1.next();//place cursoer at first row
            BciUser1.setNumberOfRunsPerSession(ResultSet1.getByte("number_of_runs_per_session"));//gte default settings
            BciUser1.setNumberOfTrialsPerRun(ResultSet1.getByte("number_of_trials_per_run"));
             
           }else{                                 
             
             ResultSet1 = sqlConnection1.executeQuery("SELECT number_of_runs_per_session,number_of_trials_per_run"
                    + " FROM bci_session"
                 + " WHERE id = "+latestCurrentPhaseSessionId+";");//get settings of session 
             
            ResultSet1.next();//place cursoer at first row
            BciUser1.setNumberOfRunsPerSession(ResultSet1.getByte("number_of_runs_per_session"));
            BciUser1.setNumberOfTrialsPerRun(ResultSet1.getByte("number_of_trials_per_run")); 
             
             //get the latest run
             ResultSet1 = sqlConnection1.executeQuery("SELECT "
                     + "COUNT(id) AS number_of_lapsed_runs FROM bci_run "
                     + "WHERE session_id ="+latestCurrentPhaseSessionId+";");
             ResultSet1.next();
             int nbOfLapsedRuns = ResultSet1.getInt("number_of_lapsed_runs");                          
                     if(nbOfLapsedRuns==0){//if thre is no run
                         BciUser1.setSessionId(latestCurrentPhaseSessionId);//keep the previous session id
                         BciUser1.setCurrentSession(nbrOfCurrentPhaseSessions);
                         BciUser1.setCurrentRun(1);                         
                     }else{                                      
                         if(nbOfLapsedRuns >= BciUser1.getNumberOfRunsPerSession()){//if session is full
                             Date DateNow = new Date();
                             SimpleDateFormat Ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");      
                             //create new session timestamped
                           sqlConnection1.executeUpdate("INSERT INTO bci_session(user_id,phase_id,session_date)"
                             + " VALUES("+user_id+","+phase_id+",'"+Ft.format(DateNow)+"');");//create a session
                             BciUser1.setCurrentSession(nbrOfCurrentPhaseSessions+1);//increment session number
                             BciUser1.setCurrentRun(1);//new initial run                             
                             //get session id
                           ResultSet1 = sqlConnection1.executeQuery("SELECT MAX(id) AS highest_session_id "
                           + "FROM bci_session;"); 
                           ResultSet1.next();
                           BciUser1.setSessionId(ResultSet1.getInt("highest_session_id"));              
                             
                            //get settings of session 
                            ResultSet1 = sqlConnection1.executeQuery("SELECT number_of_runs_per_session,number_of_trials_per_run"
                                 + " FROM bci_session"
                                 + " WHERE id = "+BciUser1.getSessionId()+";"); 
            
                            ResultSet1.next();//place cursoer at first row
                            BciUser1.setNumberOfRunsPerSession(ResultSet1.getByte("number_of_runs_per_session"));
                            BciUser1.setNumberOfTrialsPerRun(ResultSet1.getByte("number_of_trials_per_run")); 
                                                                                      
                         }else{
                             BciUser1.setSessionId(latestCurrentPhaseSessionId);//keep the previous session id                                          
                             BciUser1.setCurrentSession(nbrOfCurrentPhaseSessions);
                             BciUser1.setCurrentRun(nbOfLapsedRuns+1);//point to next run
                         }
                     }                                                  
                     BciUser1.setCurrentTrial(1);//first trial in any case
            }
                    ResultSet1.close();
                    return BciUser1;
            }catch (SQLException ex) {
                ex.printStackTrace();
                return null;            
        }
      }
        public static ImageryEnum getImageryPerIndex(int id){
            switch(id){
               case 0:
                   return ImageryEnum.LEFT_HAND;                   
               case 1:
                   return ImageryEnum.RIGHT_HAND;                   
               case 2:
                   return ImageryEnum.LEFT_FOOT;                   
               case 3:
                   return ImageryEnum.RIGHT_FOOT;                   
               case 4:
                   return ImageryEnum.REST;
               default:
                   return ImageryEnum.LEFT_HAND;                  
           }
        }
        public static BciPhaseEnum getPhasePerIndex(int id){
            switch(id){
                case 0:
                    return BciPhaseEnum.SCREENING_PHASE;
                case 1:
                    return BciPhaseEnum.VERTICAL_1D_PHASE;
                case 2:
                    return BciPhaseEnum.HORIZONTAL_1D_PHASE;
                case 3:
                    return BciPhaseEnum.CONTROL_2D_PHASE;
                default:
                    return BciPhaseEnum.SCREENING_PHASE;
            }
        }

    public ArrayList<Integer> getTarget2DSequence() {
        return Target2DSequence;
    }

    public void setTarget2DSequence(ArrayList<Integer> Target2DSequence) {
        this.Target2DSequence = Target2DSequence;
    }
}
