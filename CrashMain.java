import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * The CrashMain class is the main process of the application
 * It creates the frame and puts all the components of the UI together
 */
public class CrashMain implements ActionListener{
    /*
     * Declare instance variables frame and carPane and define static variable input
     * Input is static so it is easily accessible by CrashPanel (to update Impact Force label)
     * The constructor simply adds the input panel and CrashPanel to the frame and sizes everything properly (and shows the actual application).
     * It also makes an instructional messagebox to show up.
     * The main method simply creates an instance of CrashMain (basically runs the application)
     */
    public JFrame frame = new JFrame("Traffic Physics");
    public CrashPanel carPane = new CrashPanel();
    public static InputPanel input = new InputPanel();
    public CrashMain(){
        frame.setContentPane(carPane);
        InputPanel.launch.addActionListener(this);
        frame.add(input);
        input.setVisible(true);
        frame.setSize(1200,800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        String message = "Welcome to Traffic Physics.\nYou may simulate different traffic conditions by adjusting the number of lanes, speed limit, light duration, etc.\nThis can be done by using the inputs on the top of the screen. Only integers are supported.\nAverage Force (Impact Force) will update when cars crash.";
        JOptionPane.showMessageDialog(null, message, "Welcome to Traffic Physics", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void main(String[] args) {
        new CrashMain(); 
    }

    /*
     * This method takes input from the input panel and passes it to the CrashPanel
     * If an empty String is passed, there are default values to use instead
     * The defaults are:
     * # of Lanes - 2
     * Light Duration (s) - 5s
     * # of Cars - 4
     * Speed Limit (km/h) - 30 km/h
     */
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == InputPanel.launch){
            int lanes = !InputPanel.lanes.getText().equals("")?Integer.parseInt(InputPanel.lanes.getText()):2;
            int lightDuration = !InputPanel.lightDuration.getText().equals("")?Integer.parseInt(InputPanel.lightDuration.getText()):5;
            int trafficLevel = !InputPanel.trafficLevel.getText().equals("")?Integer.parseInt(InputPanel.trafficLevel.getText()):4;
            int speedLimit = !InputPanel.speedLimit.getText().equals("")?Integer.parseInt(InputPanel.speedLimit.getText()):30;
            carPane.launch(lanes, lightDuration*1000, trafficLevel, speedLimit/10);
        }
    }
}