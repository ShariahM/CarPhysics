import java.awt.Color;
import java.awt.Polygon;
public class Car {
    public boolean collided = false;
    double velocity = 0;
    double targetVelocity = 0;
    double angularVelocity = 0;
    double targetAngle = 0;
    double acceleration = 0;
    double mass = Math.random()*250+1000;
    double angle = 0;
    Polygon rect = new Polygon();
    double x = 0;
    double y = 0;
    double height = 50;
    double width = 100;
    Color color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
    public Car(double x, double y, double angle, double velocity){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.targetAngle = angle;
        this.velocity = velocity;
        targetVelocity = velocity;
        updateRectangle();
    }
    public void updateRectangle(){
        double angle = this.angle;
        rect.reset();
        double[] xPoints = new double[4];
        double[] yPoints = new double[4];
        double halfWidth = width/2;
        double halfHeight = height/2;
        
        xPoints[0] = x - halfWidth;
        yPoints[0] = y - halfHeight;
        
        xPoints[1] = x + halfWidth;
        yPoints[1] = y - halfHeight;
        
        xPoints[2] = x + halfWidth;
        yPoints[2] = y + halfHeight;
        
        xPoints[3] = x - halfWidth;
        yPoints[3] = y + halfHeight;
        
        for (int i = 0; i < 4; i++) {
            double xRotated = x + (xPoints[i] - x) * Math.cos(angle) - (yPoints[i] - y) * Math.sin(angle);
            double yRotated = y + (xPoints[i] - x) * Math.sin(angle) + (yPoints[i] - y) * Math.cos(angle);
            rect.addPoint((int) Math.round(xRotated), (int) Math.round(yRotated));
        }
    }
}