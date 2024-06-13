import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * This class stores all the components that make up the input panel
 * It handles getting input from the user
 * Prevents invalid characters from getting entered
 */
public class InputPanel extends JPanel{
    /*
     * Declare instance and static variables
     * The instance variables do not need to be accessed by other classes (They are just labels that do not change)
     * The static variables are accessed by other classes, so they are static to make things easier
     * The KeyAdapter numsOnly intercepts keyevents fired by text fields, it ensures only digits can be entered into the input fields
     */
    public static JTextField lightDuration = new JTextField(10);
    public static JTextField speedLimit = new JTextField(10);
    public static JTextField lanes = new JTextField(10);
    public static JTextField trafficLevel = new JTextField(10);
    public static JButton launch = new JButton("Launch");
    public JLabel durationLabel = new JLabel("Light Duration:");
    public JLabel speedLabel = new JLabel("Speed Limit:");
    public JLabel lanesLabel = new JLabel("# of Lanes:");
    public JLabel trafficLabel = new JLabel("# of Cars:");
    public JLabel averageForceLabel = new JLabel("Average Force: 0N");
    public int averageForce = 0;
    public KeyAdapter numsOnly = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            JTextField field = (JTextField) e.getSource();
            if (Character.isDigit(e.getKeyChar()) || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                field.setEditable(true);
            } else {
                field.setEditable(false);
            }
        }
    };
    
    /*
     * The constructor adds all the components (labels, fields) to the main input panel
     * Additionally, it adds the KeyAdapter to intercept events from every text field
     */
    public InputPanel(){
        super();
        lightDuration.addKeyListener(numsOnly);
        speedLimit.addKeyListener(numsOnly);
        lanes.addKeyListener(numsOnly);
        trafficLevel.addKeyListener(numsOnly);
        add(durationLabel);
        add(lightDuration);
        add(speedLabel);
        add(speedLimit);
        add(lanesLabel);
        add(lanes);
        add(trafficLabel);
        add(trafficLevel);
        add(launch);
        add(averageForceLabel);
    }

}