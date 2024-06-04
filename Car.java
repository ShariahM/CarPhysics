class Car {
    public double initialVelocity;
    public double velocity;
    public double mass;
    public double angle;
    public double x;
    public double y;
    public int width;
    public int length;

    public Car(double initialVelocity, double velocity, double mass, double angle, int width, int length){
        this.initialVelocity = initialVelocity;
        this.velocity = velocity;
        this.mass = mass;
        this.angle = angle;
        this.width = width;
        this.length = length;
        x = 0;
        y = 0;
    }
    public Car(){
        initialVelocity = 0;
        velocity = 10;
        mass = 0;
        angle = 45;
        x = 0;
        y = 0;
    }
}