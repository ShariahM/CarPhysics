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
    int debounce = 0;
    int movementTickRate = 5;
    Timer paintTimer;
    Timer movementTimer;
    public CrashPanel(){
        carA = new Car(500,500,8,270, 50, 100);
        carB = new Car(600,552,10,0, 50, 100);
        //paintTimer = new Timer(16, this);
        //paintTimer.start(); 
        movementTimer = new Timer(movementTickRate, this);
    }
    public void updateMotion(Car car){
        double tickRateInSeconds = movementTickRate / 1000.0;
        double rad = Math.toRadians(car.angle);
        double distancePerTick = car.velocity * tickRateInSeconds;
        double oldX = car.x;
        double oldY = car.y;
    
        car.x += distancePerTick * Math.cos(rad);
        car.y -= distancePerTick * Math.sin(rad);
        if(isColliding(carA, carB)){
            car.x = oldX;
            car.y = oldY;
            applyCollision(carA, carB);
        }
        // if(outsideXBounds(car)){
        //     car.x = car.x - car.length/2 <=0 ? car.length/2: car.x + car.length/2 >= getWidth()?getWidth()-car.length/2:car.x;
        //     car.angle = Math.toDegrees(Math.PI-rad);
        // }
        // if(outsideYBounds(car)){
        //     car.y = car.y - car.width/2 <=0 ? car.width/2: car.y + car.width/2 >= getHeight()?getHeight()-car.width/2:car.y;
        //     car.angle = Math.toDegrees(Math.PI*2-Math.toRadians(car.angle));
        // }
    }
    public boolean isColliding(Car car1, Car car2) {
        double car1HalfLength = car1.width / 2.0; 
        double car1HalfWidth = car1.length / 2.0;  
        double car2HalfLength = car2.width / 2.0;  
        double car2HalfWidth = car2.length / 2.0; 
    
        double car1Left = car1.x - car1HalfWidth;
        double car1Right = car1.x + car1HalfWidth;
        double car1Top = car1.y - car1HalfLength;
        double car1Bottom = car1.y + car1HalfLength;
    
        double car2Left = car2.x - car2HalfWidth;
        double car2Right = car2.x + car2HalfWidth;
        double car2Top = car2.y - car2HalfLength;
        double car2Bottom = car2.y + car2HalfLength;
    
        boolean isCollidingHorizontally = car1Left < car2Right && car1Right > car2Left;
        boolean isCollidingVertically = car1Top < car2Bottom && car1Bottom > car2Top;
    
        return isCollidingHorizontally && isCollidingVertically;
    }

    public void applyCollision(Car carA, Car carB){
        debounce = 0;
        double e = 0.5;
        double mA = carA.mass;
        double mB = carB.mass;
        double vIAx = carA.velocity*Math.cos(Math.toRadians(carA.angle));
        double vIBx = carB.velocity*Math.cos(Math.toRadians(carB.angle));
        double vFAx = (mA*vIAx + mB*vIBx - e*mB*(vIAx - vIBx))/(mA+mB);
        double vFBx = vFAx + e*(vIAx - vIBx);

        double vIAy = carA.velocity*Math.sin(Math.toRadians(carA.angle));
        double vIBy = carB.velocity*Math.sin(Math.toRadians(carB.angle));
        double vFAy = (mA*vIAy + mB*vIBy - e*mB*(vIAy - vIBy))/(mA+mB);
        double vFBy = vFAy + e*(vIAy - vIBy);

        carA.velocity = Math.sqrt(Math.pow(vFAy, 2) + Math.pow(vFAx, 2));
        carA.angle = Math.toDegrees(Math.atan2(vFAy, vFAx));

        carB.velocity = Math.sqrt(Math.pow(vFBy, 2) + Math.pow(vFBx, 2));
        carB.angle = Math.toDegrees(Math.atan2(vFBy, vFBx)); 
        System.out.println(carA.velocity);
        System.out.println(carB.velocity);
        System.out.println("------------------");
            System.out.println("Total momentum: " + (carA.velocity*carA.mass + carB.velocity*carB.mass));
            System.out.println("------------------");
    }

    public boolean outsideXBounds(Car car){
        return car.x - car.length/2 <=0 || car.x + car.length/2 >= getWidth();
    }

    public boolean outsideYBounds(Car car){
        return car.y - car.width/2 <=0 || car.y + car.width/2 >= getHeight();
    }
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == movementTimer){ 
            updateMotion(carA);
            updateMotion(carB);
            if(carA.x > 1000 && carB.x>1000){
                movementTimer.stop();
                System.out.println("stopping");
            }
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.red);


        g.drawRect((int)(carA.x - carA.length/2), (int)(carA.y - carA.width/2), (int)(carA.length), (int)(carA.width));
        g.drawRect((int)(carB.x - carB.length/2), (int)(carB.y - carB.width/2), (int)(carB.length), (int)(carB.width));


        
    }
}
