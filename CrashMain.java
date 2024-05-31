import javax.swing.JFrame;

public class CrashMain {
    JFrame frame = new JFrame();
    CrashPanel carPane = new CrashPanel();
    public CrashMain(){
        frame.setContentPane(carPane);
        frame.setSize(600,400);
        frame.setVisible(true);
        carPane.carA.y = carPane.getHeight()-50;
        carPane.carA.x = carPane.getWidth()-100;
    }
    public static void main(String[] args) {
        new CrashMain(); 
    }
}
