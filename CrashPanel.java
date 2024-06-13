import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * CrashPanel does all the heavylifting in this application
 * This class handles:
 * Crash detection
 * Motion
 * Traffic lights
 * Painting
 * All cars
 */
public class CrashPanel extends JPanel implements ActionListener{
    
    /*
     * Initialize instance variables
     * Stores information such as lane width, number of lanes, number of cars, traffic light state, etc.
     * Cars are stored in two separate 2D arrays, one for the X axis and one for the Y axis
     * Timers are set up to change the traffic light and calculate motion
     * booleans are declared in order to show message, turn cars gray after crash, etc.
     * Constructor simply initializes the 2D arrays for the cars
     */
    public int laneWidth = 75;
    public int lanes = 2;
    public int trafficLevel = 1;
    public int trafficLight = 1;
    public int speedLimit = 3;
    public ArrayList<Car>[] yTraffic = new ArrayList[lanes*2]; 
    public ArrayList<Car>[] xTraffic = new ArrayList[lanes*2]; 
    public ArrayList<Car> collidedCars = new ArrayList<Car>();
    public Polygon intersectionPolygon = new Polygon();
    public int movementTick = 5;
    public int colourTick = 0;
    public int lightDuration = 2500;
    public Timer movementTimer = new Timer(movementTick, this);
    public Timer trafficTimer = new Timer(lightDuration, this);
    public boolean collided = false;
    public boolean shownMessage = false;

    public CrashPanel(){
        for(int i = 0; i < lanes*2; i++) { 
            yTraffic[i] = new ArrayList<Car>(); 
            xTraffic[i] = new ArrayList<Car>(); 
        } 
    }

    /*
     * randomVelocity method randomizes the velocity of each car
     * a random velocity is returned
     * it ensure that a car will not randomly crash into the car in front of it (in other words, keeps cars slower than the one in front of it)
     */
    public double randomVelocity(ArrayList<Car> lane){
        double speed = Math.random()+speedLimit;
        if (lane.size() == 0){
            return speed;
        }else{
            return Math.min(lane.get(lane.size()-1).targetVelocity, speed);
        }
    }

    /*
     * launch method resets everything and sends a new wave of traffic with a new configuration
     * This method is called in the CrashMain, it takes the values from the user's input
     */
    public void launch(int lanes, int lightDuration, int trafficLevel, int speedLimit){
        trafficTimer.setDelay(lightDuration);
        reset();
        this.lanes = lanes;
        this.lightDuration = lightDuration;
        this.trafficLevel = trafficLevel; 
        addCars();
    }

    /*
     * addCars method loops through the 2D arrays of cars and actually creates new cars to add to them
     * It adds trafficLevel many cars to each lane
     * For example, if trafficLevel is 5, each lanes (northbound, southbound, eastbound, westbound) will have 5 cars
     */
    public void addCars(){
        yTraffic = new ArrayList[lanes*2]; 
        xTraffic = new ArrayList[lanes*2]; 
        for(int i = 0; i < lanes*2; i++) { 
            yTraffic[i] = new ArrayList<Car>(); 
            xTraffic[i] = new ArrayList<Car>(); 
        } 
        for(int j = 0; j<trafficLevel;j++){
            int i = j%(lanes);
            boolean negative = i<lanes;  
                yTraffic[i].add(new Car(getWidth()/2-laneWidth*lanes+laneWidth*i+laneWidth/2, negative?-125*yTraffic[i].size():getHeight()+yTraffic[i].size()*125, Math.toRadians(negative?270:90), randomVelocity(yTraffic[i])));
                xTraffic[i].add(new Car(!negative?-125*xTraffic[i].size():getWidth()+125*xTraffic[i].size(), getHeight()/2-laneWidth*lanes+laneWidth*i+laneWidth/2, Math.toRadians(negative?180:0), randomVelocity(xTraffic[i])));
                i += lanes;
                negative = i<lanes;
                yTraffic[i].add(new Car(getWidth()/2-laneWidth*lanes+laneWidth*i+laneWidth/2, negative?-125*yTraffic[i].size():getHeight()+yTraffic[i].size()*125, Math.toRadians(negative?270:90), randomVelocity(yTraffic[i])));
                xTraffic[i].add(new Car(!negative?-125*xTraffic[i].size():getWidth()+125*xTraffic[i].size(), getHeight()/2-laneWidth*lanes+laneWidth*i+laneWidth/2, Math.toRadians(negative?180:0), randomVelocity(xTraffic[i])));
        }
    }

    /*
     * grayscaleCars method is used to turn cars not involved in collisions gray
     * It loops through both 2D arrays of cars, and calculates the average of the R, G, and B components of the Car's colour
     * Finally, that average value is used as the new R, G, and B components
     */
    public void grayscaleCars(){
        for(int i = 0; i < lanes*2; i++) { 
            for(Car car : yTraffic[i]){
                if(!collidedCars.contains(car)){
                    float average = (car.color.getRGBComponents(null)[0]+car.color.getRGBComponents(null)[1]+car.color.getRGBComponents(null)[2])/3;
                    car.color = new Color(average,average,average);
                }
            }
            for(Car car : xTraffic[i]){
                if(!collidedCars.contains(car)){
                    float average = (car.color.getRGBComponents(null)[0]+car.color.getRGBComponents(null)[1]+car.color.getRGBComponents(null)[2])/3;
                    car.color = new Color(average,average,average);
                }
            }
        } 
    }

    /*
     * This method resets the conditions of the simulation
     * It resets booleans, clears the 2D arrays of cars, and resets the resulting force label
     * Also timers are restarted here
     */
    public void reset(){
        colourTick = 0;
        collided = false;
        shownMessage = false;
        CrashMain.input.averageForce = 0;
        CrashMain.input.averageForceLabel.setText("Average Force: 0N");
        for(int i = 0; i < lanes*2; i++) { 
            yTraffic[i].clear();
            xTraffic[i].clear();
        } 
        movementTimer.restart();
        trafficTimer.restart();
    }

    /*
     * applyMotion simply adds the velocity to the car's coordinates
     * It uses components of vectors to properly increment x and y
     */
    public void applyMotion(Car car){
        car.x += Math.cos(car.angle)*car.velocity;
        car.y -= Math.sin(car.angle)*car.velocity;
    }

    /*
     * yellowLight method is called when a car should not advance into the intersection
     * It makes cars yield during a yellow or red light, and reorders cars as they go off the screen and respawn
     */
    public void yellowLight(Car car, ArrayList<Car> lane){
        if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
            lane.remove(car);
            lane.add(car);
            return;
        }
        int index = lane.indexOf(car);
        boolean eastBound = car.angle == Math.toRadians(0) && car.x<getWidth()/2-laneWidth*lanes-(car.width+25)*index-25 && car.x>getWidth()/2-laneWidth*lanes-(car.width+25)*(index+1);
        boolean westBound = car.angle == Math.toRadians(180) && car.x>getWidth()/2+laneWidth*lanes+(car.width+25)*index+25 && car.x<getWidth()/2+laneWidth*lanes+(car.width+25)*(index+1);
        boolean northBound = car.angle == Math.toRadians(90) && car.y>getHeight()/2+laneWidth*lanes+(car.width+25)*index+25 && car.y<getHeight()/2+laneWidth*lanes+(car.width+25)*(index+1);
        boolean southBound = car.angle == Math.toRadians(270) && car.y<getHeight()/2-laneWidth*lanes-(car.width+25)*index-25 && car.y>getHeight()/2-laneWidth*lanes-(car.width+25)*(index+1);
        if(eastBound || westBound || northBound || southBound){
                car.velocity = 0;
                return;
        }
        car.velocity = car.targetVelocity;
    }

    /*
     * The isColliding method uses built in Java methods to determine if two cars have collided
     * It takes the rectangles formed by the cars polygon (hitbox) and sees if there is an overlap with any other car
     * If there is, return the car which is overlapping
     * Otherwise, return null
     */
    public Car isColliding(Car car){
        for(int i = 0; i<lanes*2;i++){
            for(Car car2 : xTraffic[i]){
                if(car2 == car){
                    continue;
                }
                Rectangle2D intersection = car.rect.getBounds2D().createIntersection(car2.rect.getBounds2D());
                if(!intersection.isEmpty() && getBounds().contains(intersection)){
                    return car2;
                }
            }
            for(Car car2 : yTraffic[i]){
                if(car2 == car){
                    continue;
                }
                Rectangle2D intersection = car.rect.getBounds2D().createIntersection(car2.rect.getBounds2D());
                if(!intersection.isEmpty() && getBounds().contains(intersection)){
                    return car2;
                }
            }
        }
        return null;
    }

    /*
     * applyCollision uses physics to simulate a imperfect inelastic collision between two cars
     * The formulas are derrived from the coefficient of restitution, and the conservation of momentum
     * The impulse-Momentum theorem is used to calculate the impact force of the crash and update the result label
     * Lastly, a message box shows up explaining that the cars which were involved in the crash will flash and explains how to improve the intersection setup 
     */
    public void applyCollision(Car carA, Car carB){
        double e = 0.1;
        double mA = carA.mass;
        double mB = carB.mass;
        double vIAx = carA.velocity * Math.cos(carA.angle);
        double vIBx = carB.velocity * Math.cos(carB.angle);
        double vIAy = carA.velocity * Math.sin(carA.angle);
        double vIBy = carB.velocity * Math.sin(carB.angle);
        
        double vFAx = (mA * vIAx + mB * vIBx - e * mB * (vIAx - vIBx)) / (mA + mB);
        double vFBx = (mA * vIAx + mB * vIBx + e * mA * (vIBx - vIAx)) / (mA + mB);
        
        double vFAy = (mA * vIAy + mB * vIBy - e * mB * (vIAy - vIBy)) / (mA + mB);
        double vFBy = (mA * vIAy + mB * vIBy + e * mA * (vIBy - vIAy)) / (mA + mB);
        
        carA.velocity = Math.sqrt(Math.pow(vFAx, 2) + Math.pow(vFAy, 2));
        carA.angle = Math.atan2(vFAy, vFAx);
    
        carB.velocity = Math.sqrt(Math.pow(vFBx, 2) + Math.pow(vFBy, 2));
        carB.angle = Math.atan2(vFBy, vFBx);
    
        double forceA = Math.abs(mA * (-Math.sqrt(Math.pow(vIAx, 2) + Math.pow(vIAy, 2))) / 0.01);
        double forceB = Math.abs(mB * (-Math.sqrt(Math.pow(vIBx, 2) + Math.pow(vIBy, 2))) / 0.01);
    
        CrashMain.input.averageForce = (int) (CrashMain.input.averageForce + forceA + forceB) / 3;
        CrashMain.input.averageForceLabel.setText("Average Force: " + CrashMain.input.averageForce + "N");

        collidedCars.add(carB);
        collidedCars.add(carA);
        collided = true;
        carA.updateRectangle();
        carB.updateRectangle();
        trafficTimer.stop();
        grayscaleCars();
        repaint();
        if(!shownMessage){
            String message = "Cars directly affected by collisions are flashing.\nCars are shown with final angles.\nMost forms of crashes can be avoided by reducing congestion.\nSome steps to take include:\nMore lanes\nMedium speed limit (40-60kmh)\nLonger yellow light duration.";
            JOptionPane.showMessageDialog(null, message, "Crash Detected!", JOptionPane.INFORMATION_MESSAGE);
            shownMessage = true;
        }
    }

    /*
     * resetCar method is used to recycle cars when they go off the screen.
     * Rather than constantly recreating new cars and destroying old ones, cars are randomized and teleported back (teleportation is done in checkBounds)
     * checkBounds method is used to determine if a car is outside of the screens bounds and the car is resetted accordingly
     * If the car is outside of bounds, it is teleported back to the start of the lane and the car is randomized
     */
    public void resetCar(Car car, ArrayList<Car> lane){
        lane.remove(car);
        double speed = randomVelocity(lane);
        lane.add(car);
        car.velocity = speed;
        car.targetVelocity = speed;
        car.color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
        car.origColor = car.color;
    }
    
    public void checkBounds(Car car, ArrayList<Car> lane){
        if((car.y+car.width<0 && car.angle == Math.toRadians(90))||(car.y-car.height>getHeight() && car.angle == Math.toRadians(270))){
            car.y = (car.y<0 && car.angle == Math.toRadians(90))?getHeight()+(car.width+25)*lane.size():-(car.width+25)*lane.size();
            resetCar(car, lane);
        }else if((car.x+car.width/2<0 && car.angle == Math.toRadians(180)) || (car.x-car.width/2>getWidth() && car.angle == Math.toRadians(0))){
            car.x = (car.x<0 && car.angle == Math.toRadians(180))?getWidth()+(car.width*2+25)*lane.size():-(car.width*2+25)*lane.size();
            resetCar(car, lane);
        }
    }

    /*
     * The actionPerformed methods runs whenever the Traffic light timer or movement timer goes off
     * If the traffic light timer goes off, simply change the traffic light
     * if it's the movement timer, increment colourtick (used to flash collided cars)
     * Then loop through every car and apply motion (update hitboxes too).
     * Apply yellowlight behaviour if necessary (stopping at lights)
     * Check for collisions, and apply collisions if necessary
     */
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == movementTimer){
            colourTick++;
            if(!collided){
                for(int i=0; i<lanes*2; i++){
                    for(int j=0; j<yTraffic[i].size();j++){
                        Car car = yTraffic[i].get(j);
                        car.velocity = car.targetVelocity;
                        if(trafficLight!=1){
                            yellowLight(car, yTraffic[i]);
                        }
                        applyMotion(car);
                        checkBounds(car, yTraffic[i]);
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                    for(int j=0; j<xTraffic[i].size();j++){
                        Car car = xTraffic[i].get(j);
                        car.velocity = car.targetVelocity;
                        if(trafficLight!=3){
                            yellowLight(car, xTraffic[i]);
                        }
                        applyMotion(car);
                        checkBounds(car, xTraffic[i]);
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                }
            }
            repaint();
        }else if (source == trafficTimer){
            trafficLight = trafficLight+1>4?1:trafficLight+1;
        }
    }

    /*
     * The paintComponent method draws the panel
     * The background is drawn (intersection, grass, light bars)
     * Cars are also looped through and drawn
     * If the cars were collided use the colourTick variable to make them flash
     */
    @Override
    protected void paintComponent(Graphics g){
        intersectionPolygon.reset();
        intersectionPolygon.addPoint(getWidth()/2-laneWidth*lanes, getHeight()/2-laneWidth*lanes);
        intersectionPolygon.addPoint(getWidth()/2+laneWidth*lanes, getHeight()/2-laneWidth*lanes);
        intersectionPolygon.addPoint(getWidth()/2+laneWidth*lanes, getHeight()/2+laneWidth*lanes);
        intersectionPolygon.addPoint(getWidth()/2-laneWidth*lanes, getHeight()/2+laneWidth*lanes);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(0,154,23));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.gray);
        g2d.fillRect(getWidth()/2-lanes*laneWidth, 0, lanes*laneWidth*2, getHeight());
        g2d.fillRect(0, getHeight()/2-lanes*laneWidth, getWidth(), lanes*laneWidth*2);
        g2d.setColor(trafficLight==1?Color.green:trafficLight==2?Color.yellow:Color.red);
        g2d.fillRect(getWidth()/2-lanes*laneWidth+20, getHeight()/2-lanes*laneWidth-20, laneWidth*lanes*2-40, 20);
        g2d.fillRect(getWidth()/2-lanes*laneWidth+20, getHeight()/2+lanes*laneWidth+20, laneWidth*lanes*2-40, 20);
        g2d.setColor(trafficLight==3?Color.green:trafficLight==4?Color.yellow:Color.red);
        g2d.fillRect(getWidth()/2-lanes*laneWidth-20, getHeight()/2-lanes*laneWidth+20, 20, laneWidth*lanes*2-40);
        g2d.fillRect(getWidth()/2+lanes*laneWidth, getHeight()/2-lanes*laneWidth+20, 20, laneWidth*lanes*2-40);

        for(int i=0; i<collidedCars.size(); i++){
            Car car = collidedCars.get(i);
            if(i%2 == 0){
                car.color = colourTick%50==0?car.origColor.darker():colourTick%50==25?car.origColor.brighter():car.color;
            }else{
                car.color = colourTick%50==0?car.origColor.brighter():colourTick%50==25?car.origColor.darker():car.color;
            }
        }
        for(int i=0; i<lanes*2; i++){
            for (Car car : xTraffic[i]) {
                g2d.setColor(car.color);
                g2d.fillPolygon(car.rect);
            }
            for (Car car : yTraffic[i]) {
                g2d.setColor(car.color);
                g2d.fillPolygon(car.rect);
            }
        }
        
    }
}