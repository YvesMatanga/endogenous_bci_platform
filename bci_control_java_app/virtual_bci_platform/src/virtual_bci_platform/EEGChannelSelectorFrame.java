/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_bci_platform;

import com.user.bci.BciUser;
import com.user.bci.EegChannelSelector;
import com.user.sql.SqlConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yves Matanga
 */
public class EEGChannelSelectorFrame extends javax.swing.JFrame {
      
    BciUser DefaultBciUser;
    EegChannelSelector EEGChannelList;
    
     //database connection details
    String RDBMS = "mysql";
    String DB_NAME = "mtech_research";
    String SQL_USER_NAME = "root";
    String SQL_USER_PASSWORD = "root";
    
        //database 
    final String eegChannelsQuery = "update bci_session " +
"set  " +
" fp1_used=?," +
" fp2_used=?," +
" af3_used=?," +
" af4_used=?," +
" f7_used=?," +
" f3_used=?," +
" fz_used=?," +
" f4_used=?," +
" f8_used=?," +
" fc5_used=?," +
" fc1_used=?," +
" fc2_used=?," +
" fc6_used=?," +
" t7_used=?," +
" c3_used=?," +
" cz_used=?," +
"" +
" c4_used=?," +
" t8_used=?," +
" cp5_used=?," +
" cp1_used=?," +
" cp2_used=?," +
" cp6_used=?," +
" p7_used=?," +
" p3_used=?," +
"" +
" pz_used=?," +
" p4_used=?," +
" p8_used=?," +
" po7_used=?," +
" po3_used=?," +
" po4_used=?," +
" po8_used=?," +
" oz_used=?" +
" " +
" WHERE id=?;";
    
    /**
     * Creates new form EEGChannelSelectorFrame
     */
    public EEGChannelSelectorFrame() {
        DefaultBciUser = null;
        initComponents();        
        userInitComponents(DefaultBciUser);
        
    }
    
    public EEGChannelSelectorFrame(BciUser BciUser1) {
        DefaultBciUser = BciUser1;
        initComponents();
        userInitComponents(DefaultBciUser);        
    }
    
    private void userInitComponents(BciUser BciUser1){        
       
        if(BciUser1==null){
        EEGChannelList = new EegChannelSelector();
        }else{
        EEGChannelList = BciUser1.getEEGChannelSelector();
        }
        
        viewUpdate(EEGChannelList);
        //Listeners
        OkButton.addActionListener(new SaveButtonListener());
        CancelButton.addActionListener(new CancelButtonListener());
        DefaultButton.addActionListener(new DefaultButtonListener());
    }
    
    public void loadEEGChannels(EegChannelSelector ChannelList){
        viewUpdate(ChannelList);
    }
    
    public EegChannelSelector getEEGChannelList(){
        return EEGChannelList;
    }
    
    protected void viewUpdate(EegChannelSelector ChannelList){
        //update view
        Fp1CheckBox.setSelected(ChannelList.isFp1_used());
        Fp2CheckBox.setSelected(ChannelList.isFp2_used());
        Af3CheckBox.setSelected(ChannelList.isAf3_used());
        Af4CheckBox.setSelected(ChannelList.isAf4_used());
        F7CheckBox.setSelected(ChannelList.isF7_used());
        F3CheckBox.setSelected(ChannelList.isF3_used());
        FzCheckBox.setSelected(ChannelList.isFz_used());
        Fc2CheckBox.setSelected(ChannelList.isFc2_used());
        Fc1CheckBox.setSelected(ChannelList.isFc1_used());
        Fc5CheckBox.setSelected(ChannelList.isFc5_used());
        F4CheckBox.setSelected(ChannelList.isF4_used());
        F8CheckBox.setSelected(ChannelList.isF8_used());
        Fc6CheckBox.setSelected(ChannelList.isFc6_used());
        T7CheckBox.setSelected(ChannelList.isT7_used());
        C3CheckBox.setSelected(ChannelList.isC3_used());
        CzCheckBox.setSelected(ChannelList.isCz_used());
        C4CheckBox.setSelected(ChannelList.isC4_used());
        T8CheckBox.setSelected(ChannelList.isT8_used());
        CP5CheckBox.setSelected(ChannelList.isCp5_used());
        CP1CheckBox.setSelected(ChannelList.isCp1_used());
        CP2CheckBox.setSelected(ChannelList.isCp2_used());
        CP6CheckBox.setSelected(ChannelList.isCp6_used());
        P7CheckBox.setSelected(ChannelList.isP7_used());
        P3CheckBox.setSelected(ChannelList.isP3_used());
        PZCheckBox.setSelected(ChannelList.isPz_used());
        P4CheckBox.setSelected(ChannelList.isP4_used());
        P8CheckBox.setSelected(ChannelList.isP8_used());
        PO7CheckBox.setSelected(ChannelList.isPo7_used());
        PO3CheckBox.setSelected(ChannelList.isPo3_used());
        PO4CheckBox.setSelected(ChannelList.isPo4_used());
        PO8CheckBox.setSelected(ChannelList.isPo8_used());
        OZCheckBox.setSelected(ChannelList.isOz_used());
        
    }
    
    //Listeners    
    class SaveButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
             //load view to model
        EEGChannelList.setFp1_used(Fp1CheckBox.isSelected());
        EEGChannelList.setFp2_used(Fp2CheckBox.isSelected());
        EEGChannelList.setAf3_used(Af3CheckBox.isSelected());
        EEGChannelList.setAf4_used(Af4CheckBox.isSelected());
        EEGChannelList.setF7_used(F7CheckBox.isSelected());
        EEGChannelList.setF3_used(F3CheckBox.isSelected());
        EEGChannelList.setFz_used(FzCheckBox.isSelected());
        EEGChannelList.setFc2_used(Fc2CheckBox.isSelected());
        EEGChannelList.setFc1_used(Fc1CheckBox.isSelected());
        EEGChannelList.setFc5_used(Fc5CheckBox.isSelected());
        EEGChannelList.setF4_used(F4CheckBox.isSelected());
        EEGChannelList.setF8_used(F8CheckBox.isSelected());
        EEGChannelList.setFc6_used(Fc6CheckBox.isSelected());
        EEGChannelList.setT7_used(T7CheckBox.isSelected());
        EEGChannelList.setC3_used(C3CheckBox.isSelected());
        EEGChannelList.setCz_used(CzCheckBox.isSelected());
        EEGChannelList.setC4_used(C4CheckBox.isSelected());
        EEGChannelList.setT8_used(T8CheckBox.isSelected());
        EEGChannelList.setCp5_used(CP5CheckBox.isSelected());
        EEGChannelList.setCp1_used(CP1CheckBox.isSelected());
        EEGChannelList.setCp2_used(CP2CheckBox.isSelected());
        EEGChannelList.setCp6_used(CP6CheckBox.isSelected());
        EEGChannelList.setP7_used(P7CheckBox.isSelected());
        EEGChannelList.setP3_used(P3CheckBox.isSelected());
        EEGChannelList.setPz_used(PZCheckBox.isSelected());
        EEGChannelList.setP4_used(P4CheckBox.isSelected());
        EEGChannelList.setP8_used(P8CheckBox.isSelected());
        EEGChannelList.setPo7_used(PO7CheckBox.isSelected());
        EEGChannelList.setPo3_used(PO3CheckBox.isSelected());
        EEGChannelList.setPo4_used(PO4CheckBox.isSelected());
        EEGChannelList.setPo8_used(PO8CheckBox.isSelected());
        EEGChannelList.setOz_used(OZCheckBox.isSelected());     
        
        if(DefaultBciUser!=null){ 
            DefaultBciUser.setEEGChannelSelector(EEGChannelList);
            saveChannelToDb(EEGChannelList,DefaultBciUser.getSessionId());            
        }   
            dispose();//close frame
       }
    }
    
    
    public EegChannelSelector saveChannelToDb(EegChannelSelector ChannelList,int sessionId){
        try {
            //Session
                    SqlConnection SqlConnection1 = new SqlConnection();
                    SqlConnection1.connect(RDBMS,DB_NAME,SQL_USER_NAME,SQL_USER_PASSWORD);//get access
                    
                    PreparedStatement PreparedStatement1 = SqlConnection1.getConn().prepareStatement(eegChannelsQuery);
                    
                    PreparedStatement1.setBoolean(1,ChannelList.isFp1_used());
                    PreparedStatement1.setBoolean(2,ChannelList.isFp2_used());
                    PreparedStatement1.setBoolean(3,ChannelList.isAf3_used());
                    PreparedStatement1.setBoolean(4,ChannelList.isAf4_used());
                    PreparedStatement1.setBoolean(5,ChannelList.isF7_used());
                    PreparedStatement1.setBoolean(6,ChannelList.isF3_used());
                    PreparedStatement1.setBoolean(7,ChannelList.isFz_used());
                    PreparedStatement1.setBoolean(8,ChannelList.isF4_used());
                    
                    PreparedStatement1.setBoolean(9,ChannelList.isF8_used());
                    PreparedStatement1.setBoolean(10,ChannelList.isFc5_used());
                    PreparedStatement1.setBoolean(11,ChannelList.isFc1_used());
                    PreparedStatement1.setBoolean(12,ChannelList.isFc2_used());
                    PreparedStatement1.setBoolean(13,ChannelList.isFc6_used());
                    PreparedStatement1.setBoolean(14,ChannelList.isT7_used());
                    PreparedStatement1.setBoolean(15,ChannelList.isC3_used());
                    PreparedStatement1.setBoolean(16,ChannelList.isCz_used());
                    
                    PreparedStatement1.setBoolean(17,ChannelList.isC4_used());
                    PreparedStatement1.setBoolean(18,ChannelList.isT8_used());
                    PreparedStatement1.setBoolean(19,ChannelList.isCp5_used());
                    PreparedStatement1.setBoolean(20,ChannelList.isCp1_used());
                    PreparedStatement1.setBoolean(21,ChannelList.isCp2_used());
                    PreparedStatement1.setBoolean(22,ChannelList.isCp6_used());
                    PreparedStatement1.setBoolean(23,ChannelList.isP7_used());
                    PreparedStatement1.setBoolean(24,ChannelList.isP3_used());
                    
                    PreparedStatement1.setBoolean(25,ChannelList.isPz_used());
                    PreparedStatement1.setBoolean(26,ChannelList.isP4_used());
                    PreparedStatement1.setBoolean(27,ChannelList.isP8_used());
                    PreparedStatement1.setBoolean(28,ChannelList.isPo7_used());
                    PreparedStatement1.setBoolean(29,ChannelList.isPo3_used());
                    PreparedStatement1.setBoolean(30,ChannelList.isPo4_used());
                    PreparedStatement1.setBoolean(31,ChannelList.isPo8_used());
                    PreparedStatement1.setBoolean(32,ChannelList.isOz_used());
                    
                    PreparedStatement1.setInt(33,sessionId);
                    
                    PreparedStatement1.executeUpdate();
                    
                    SqlConnection1.disconnect();
                    return ChannelList;
                    
        } catch (SQLException ex) {
            Logger.getLogger(EEGChannelSelectorFrame.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    class CancelButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();//dispose frame
        }        
    }

    class DefaultButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            EEGChannelList = new EegChannelSelector();
            viewUpdate(EEGChannelList);
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

        jTextField1 = new javax.swing.JTextField();
        OkButton = new javax.swing.JButton();
        DefaultButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        Fp1CheckBox = new javax.swing.JCheckBox();
        Fp2CheckBox = new javax.swing.JCheckBox();
        Af3CheckBox = new javax.swing.JCheckBox();
        Af4CheckBox = new javax.swing.JCheckBox();
        F7CheckBox = new javax.swing.JCheckBox();
        F3CheckBox = new javax.swing.JCheckBox();
        FzCheckBox = new javax.swing.JCheckBox();
        F4CheckBox = new javax.swing.JCheckBox();
        F8CheckBox = new javax.swing.JCheckBox();
        CzCheckBox = new javax.swing.JCheckBox();
        C3CheckBox = new javax.swing.JCheckBox();
        Fc1CheckBox = new javax.swing.JCheckBox();
        Fc2CheckBox = new javax.swing.JCheckBox();
        Fc6CheckBox = new javax.swing.JCheckBox();
        T7CheckBox = new javax.swing.JCheckBox();
        Fc5CheckBox = new javax.swing.JCheckBox();
        C4CheckBox = new javax.swing.JCheckBox();
        P7CheckBox = new javax.swing.JCheckBox();
        P3CheckBox = new javax.swing.JCheckBox();
        CP1CheckBox = new javax.swing.JCheckBox();
        CP5CheckBox = new javax.swing.JCheckBox();
        CP2CheckBox = new javax.swing.JCheckBox();
        CP6CheckBox = new javax.swing.JCheckBox();
        T8CheckBox = new javax.swing.JCheckBox();
        P4CheckBox = new javax.swing.JCheckBox();
        PO4CheckBox = new javax.swing.JCheckBox();
        PO3CheckBox = new javax.swing.JCheckBox();
        P8CheckBox = new javax.swing.JCheckBox();
        PO7CheckBox = new javax.swing.JCheckBox();
        OZCheckBox = new javax.swing.JCheckBox();
        PO8CheckBox = new javax.swing.JCheckBox();
        PZCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Select EEG Channels");
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 345, 25));

        OkButton.setText("Ok");
        getContentPane().add(OkButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 265, 60, 35));

        DefaultButton.setText("Default");
        getContentPane().add(DefaultButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 265, -1, 35));

        CancelButton.setText("Cancel");
        getContentPane().add(CancelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 265, -1, 35));

        Fp1CheckBox.setSelected(true);
        Fp1CheckBox.setText("FP1");
        getContentPane().add(Fp1CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, -1));

        Fp2CheckBox.setSelected(true);
        Fp2CheckBox.setText("FP2");
        getContentPane().add(Fp2CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, -1));

        Af3CheckBox.setSelected(true);
        Af3CheckBox.setText("AF3");
        getContentPane().add(Af3CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));

        Af4CheckBox.setSelected(true);
        Af4CheckBox.setText("AF4");
        getContentPane().add(Af4CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, -1));

        F7CheckBox.setSelected(true);
        F7CheckBox.setText("F7");
        getContentPane().add(F7CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, -1, -1));

        F3CheckBox.setSelected(true);
        F3CheckBox.setText("F3");
        getContentPane().add(F3CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        FzCheckBox.setSelected(true);
        FzCheckBox.setText("FZ");
        getContentPane().add(FzCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, -1));

        F4CheckBox.setSelected(true);
        F4CheckBox.setText("F4");
        getContentPane().add(F4CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, -1, -1));

        F8CheckBox.setSelected(true);
        F8CheckBox.setText("F8");
        getContentPane().add(F8CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, -1));

        CzCheckBox.setSelected(true);
        CzCheckBox.setText("CZ");
        getContentPane().add(CzCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, -1, -1));

        C3CheckBox.setSelected(true);
        C3CheckBox.setText("C3");
        getContentPane().add(C3CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, -1, -1));

        Fc1CheckBox.setSelected(true);
        Fc1CheckBox.setText("FC1");
        getContentPane().add(Fc1CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, -1, -1));

        Fc2CheckBox.setSelected(true);
        Fc2CheckBox.setText("FC2");
        getContentPane().add(Fc2CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, -1, -1));

        Fc6CheckBox.setSelected(true);
        Fc6CheckBox.setText("FC6");
        getContentPane().add(Fc6CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, -1, -1));

        T7CheckBox.setSelected(true);
        T7CheckBox.setText("T7");
        getContentPane().add(T7CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, -1, -1));

        Fc5CheckBox.setSelected(true);
        Fc5CheckBox.setText("FC5");
        getContentPane().add(Fc5CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, -1, -1));

        C4CheckBox.setSelected(true);
        C4CheckBox.setText("C4");
        getContentPane().add(C4CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));

        P7CheckBox.setSelected(true);
        P7CheckBox.setText("P7");
        getContentPane().add(P7CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, -1, -1));

        P3CheckBox.setSelected(true);
        P3CheckBox.setText("P3");
        getContentPane().add(P3CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 220, -1, -1));

        CP1CheckBox.setSelected(true);
        CP1CheckBox.setText("CP1");
        getContentPane().add(CP1CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, -1, -1));

        CP5CheckBox.setSelected(true);
        CP5CheckBox.setText("CP5");
        getContentPane().add(CP5CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, -1, -1));

        CP2CheckBox.setSelected(true);
        CP2CheckBox.setText("CP2");
        getContentPane().add(CP2CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 160, -1, -1));

        CP6CheckBox.setSelected(true);
        CP6CheckBox.setText("CP6");
        getContentPane().add(CP6CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, -1, -1));

        T8CheckBox.setSelected(true);
        T8CheckBox.setText("T8");
        getContentPane().add(T8CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, -1));

        P4CheckBox.setSelected(true);
        P4CheckBox.setText("P4");
        getContentPane().add(P4CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 100, -1, -1));

        PO4CheckBox.setSelected(true);
        PO4CheckBox.setText("PO4");
        getContentPane().add(PO4CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 180, -1, -1));

        PO3CheckBox.setSelected(true);
        PO3CheckBox.setText("PO3");
        getContentPane().add(PO3CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 160, -1, -1));

        P8CheckBox.setSelected(true);
        P8CheckBox.setText("P8");
        getContentPane().add(P8CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 120, -1, -1));

        PO7CheckBox.setSelected(true);
        PO7CheckBox.setText("PO7");
        getContentPane().add(PO7CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, -1, -1));

        OZCheckBox.setSelected(true);
        OZCheckBox.setText("OZ");
        getContentPane().add(OZCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, -1, -1));

        PO8CheckBox.setSelected(true);
        PO8CheckBox.setText("PO8");
        getContentPane().add(PO8CheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 200, -1, -1));

        PZCheckBox.setSelected(true);
        PZCheckBox.setText("PZ");
        getContentPane().add(PZCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, -1, -1));

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
            java.util.logging.Logger.getLogger(EEGChannelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EEGChannelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EEGChannelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EEGChannelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EEGChannelSelectorFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JCheckBox Af3CheckBox;
    protected javax.swing.JCheckBox Af4CheckBox;
    protected javax.swing.JCheckBox C3CheckBox;
    protected javax.swing.JCheckBox C4CheckBox;
    protected javax.swing.JCheckBox CP1CheckBox;
    protected javax.swing.JCheckBox CP2CheckBox;
    protected javax.swing.JCheckBox CP5CheckBox;
    protected javax.swing.JCheckBox CP6CheckBox;
    protected javax.swing.JButton CancelButton;
    protected javax.swing.JCheckBox CzCheckBox;
    protected javax.swing.JButton DefaultButton;
    protected javax.swing.JCheckBox F3CheckBox;
    protected javax.swing.JCheckBox F4CheckBox;
    protected javax.swing.JCheckBox F7CheckBox;
    protected javax.swing.JCheckBox F8CheckBox;
    protected javax.swing.JCheckBox Fc1CheckBox;
    protected javax.swing.JCheckBox Fc2CheckBox;
    protected javax.swing.JCheckBox Fc5CheckBox;
    protected javax.swing.JCheckBox Fc6CheckBox;
    private javax.swing.JCheckBox Fp1CheckBox;
    protected javax.swing.JCheckBox Fp2CheckBox;
    protected javax.swing.JCheckBox FzCheckBox;
    protected javax.swing.JCheckBox OZCheckBox;
    protected javax.swing.JButton OkButton;
    protected javax.swing.JCheckBox P3CheckBox;
    protected javax.swing.JCheckBox P4CheckBox;
    protected javax.swing.JCheckBox P7CheckBox;
    protected javax.swing.JCheckBox P8CheckBox;
    protected javax.swing.JCheckBox PO3CheckBox;
    protected javax.swing.JCheckBox PO4CheckBox;
    protected javax.swing.JCheckBox PO7CheckBox;
    protected javax.swing.JCheckBox PO8CheckBox;
    protected javax.swing.JCheckBox PZCheckBox;
    protected javax.swing.JCheckBox T7CheckBox;
    protected javax.swing.JCheckBox T8CheckBox;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
