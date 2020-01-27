import javax.swing.*;
public class Main {

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {

            public void run(){
                JFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                loginFrame.setSize(525,175);
                loginFrame.setResizable(false);
                loginFrame.setLocationRelativeTo(null);

            }
        });

    }
}
