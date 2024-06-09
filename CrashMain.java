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
    JButton reset = new JButton("Reset");
    public CrashMain(){
        frame.setContentPane(carPane);
        frame.setGlassPane(overlay);
        launch.addActionListener(this);
        reset.addActionListener(this);
        overlay.add(launch);
        overlay.add(reset);
        overlay.setOpaque(false);
        overlay.setVisible(true);
        frame.setSize(1200,800);
        frame.setVisible(true);
        carPane.intersectionPolygon.addPoint(carPane.getWidth()/2-carPane.laneWidth*carPane.lanes, carPane.getHeight()/2-carPane.laneWidth*carPane.lanes);
        carPane.intersectionPolygon.addPoint(carPane.getWidth()/2+carPane.laneWidth*carPane.lanes, carPane.getHeight()/2-carPane.laneWidth*carPane.lanes);
        carPane.intersectionPolygon.addPoint(carPane.getWidth()/2+carPane.laneWidth*carPane.lanes, carPane.getHeight()/2+carPane.laneWidth*carPane.lanes);
        carPane.intersectionPolygon.addPoint(carPane.getWidth()/2-carPane.laneWidth*carPane.lanes, carPane.getHeight()/2+carPane.laneWidth*carPane.lanes);
    }
    public static void main(String[] args) {
        new CrashMain(); 
    }
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == launch){
            carPane.addCars();
        }else if(e.getSource() == reset){
            carPane.reset();
        }
    }
}
