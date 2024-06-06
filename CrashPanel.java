import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

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
        carA = new Car(50,50,8,270, 50, 100);
        carB = new Car(50,50,10,0, 50, 100);
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
        // if (car.angle != car.targetAngle) {
        //     double angleDifference = car.targetAngle - car.angle;
        //     System.out.println(angleDifference);
        //     if (Math.abs(angleDifference) < 3) {
        //         car.angle = car.targetAngle; 
        //     } else {
        //         if(Math.abs(angleDifference)>180){
        //             car.angle+=3;
        //         }else if(Math.abs(angleDifference)<180){
        //             car.angle-=3;
        //         }
        //     }
        // }
        car.updatePoly();
        if(isColliding(carA, carB) && debounce<0){
            car.x = oldX;
            car.y = oldY;
            applyCollision(carA, carB);
        }
        
    }
    public boolean isColliding(Car car1, Car car2) {
        Rectangle2D car1Area = car1.rect.getBounds2D();
        Rectangle2D car2Area = car2.rect.getBounds2D();
        Rectangle2D intersection = car1Area.createIntersection(car2Area);
        return !intersection.isEmpty();
    }

    public void applyCollision(Car carA, Car carB){
        Area overlap = new Area(carA.rect);
        overlap.intersect(new Area(carB.rect));
        System.out.println(overlap.getBounds2D().getCenterX());
        System.out.println(overlap.getBounds2D().getCenterY());

        double e = 0.5;


        // debounce = 200;
        // double e = 0.5;
        // double mA = carA.mass;
        // double mB = carB.mass;
        // double vIAx = carA.velocity*Math.cos(Math.toRadians(carA.angle));
        // double vIBx = carB.velocity*Math.cos(Math.toRadians(carB.angle));
        // double vFAx = (mA*vIAx + mB*vIBx - e*mB*(vIAx - vIBx))/(mA+mB);
        // double vFBx = vFAx + e*(vIAx - vIBx);

        // double vIAy = carA.velocity*Math.sin(Math.toRadians(carA.angle));
        // double vIBy = carB.velocity*Math.sin(Math.toRadians(carB.angle));
        // double vFAy = (mA*vIAy + mB*vIBy - e*mB*(vIAy - vIBy))/(mA+mB);
        // double vFBy = vFAy + e*(vIAy - vIBy);

        // carA.velocity = Math.sqrt(Math.pow(vFAy, 2) + Math.pow(vFAx, 2));
        // carA.angle = Math.toDegrees(Math.atan2(vFAy, vFAx));

        // carB.velocity = Math.sqrt(Math.pow(vFBy, 2) + Math.pow(vFBx, 2));
        // carB.angle = Math.toDegrees(Math.atan2(vFBy, vFBx)); 
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
        
        g2d.fillPolygon(carA.rect);
        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(carB.rect);  
    }
}
