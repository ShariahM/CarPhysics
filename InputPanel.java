import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanel extends JPanel{
    public static JTextField lightDuration = new JTextField(10);
    public static JTextField speedLimit = new JTextField(10);
    public static JTextField lanes = new JTextField(10);
    public static JTextField trafficLevel = new JTextField(10);
    public static JButton launch = new JButton("Launch");
    public static JButton reset = new JButton("Reset");
    public JLabel durationLabel = new JLabel("Light Duration:");
    public JLabel speedLabel = new JLabel("Speed Limit:");
    public JLabel lanesLabel = new JLabel("# of Lanes:");
    public JLabel trafficLabel = new JLabel("# of Cars:");
    
    public InputPanel(){
        super();
        add(durationLabel);
        add(lightDuration);
        add(speedLabel);
        add(speedLimit);
        add(lanesLabel);
        add(lanes);
        add(trafficLabel);
        add(trafficLevel);
        add(launch);
        add(reset);
    }
    
}
