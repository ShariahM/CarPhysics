public class Vector {
    public double angle = 0;
    public double xComponent = 0;
    public double yComponent = 0;
    public double magnitude = 0;

    public Vector(double xComponent, double yComponent) {
        this.xComponent = xComponent;
        this.yComponent = yComponent;
        updateMagnitude();
        updateAngle();
    }

    public void updateAngle() {
        angle = Math.atan2(yComponent, xComponent);
    }

    public void updateMagnitude() {
        magnitude = Math.sqrt(xComponent * xComponent + yComponent * yComponent);
    }

    public void setXComponent(double xComponent) {
        this.xComponent = xComponent;
        updateAngle();
        updateMagnitude();
    }

    public void setYComponent(double yComponent) {
        this.yComponent = yComponent;
        updateAngle();
        updateMagnitude();
    }

    public static Vector multiply(Vector a, Vector b) {
        return new Vector(a.xComponent * b.xComponent, a.yComponent * b.yComponent);
    }

    public static Vector multiply(Vector a, double b) {
        return new Vector(a.xComponent * b, a.yComponent * b);
    }

    public static double dot(Vector v1, Vector v2) {
        return v1.xComponent * v2.xComponent + v1.yComponent * v2.yComponent;
    }

    public static Vector square(Vector a) {
        return new Vector(a.xComponent * a.xComponent, a.yComponent * a.yComponent);
    }

    public static Vector divide(Vector a, double b) {
        return new Vector(a.xComponent / b, a.yComponent / b);
    }

    public static Vector normalize(Vector a) {
        double magnitude = a.magnitude;
        return new Vector(a.xComponent / magnitude, a.yComponent / magnitude);
    }

    public static Vector add(Vector a, Vector b) {
        return new Vector(a.xComponent + b.xComponent, a.yComponent + b.yComponent);
    }

    public static Vector subtract(Vector a, Vector b) {
        return new Vector(a.xComponent - b.xComponent, a.yComponent - b.yComponent);
    }

    public static double cross(Vector a, Vector b) {
        return a.xComponent * b.yComponent - a.yComponent * b.xComponent;
    }

    @Override
    public String toString() {
        return "Vector{" + "xComponent=" + xComponent + ", yComponent=" + yComponent + ", magnitude=" + magnitude + '}';
    }
}