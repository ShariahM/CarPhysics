import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Timer;

import javax.swing.JPanel;

public class CrashPanel extends JPanel implements ActionListener{
    int laneWidth = 75;
    int lanes = 2;
    int trafficLevel = 1;
    int trafficLight = 1; // 1 VGreen - 2 VYellow - 3HGreen - 4 HYellow
    int speedLimit = 3;
    ArrayList<Car>[] yTraffic = new ArrayList[lanes*2]; 
    ArrayList<Car>[] xTraffic = new ArrayList[lanes*2]; 
    Polygon intersectionPolygon = new Polygon();
    int movementTick = 5;
    int lightDuration = 2500;
    Timer movementTimer = new Timer(movementTick, this);
    Timer trafficTimer = new Timer(lightDuration, this);
    boolean collided = false;


    public CrashPanel(){
        movementTimer.start();
        trafficTimer.start();
        for(int i = 0; i < lanes*2; i++) { 
            yTraffic[i] = new ArrayList<Car>(); 
            xTraffic[i] = new ArrayList<Car>(); 
        } 
    }

    public double randomVelocity(ArrayList<Car> lane){
        double speed = Math.random()+speedLimit;
        if (lane.size() == 0){
            return speed;
        }else{
            return Math.min(lane.get(lane.size()-1).targetVelocity, speed);
        }
    }

    public void launch(int lanes, int lightDuration, int trafficLevel, int speedLimit){
        reset();
        this.lanes = lanes;
        this.lightDuration = lightDuration;
        this.trafficLevel = trafficLevel; 
        this.speedLimit = speedLimit;
        addCars();
    }

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
    public void reset(){
        collided = false;
        for(int i = 0; i < lanes*2; i++) { 
            yTraffic[i].clear();
            xTraffic[i].clear();
        } 
        movementTimer.restart();
        trafficTimer.restart();
    }
    public void applyMotion(Car car){
        car.angle = car.angle%(Math.toRadians(360));
        car.velocity += car.acceleration;
        car.x += Math.cos(car.angle)*car.velocity;
        car.y -= Math.sin(car.angle)*car.velocity;
    }

    public void yellowLight(Car car, ArrayList<Car> lane){
        int index = lane.indexOf(car);

        if(car.angle == Math.toRadians(0)){
            if(car.x<getWidth()/2-laneWidth*lanes-(car.width+25)*index-25 && car.x>getWidth()/2-laneWidth*lanes-(car.width+25)*(index+1)){
                car.velocity = 0;
            }else if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
                lane.remove(car);
                lane.add(car);
                //car.velocity = car.targetVelocity*1.25;
            }else{
                car.velocity = car.targetVelocity;
            }
        }
        if(car.angle == Math.toRadians(180)){
            if(car.x>getWidth()/2+laneWidth*lanes+(car.width+25)*index+25 && car.x<getWidth()/2+laneWidth*lanes+(car.width+25)*(index+1)){
                car.velocity = 0;
            }else if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
                lane.remove(car);
                lane.add(car);
                //car.velocity = car.targetVelocity*1.25;
            }else{
                car.velocity = car.targetVelocity;
            }
        }
        if(car.angle == Math.toRadians(90)){
            if(car.y>getHeight()/2+laneWidth*lanes+(car.width+25)*index+25 && car.y<getHeight()/2+laneWidth*lanes+(car.width+25)*(index+1)){
                car.velocity = 0;
            }else if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
                lane.remove(car);
                lane.add(car);
                //car.velocity = car.targetVelocity*1.25;
            }else{
                car.velocity = car.targetVelocity;
            }
        }
        if(car.angle == Math.toRadians(270)){
            if(car.y<getHeight()/2-laneWidth*lanes-(car.width+25)*index-25 && car.y>getHeight()/2-laneWidth*lanes-(car.width+25)*(index+1)){
                car.velocity = 0;
            }else if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
                lane.remove(car);
                lane.add(car);
                //car.velocity = car.targetVelocity*1.25;
            }else{
                car.velocity = car.targetVelocity;
            }
        }
    }

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

    public void applyCollision(Car carA, Car carB){
        double e = 0.5;
        double mA = carA.mass;
        double mB = carB.mass;
        double vIAx = carA.velocity*Math.cos(carA.angle);
        double vIBx = carB.velocity*Math.cos(carB.angle);
        double vFAx = (mA*vIAx + mB*vIBx - e*mB*(vIAx - vIBx))/(mA+mB);
        double vFBx = vFAx + e*(vIAx - vIBx);

        double vIAy = carA.velocity*Math.sin(carA.angle);
        double vIBy = carB.velocity*Math.sin(carB.angle);
        double vFAy = (mA*vIAy + mB*vIBy - e*mB*(vIAy - vIBy))/(mA+mB);
        double vFBy = vFAy + e*(vIAy - vIBy);

        carA.velocity = Math.sqrt(Math.pow(vFAy, 2) + Math.pow(vFAx, 2));
        carA.angle = Math.atan2(vFAy, vFAx);

        carB.velocity = Math.sqrt(Math.pow(vFBy, 2) + Math.pow(vFBx, 2));
        carB.angle = Math.atan2(vFBy, vFBx); 
        carA.collided = true;
        carB.collided = true;
        collided = true;
        carA.updateRectangle();
        carB.updateRectangle();
        movementTimer.stop();
        trafficTimer.stop();
        repaint();
    }


    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == movementTimer){
            for(int i=0; i<lanes*2; i++){
                if(trafficLight == 1){
                    for(int j=0; j<yTraffic[i].size(); j++){
                        Car car = yTraffic[i].get(j);
                        car.velocity = car.targetVelocity;
                        applyMotion(car);
                        if((car.y+car.width<0 && car.angle == Math.toRadians(90))||(car.y-car.height>getHeight() && car.angle == Math.toRadians(270))){
                            System.out.println("reset");
                            car.y = (car.y<0 && car.angle == Math.toRadians(90))?getHeight()+(car.width+25)*yTraffic[i].size():-(car.width+25)*yTraffic[i].size();
                            yTraffic[i].remove(car);
                            double speed = randomVelocity(yTraffic[i]);
                            yTraffic[i].add(car);
                            car.velocity = speed;
                            car.targetVelocity = speed;
                            car.color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
                        }
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                    for(int j=0; j<xTraffic[i].size(); j++){
                        Car car = xTraffic[i].get(j);
                        yellowLight(car, xTraffic[i]);
                        applyMotion(car);
                        if((car.x+car.width/2<0 && car.angle == Math.toRadians(180)) || (car.x-car.width/2>getWidth() && car.angle == Math.toRadians(0))){
                            car.x = (car.x<0 && car.angle == Math.toRadians(180))?getWidth()+(car.width*2+25)*xTraffic[i].size():-(car.width*2+25)*xTraffic[i].size();
                            xTraffic[i].remove(car);
                            double speed = randomVelocity(yTraffic[i]);
                            xTraffic[i].add(car);
                            car.velocity = speed;
                            car.targetVelocity = speed;
                            car.color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
                        }
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                }else
                if(trafficLight == 3){
                    for(int j=0; j<yTraffic[i].size(); j++){
                        Car car = yTraffic[i].get(j);
                        yellowLight(car, yTraffic[i]);
                        applyMotion(car);
                        if((car.y+car.width<0 && car.angle == Math.toRadians(90))||(car.y-car.height>getHeight() && car.angle == Math.toRadians(270))){
                            System.out.println("reset");
                            car.y = (car.y<0 && car.angle == Math.toRadians(90))?getHeight()+(car.width+25)*yTraffic[i].size():-(car.width+25)*yTraffic[i].size();
                            yTraffic[i].remove(car);
                            double speed = randomVelocity(yTraffic[i]);
                            yTraffic[i].add(car);
                            car.velocity = speed;
                            car.targetVelocity = speed;
                            car.color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
                            
                            
                        }
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                    for(int j=0; j<xTraffic[i].size(); j++){
                        Car car = xTraffic[i].get(j);
                        car.velocity = car.targetVelocity;
                        applyMotion(car);
                        if((car.x+car.width/2<0 && car.angle == Math.toRadians(180)) || (car.x-car.width/2>getWidth() && car.angle == Math.toRadians(0))){
                            car.x = (car.x<0 && car.angle == Math.toRadians(180))?getWidth()+(car.width*2+25)*xTraffic[i].size():-(car.width*2+25)*xTraffic[i].size();
                            xTraffic[i].remove(car);
                            double speed = randomVelocity(yTraffic[i]);
                            xTraffic[i].add(car);
                            car.velocity = speed;
                            car.targetVelocity = speed;
                            car.color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
                        }
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                }else
                if(trafficLight == 2 || trafficLight == 4){
                    for(int j=0; j<yTraffic[i].size(); j++){
                        Car car = yTraffic[i].get(j);
                        yellowLight(car, yTraffic[i]);
                        applyMotion(car);
                        if((car.y+car.width<0 && car.angle == Math.toRadians(90))||(car.y-car.height>getHeight() && car.angle == Math.toRadians(270))){
                            System.out.println("reset");
                            car.y = (car.y<0 && car.angle == Math.toRadians(90))?getHeight()+(car.width+25)*yTraffic[i].size():-(car.width+25)*yTraffic[i].size();
                            yTraffic[i].remove(car);
                            double speed = randomVelocity(yTraffic[i]);
                            yTraffic[i].add(car);
                            car.velocity = speed;
                            car.targetVelocity = speed;
                            car.color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
                        }
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                    for(int j=0; j<xTraffic[i].size(); j++){
                        Car car = xTraffic[i].get(j);
                        yellowLight(car, xTraffic[i]);
                        applyMotion(car);
                        if((car.x+car.width/2<0 && car.angle == Math.toRadians(180)) || (car.x-car.width/2>getWidth() && car.angle == Math.toRadians(0))){
                            car.x = (car.x<0 && car.angle == Math.toRadians(180))?getWidth()+(car.width*2+25)*xTraffic[i].size():-(car.width*2+25)*xTraffic[i].size();
                            xTraffic[i].remove(car);
                            double speed = randomVelocity(yTraffic[i]);
                            xTraffic[i].add(car);
                            car.velocity = speed;
                            car.targetVelocity = speed;
                            car.color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
                        }
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
            System.out.println(trafficLight);
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        // 1 VGreen - 2 VYellow - 3HGreen - 4 HYellow
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
        for(int i=0; i<lanes*2; i++){
            for (Car car : xTraffic[i]) {
                if(!car.collided && collided){
                        float average = (car.color.getRGBComponents(null)[0]+car.color.getRGBComponents(null)[1]+car.color.getRGBComponents(null)[2])/3;
                        car.color = new Color(average,average,average);
                }   
                g2d.setColor(car.color);
                g2d.fillPolygon(car.rect);
            }
            for (Car car : yTraffic[i]) {
                if(!car.collided && collided){
                    float average = (car.color.getRGBComponents(null)[0]+car.color.getRGBComponents(null)[1]+car.color.getRGBComponents(null)[2])/3;
                    car.color = new Color(average,average,average);
                }     
                g2d.setColor(car.color);
                g2d.fillPolygon(car.rect);
            }
        }
    }
}