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
    boolean collided;
    Timer paintTimer;
    Timer movementTimer;
    public CrashPanel(){
        carA = new Car(0,7,20,55);
        carB = new Car(0,10,10,125);
        paintTimer = new Timer(16, this);
        paintTimer.start(); 
        movementTimer = new Timer(100, this);
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
        System.out.println(carA.velocity + carB.velocity);
        System.out.println("------------------");
            System.out.println("Total momentum: " + (carA.velocity*carA.mass + carB.velocity*carB.mass));
            System.out.println("------------------");
    }

    // public void applyCollision(Car carA, Car carB){
    //     debounce = 100;
    //     // Define the coefficient of restitution for the inelastic collision
    //     double e = 0.2;
    
    //     // Masses of the cars
    //     double mA = carA.mass;
    //     double mB = carB.mass;
    
    //     // Initial velocities along the x-axis
    //     double vIAx = carA.velocity * Math.cos(Math.toRadians(carA.angle));
    //     double vIBx = carB.velocity * Math.cos(Math.toRadians(carB.angle));
    
    //     // Final velocities along the x-axis after collision
    //     double vFAx = (mA * vIAx + mB * vIBx - e * mB * (vIAx - vIBx)) / (mA + mB);
    //     double vFBx = vFAx - e * (vIAx - vIBx);
    
    //     // Initial velocities along the y-axis
    //     double vIAy = carA.velocity * Math.sin(Math.toRadians(carA.angle));
    //     double vIBy = carB.velocity * Math.sin(Math.toRadians(carB.angle));
    
    //     // Final velocities along the y-axis after collision
    //     double vFAy = (mA * vIAy + mB * vIBy - e * mB * (vIAy - vIBy)) / (mA + mB);
    //     double vFBy = vFAy - e * (vIAy - vIBy);
    
    //     // Update the velocities and angles of carA
    //     carA.velocity = Math.sqrt(Math.pow(vFAy, 2) + Math.pow(vFAx, 2));
    //     carA.angle = Math.toDegrees(Math.atan2(vFAy, vFAx));
    
    //     // Update the velocities and angles of carB
    //     carB.velocity = Math.sqrt(Math.pow(vFBy, 2) + Math.pow(vFBx, 2));
    //     carB.angle = Math.toDegrees(Math.atan2(vFBy, vFBx));
    
    //     // Print the results

    // }
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
                applyCollision(carA, carB);
                collided = true;
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
