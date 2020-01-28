import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MainProgram extends JFrame{
    private JPanel panel;
    private JButton startButton;
    private JLabel number;
    private JTextArea maxNumber;


    public MainProgram(){
        super("Main Program");
        setVisible(true);
        setSize(525,175);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);


        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Random random = new Random();
                Integer randomNumber;
                try {
                    randomNumber = random.nextInt(Integer.valueOf(maxNumber.getText()));
                }catch (Exception e){
                    randomNumber=random.nextInt(10);
                }

                number.setText(randomNumber.toString());
            }
        });
    }
}
