import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.JPanel;
import java.lang.Math;
import java.util.Arrays;

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
        carA = new Car(500,1000,8,270, 50, 100);
        carB = new Car(600,1552,10,0, 50, 100);
        //paintTimer = new Timer(16, this);
        //paintTimer.start(); 
        movementTimer = new Timer(movementTickRate, this);
    }
    public void updateMotion(Car car){
        car.angle %= 360;
        debounce--;
        double tickRateInSeconds = movementTickRate / 1000.0;
        double rad = Math.toRadians(car.angle);
        double distancePerTick = car.velocity * tickRateInSeconds;
        double oldX = car.x;
        double oldY = car.y;
    
        car.x += distancePerTick * Math.cos(rad);
        car.y -= distancePerTick * Math.sin(rad);
        if (car.angle != car.targetAngle) {
            double angleDifference = car.targetAngle - car.angle;
            System.out.println(angleDifference);
            if (Math.abs(angleDifference) < 3) {
                car.angle = car.targetAngle; // Directly set to target if close enough
            } else {
                if(Math.abs(angleDifference)>180){
                    car.angle+=3;
                }else if(Math.abs(angleDifference)<180){
                    car.angle-=3;
                }
            }
        }
        if(isColliding(carA, carB) && debounce<0){
            car.x = oldX;
            car.y = oldY;
            applyCollision(carA, carB);
        }

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
        debounce = 200;
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
        carA.targetAngle = Math.toDegrees(Math.atan2(vFAy, vFAx));

        carB.velocity = Math.sqrt(Math.pow(vFBy, 2) + Math.pow(vFBx, 2));
        carB.targetAngle = Math.toDegrees(Math.atan2(vFBy, vFBx)); 
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
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        g2d.setColor(Color.red);
        double radians = Math.toRadians(180 - carA.angle);
        
        double centerX = carA.x;
        double centerY = carA.y;
        // Calculate half dimensions
        double halfWidth = carA.length / 2.0;
        double halfHeight = carA.width / 2.0;
        
        // Calculate the coordinates of the corners relative to the center
        double[] xPoints = new double[4];
        double[] yPoints = new double[4];
        
        xPoints[0] = centerX - halfWidth;
        yPoints[0] = centerY - halfHeight;
        
        xPoints[1] = centerX + halfWidth;
        yPoints[1] = centerY - halfHeight;
        
        xPoints[2] = centerX + halfWidth;
        yPoints[2] = centerY + halfHeight;
        
        xPoints[3] = centerX - halfWidth;
        yPoints[3] = centerY + halfHeight;
        
        // Rotate the coordinates around the center
        int[] xRotatedPoints = new int[4];
        int[] yRotatedPoints = new int[4];
        
        for (int i = 0; i < 4; i++) {
            double xRotated = centerX + (xPoints[i] - centerX) * Math.cos(radians) - (yPoints[i] - centerY) * Math.sin(radians);
            double yRotated = centerY + (xPoints[i] - centerX) * Math.sin(radians) + (yPoints[i] - centerY) * Math.cos(radians);
            xRotatedPoints[i] = (int) Math.round(xRotated);
            yRotatedPoints[i] = (int) Math.round(yRotated);
        }
        Polygon polygon = new Polygon(xRotatedPoints, yRotatedPoints, 4);
        g2d.fillPolygon(polygon);
        centerX = carB.x;
        centerY = carB.y;
        radians = Math.toRadians(180 - carB.angle);
        xPoints = new double[4];
        yPoints = new double[4];
        
        xPoints[0] = centerX - halfWidth;
        yPoints[0] = centerY - halfHeight;
        
        xPoints[1] = centerX + halfWidth;
        yPoints[1] = centerY - halfHeight;
        
        xPoints[2] = centerX + halfWidth;
        yPoints[2] = centerY + halfHeight;
        
        xPoints[3] = centerX - halfWidth;
        yPoints[3] = centerY + halfHeight;
        
        // Rotate the coordinates around the center
        xRotatedPoints = new int[4];
        yRotatedPoints = new int[4];
        
        for (int i = 0; i < 4; i++) {
            double xRotated = centerX + (xPoints[i] - centerX) * Math.cos(radians) - (yPoints[i] - centerY) * Math.sin(radians);
            double yRotated = centerY + (xPoints[i] - centerX) * Math.sin(radians) + (yPoints[i] - centerY) * Math.cos(radians);
            xRotatedPoints[i] = (int) Math.round(xRotated);
            yRotatedPoints[i] = (int) Math.round(yRotated);
        }
        polygon = new Polygon(xRotatedPoints, yRotatedPoints, 4);
        g2d.fillPolygon(polygon);


        
    }
}
