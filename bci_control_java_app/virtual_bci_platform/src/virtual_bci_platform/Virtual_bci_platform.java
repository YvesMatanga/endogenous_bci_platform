/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package virtual_bci_platform;

import com.user.sql.SqlConnection;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Yves Matanga
 */
public class Virtual_bci_platform {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      
      /*set look and feel*/
            //com.sun.java.swing.plaf.windows.WindowsLookAndFeel
            //javax.swing.plaf.nimbus.NimbusLookAndFeel
      try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
     } catch (ClassNotFoundException ex) {
        Logger.getLogger(Virtual_bci_platform.class.getName()).log(Level.SEVERE, null, ex);
     } catch (InstantiationException ex) {
        Logger.getLogger(Virtual_bci_platform.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IllegalAccessException ex) {
         Logger.getLogger(Virtual_bci_platform.class.getName()).log(Level.SEVERE, null, ex);
     } catch (UnsupportedLookAndFeelException ex) {
         Logger.getLogger(Virtual_bci_platform.class.getName()).log(Level.SEVERE, null, ex);
     }        
     
     /*run sql script for database mtech_research that must exists already/
      */
      SqlConnection sqlConnection1 = new SqlConnection("com.mysql.jdbc.Driver");
      sqlConnection1.connect("mysql","mtech_research","root","root");/*connect to mysql root*/
      
      String cur_dir = new File("").getAbsolutePath();
      String path = cur_dir + "/src/db/db_set_up.sql";      
      
      //sqlConnection1.runScript(path);//run database set up file not working properly
      sqlConnection1.disconnect();     
          
     /*setting frame to register users on second display*/
     
     GraphicsEnvironment GraphicsEnvironment1 = GraphicsEnvironment.getLocalGraphicsEnvironment();
     GraphicsDevice[] GraphicsDevices = GraphicsEnvironment1.getScreenDevices();
     
     ControlFrame MainSettingsFrame = new ControlFrame();
     MainSettingsFrame.setVisible(true);
     //main_settings_frame.setVisible(true);       
     try{
         MainSettingsFrame.setLocation(GraphicsDevices[1].getDefaultConfiguration().getBounds().x,
                GraphicsDevices[1].getDefaultConfiguration().getBounds().y);
        //GraphicsDevices[1].setFullScreenWindow(MainSettingsFrame);                      
     }catch(Exception ex){         
     }
     
    }
}
