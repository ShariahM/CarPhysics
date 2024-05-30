import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.JPanel;
import java.lang.Math;

public class CrashPanel extends JPanel implements ActionListener{
    public Car carA;
    public Car carB;
    int carWidth = 50;
    int carLength = 100;
    Timer paintTimer;
    Timer movementTimer;
    public CrashPanel(){
        carA = new Car();
        carB = new Car(0,10,10,90);
        paintTimer = new Timer(16, this);
        paintTimer.start(); 
        movementTimer = new Timer(5, this);
        movementTimer.start(); 
    }
    public void updateMotion(Car car){
        double rad = Math.toRadians(car.angle);
        car.x += car.velocity*Math.cos(rad);
        car.y -= car.velocity*Math.sin(rad);
        if(outsideXBounds(car)){
            car.x = car.x<0 ? 0: car.x>getWidth()-carLength?getWidth()-carLength:car.x;
            car.angle = Math.toDegrees(Math.PI-rad);
        }
        if(outsideYBounds(car)){
            car.y = car.y<0 ? 0: car.y>getHeight()-carWidth?getHeight()-carWidth:car.y;
            car.angle = Math.toDegrees(Math.PI*2-Math.toRadians(car.angle));
        }
    }
    public boolean isColliding(Car car1, Car car2){
        double topBound = car1.y;
        double bottomBound = car1.y+carWidth;
        double leftBound = car1.x;
        double rightBound = car1.x + carLength;
        return ((car2.y+carWidth >= topBound && car2.y<=bottomBound) && (car2.x+carLength >= leftBound && car2.x<=rightBound));
    }

    public void applyCollision(Car carA, Car carB){
        double e = 0.5;
        double m1 = carA.mass;
        double m2 = carB.mass;
        double v1 = carA.velocity;
        double v2 = carB.velocity;
        
    }

    public boolean outsideXBounds(Car car){
        return car.x <=0 || car.x+carLength >= getWidth();
    }

    public boolean outsideYBounds(Car car){
        return car.y <=0 || car.y+carWidth >= getHeight();
    }
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == movementTimer){ 
            updateMotion(carA);
            updateMotion(carB);
            if(isColliding(carB, carA)){
                System.out.println("colliding");
            }
        }else if(source == paintTimer){
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.red);

        g.drawRect((int)carA.x, (int) carA.y, carLength, carWidth);
        g.drawRect((int)carB.x, (int) carB.y, carLength, carWidth);


        
    }
}
