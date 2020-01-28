import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
public class LoginFrame extends JFrame {
    private JPanel panel1;
    private JTextField inputUser;
    private JPasswordField inputPassword;
    private JButton loginButton;
    private JButton exitButton;
    Licencheck licencheck = new Licencheck();
    Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);


    public LoginFrame(){
        super("Login");
        setVisible(true);
        setSize(525,175);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String userDefault = null;
        String passDefault = null;
        userDefault = prefs.get("userName", "root");
        passDefault = prefs.get("password", "root");
        if (!userDefault.equals("root") && !passDefault.equals("root")){
            inputUser.setText(userDefault);
            inputPassword.setText(String.valueOf(passDefault));
        }
        setContentPane(panel1);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                System.exit(0);
            }
        });
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String userName = inputUser.getText();
                char[] password = inputPassword.getPassword();

                boolean exists=licencheck.checkAccount(userName,String.valueOf(password));
                if(exists){
                    prefs.put("userName",userName);
                    prefs.put("password",String.valueOf(password));
                    dispose();

                    String serial = null;
                    serial = prefs.get("serial_"+userName,"root");
                    if (!serial.equals("root")) {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                MainProgram mainProgram = new MainProgram();
                            }
                        });
                    }else{
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                LicenseFrame licenseFrame = new LicenseFrame();
                            }
                        });
                    }
                }else{
                    System.out.println("Not exists");
                }
            }
        });
    }

}
