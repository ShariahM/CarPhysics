import java.awt.Polygon;

class Car {
    public Polygon rect;
    public double initialVelocity;
    public double velocity;
    public double mass;
    public double angle;
    public double x;
    public double y;
    public int width;
    public int length;
    public double angularVelocity = 0;
    public double targetAngle;

    public Car(double initialVelocity, double velocity, double mass, double angle, int width, int length){
        this.initialVelocity = initialVelocity;
        this.velocity = velocity;
        this.mass = mass;
        this.angle = angle;
        targetAngle = angle;
        this.width = width;
        this.length = length;
        x = 0;
        y = 0;
        updatePoly();
    }
    public Car(){
        initialVelocity = 0;
        velocity = 10;
        mass = 0;
        angle = 45;
        x = 0;
        y = 0;
    }
    public void updatePoly(){
        double radians = Math.toRadians(180-angle);
        double[] xPoints = new double[4];
        double[] yPoints = new double[4];
        double halfWidth = length/2;
        double halfHeight = width/2;
        
        xPoints[0] = x - halfWidth;
        yPoints[0] = y - halfHeight;
        
        xPoints[1] = x + halfWidth;
        yPoints[1] = y - halfHeight;
        
        xPoints[2] = x + halfWidth;
        yPoints[2] = y + halfHeight;
        
        xPoints[3] = x - halfWidth;
        yPoints[3] = y + halfHeight;
        
        int[] xRotatedPoints = new int[4];
        int[] yRotatedPoints = new int[4];
        
        for (int i = 0; i < 4; i++) {
            double xRotated = x + (xPoints[i] - x) * Math.cos(radians) - (yPoints[i] - y) * Math.sin(radians);
            double yRotated = y + (xPoints[i] - x) * Math.sin(radians) + (yPoints[i] - y) * Math.cos(radians);
            xRotatedPoints[i] = (int) Math.round(xRotated);
            yRotatedPoints[i] = (int) Math.round(yRotated);
        }
        rect = new Polygon(xRotatedPoints, yRotatedPoints, 4);
    }
}