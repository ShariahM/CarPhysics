public class Vector {
    public double angle = 0;
    public double xComponent = 0;
    public double yComponent = 0;
    public double magnitude = 0;
    public Vector(double xComponent, double yComponent, double angle){
        this.xComponent = xComponent;
        this.yComponent = yComponent;
        this.magnitude = Math.sqrt(Math.pow(xComponent, 2)+Math.pow(yComponent, 2));
        this.angle = angle;
    }
    public Vector(double xComponent, double yComponent){
        this.xComponent = xComponent;
        this.yComponent = yComponent;
        this.magnitude = Math.sqrt(Math.pow(xComponent, 2)+Math.pow(yComponent, 2));
        this.angle = Math.atan2(yComponent, xComponent);
    }

}
