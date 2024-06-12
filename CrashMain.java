import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CrashMain implements ActionListener{
    JFrame frame = new JFrame();
    CrashPanel carPane = new CrashPanel();
    JPanel overlay = new JPanel();
    InputPanel input = new InputPanel();
    public CrashMain(){
        frame.setContentPane(carPane);
        frame.setGlassPane(input);
        InputPanel.launch.addActionListener(this);
        InputPanel.reset.addActionListener(this);
        input.setOpaque(false);
        input.setVisible(true);
        frame.setSize(1200,800);
        frame.setVisible(true);

    }
    public static void main(String[] args) {
        new CrashMain(); 
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == InputPanel.launch){
            carPane.launch(Integer.parseInt(InputPanel.lanes.getText()), Integer.parseInt(InputPanel.lightDuration.getText())*1000, Integer.parseInt(InputPanel.trafficLevel.getText()), Integer.parseInt(InputPanel.speedLimit.getText())/10);
        }else if(e.getSource() == InputPanel.reset){
            carPane.reset();
        }
    }
}