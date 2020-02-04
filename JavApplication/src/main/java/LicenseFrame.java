import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

public class LicenseFrame extends JFrame {
    private JPanel panel1;
    private JTextArea licenseSerial;
    private JButton enterButton;
    private JLabel errorMessage;

    public LicenseFrame(){
        super("Set up license");
        setVisible(true);
        setContentPane(panel1);

        setSize(525,175);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);

        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Licencheck licencheck = new Licencheck();
                String typeLicense = licencheck.checkLicense(licenseSerial.getText(),"Photoshop",prefs.get("userName","root"), prefs.get("password","root"));
                if (typeLicense!=null){
                    prefs.put("serial_"+prefs.get("userName","root"),licenseSerial.getText());
                    dispose();
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            MainProgram mainProgram = new MainProgram();
                        }
                    });
                }
                else{
                    errorMessage.setVisible(true);
                }
            }
        });
    }
}
