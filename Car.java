import java.util.ArrayList;

public class Car {
    ArrayList<Vector> translationalForces = new ArrayList<Vector>();
    public static void main(String[] args) {
        Car c = new Car();
        Vector v = new Vector(-3, -4);
        c.addTranslationalForce(v);
        c.getNetForce();

    }
    public void addTranslationalForce(Vector force){
        translationalForces.add(force);
    }
    public Vector getNetForce(){
        double finalXComponent = 0;
        double finalYComponent = 0;
        for (Vector vector : translationalForces) {
            finalXComponent+=vector.xComponent;
            finalYComponent+=vector.yComponent;
        }
        double angle = Math.atan2(finalYComponent, finalXComponent);
        System.out.println(Math.toDegrees(angle));
        return new Vector(finalXComponent, finalYComponent, angle);
    }
}
