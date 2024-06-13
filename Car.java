import java.awt.Color;
import java.awt.Polygon;

/*
 * This car class stores information about each car in the simulation
 * Both physics based info (velocity, mass, angle, etc.) are stored and visual info (color, polygon)
 */
public class Car {
    /*
     * Declare instance variables that contain extensive information about the car
     * Constructor simply takes in a value for the coordinates, angle, and velocity and stores them
     * After that, the constructor calls updateRectangle to update the hitbox of the car
     */
    public double velocity = 0;
    public double targetVelocity = 0;
    public double mass = Math.random()*250+1000;
    public double angle = 0;
    public Polygon rect = new Polygon();
    public double x = 0;
    public double y = 0;
    public double height = 50;
    public double width = 100;
    public Color color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
    public Color origColor = color;

    public Car(double x, double y, double angle, double velocity){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.velocity = velocity;
        targetVelocity = velocity;
        updateRectangle();
    }

    /*
     * updateRectangle method updates the hitbox of the car (Polygon)
     * A simple mathematical transformation is used
     * First, render points of a rectangle assuming an angle of 0 degrees and that (x,y) is the center of the rectangle
     * Next, use trig functions to transform the points with rotation
     * Repeat the steps for each point, and now the rectangle is rotated properly.
     */
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