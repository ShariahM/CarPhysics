import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CrashMain implements ActionListener{
    JFrame frame = new JFrame();
    CrashPanel carPane = new CrashPanel();
    JPanel overlay = new JPanel();
    JButton launch = new JButton("Launch");
    public CrashMain(){
        frame.setContentPane(carPane);
        frame.setGlassPane(overlay);
        launch.addActionListener(this);
        overlay.add(launch);
        overlay.setOpaque(false);
        overlay.setVisible(true);
        frame.setSize(600,400);
        frame.setVisible(true);


    }
    public static void main(String[] args) {
        new CrashMain(); 
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == launch){
            Car carA = carPane.carA;
            Car carB = carPane.carB;
            carA.y = 0;
            carA.x = carPane.getWidth()/2;
            carB.y = carPane.getHeight()/2;
            carB.x = 0;
            carA.targetAngle = 270;
            carA.angle = 270;
            carB.targetAngle = 0;
            carB.angle = 0;
            carA.velocity = carA.initialVelocity;
            carB.velocity = carB.initialVelocity;
            carPane.movementTimer.restart();
        }
    }
}
