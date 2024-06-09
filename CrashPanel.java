import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

import javax.swing.JPanel;

public class CrashPanel extends JPanel implements ActionListener{
    int lanes = 2;
    int laneWidth = 75;
    int xTrafficLevel = 1;
    int yTrafficLevel = 1;
    int trafficLight = 1; // 1 VGreen - 2 VYellow - 3HGreen - 4 HYellow
    ArrayList<Car>[] yTraffic = new ArrayList[lanes*2]; 
    ArrayList<Car>[] xTraffic = new ArrayList[lanes*2]; 
    Polygon intersectionPolygon = new Polygon();
    int movementTick = 5;
    Timer movementTimer = new Timer(movementTick, this);
    Timer trafficTimer = new Timer(2500, this);
    Car carCollidedA;
    Car carCollidedB;


    public CrashPanel(){
        movementTimer.start();
        trafficTimer.start();
        for(int i = 0; i < lanes*2; i++) { 
            yTraffic[i] = new ArrayList<Car>(); 
            xTraffic[i] = new ArrayList<Car>(); 
        } 
    }
    public void addCars(){
        for(int i = 0; i<lanes*2;i++){
            boolean negative = i<lanes;
            for(int j = 0; j<yTrafficLevel; j++){
                yTraffic[i].add(new Car(getWidth()/2-laneWidth*lanes+laneWidth*i+laneWidth/2, negative?-100*yTraffic[i].size():getHeight()+yTraffic[i].size()*100, Math.toRadians(negative?270:90), Math.random()+1));
            }
            for(int j = 0; j<xTrafficLevel; j++){
                xTraffic[i].add(new Car(!negative?-100*xTraffic[i].size():getWidth()+100*xTraffic[i].size(), getHeight()/2-laneWidth*lanes+laneWidth*i+laneWidth/2, Math.toRadians(negative?180:0), Math.random()+1));
            }
        }
    }
    public void reset(){
        carCollidedA = null;
        carCollidedB = null;
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
        if (car.angle != car.targetAngle) {
            double angleDifference = car.targetAngle - car.angle;
            System.out.println(car.angle);
            if (Math.abs(angleDifference) < Math.toRadians(4)) {
                car.angle = car.targetAngle;
            } else {
                if(Math.abs(angleDifference)>Math.toRadians(180)){
                    car.angle-=Math.toRadians(3);
                }else if(Math.abs(angleDifference)<Math.toRadians(180)){
                    car.angle+=Math.toRadians(3);
                }
            }
        }

    }

    public void yellowLight(Car car, int index){
        if(car.angle == Math.toRadians(0)){
            if(car.x<getWidth()/2-laneWidth*lanes-(car.width/2+25)*index-25 && car.x>getWidth()/2-laneWidth*lanes-(car.width/2+25)*(index+1)){
                car.velocity = 0;
            }else if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
                car.velocity = car.targetVelocity*1.5;
            }else{
                car.velocity = car.targetVelocity;
            }
        }
        if(car.angle == Math.toRadians(180)){
            if(car.x>getWidth()/2+laneWidth*lanes+(car.width/2+25)*index+25 && car.x<getWidth()/2+laneWidth*lanes+(car.width/2+25)*(index+1)){
                car.velocity = 0;
            }else if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
                car.velocity = car.targetVelocity*1.5;
            }else{
                car.velocity = car.targetVelocity;
            }
        }
        if(car.angle == Math.toRadians(90)){
            if(car.y>getHeight()/2+laneWidth*lanes+(car.height/2+25)*index+25 && car.y<getHeight()/2+laneWidth*lanes+(car.height/2+25)*(index+1)+25){
                car.velocity = 0;
            }else if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
                car.velocity = car.targetVelocity*1.5;
            }else{
                car.velocity = car.targetVelocity;
            }
        }
        if(car.angle == Math.toRadians(270)){
            if(car.y<getHeight()/2-laneWidth*lanes-(car.height/2+25)*index-25 && car.y>getHeight()/2-laneWidth*lanes-(car.height/2+25)*(index+1)-25){
                car.velocity = 0;
            }else if(!car.rect.getBounds2D().createIntersection(intersectionPolygon.getBounds2D()).isEmpty()){
                car.velocity = car.targetVelocity*1.5;
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
                if(!car.rect.getBounds2D().createIntersection(car2.rect.getBounds2D()).isEmpty()){
                    return car2;
                }
            }
            for(Car car2 : yTraffic[i]){
                if(car2 == car){
                    continue;
                }
                if(!car.rect.getBounds2D().createIntersection(car2.rect.getBounds2D()).isEmpty()){
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
        carA.targetAngle = Math.atan2(vFAy, vFAx);

        carB.velocity = Math.sqrt(Math.pow(vFBy, 2) + Math.pow(vFBx, 2));
        carB.targetAngle = Math.atan2(vFBy, vFBx); 
        carCollidedB = carB;
        carCollidedA = carA;
    }

    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == movementTimer){
            if(carCollidedA != null  && carCollidedB != null){
                System.out.println("Already collided");
                System.out.println(carCollidedA.velocity);
                System.out.println(carCollidedB.velocity);
                applyMotion(carCollidedA);
                applyMotion(carCollidedB);
                carCollidedA.updateRectangle();
                carCollidedB.updateRectangle();
                repaint();
                return;
            }
            for(int i=0; i<lanes*2; i++){
                if(trafficLight == 1){
                    for(int j=0; j<yTraffic[i].size(); j++){
                        Car car = yTraffic[i].get(j);
                        car.velocity = car.targetVelocity;
                        applyMotion(car);
                        if(car.y<0 && car.angle == Math.toRadians(90)){
                            car.y = getHeight()+(car.height*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            yTraffic[i].remove(car);
                            yTraffic[i].add(car);
                        }else if(car.y+car.height/2>getHeight() && car.angle == Math.toRadians(270)){
                            car.y = 0-(car.height*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            yTraffic[i].remove(car);
                            yTraffic[i].add(car);
                        }
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                    for(int j=0; j<xTraffic[i].size(); j++){
                        Car car = xTraffic[i].get(j);
                        yellowLight(car, j);
                        applyMotion(car);
                        if(car.x<0 && car.angle == Math.toRadians(180)){
                            car.x = getWidth()+(car.width*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            xTraffic[i].remove(car);
                            xTraffic[i].add(car);
                        }else if(car.x+car.width/2>getWidth() && car.angle == Math.toRadians(0)){
                            car.x = 0-(car.width*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            xTraffic[i].remove(car);
                            xTraffic[i].add(car);
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
                        yellowLight(car, j);
                        applyMotion(car);
                        if(car.y<0 && car.angle == Math.toRadians(90)){
                            car.y = getHeight()+(car.height*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            yTraffic[i].remove(car);
                            yTraffic[i].add(car);
                        }else if(car.y+car.height/2>getHeight() && car.angle == Math.toRadians(270)){
                            car.y = 0-(car.height*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            yTraffic[i].remove(car);
                            yTraffic[i].add(car);
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
                        if(car.x<0 && car.angle == Math.toRadians(180)){
                            car.x = getWidth()+(car.width*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            xTraffic[i].remove(car);
                            xTraffic[i].add(car);
                        }else if(car.x+car.width/2>getWidth() && car.angle == Math.toRadians(0)){
                            car.x = 0-(car.width*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            xTraffic[i].remove(car);
                            xTraffic[i].add(car);
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
                        yellowLight(car, j);
                        applyMotion(car);
                        if(car.y<0 && car.angle == Math.toRadians(90)){
                            car.y = getHeight()+(car.height*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            yTraffic[i].remove(car);
                            yTraffic[i].add(car);
                        }else if(car.y+car.height/2>getHeight() && car.angle == Math.toRadians(270)){
                            car.y = 0-(car.height*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            yTraffic[i].remove(car);
                            yTraffic[i].add(car);
                        }
                        car.updateRectangle();
                        Car colliding = isColliding(car);
                        if(colliding != null){
                            applyCollision(car, colliding);
                        }
                    }
                    for(int j=0; j<xTraffic[i].size(); j++){
                        Car car = xTraffic[i].get(j);
                        yellowLight(car, j);
                        applyMotion(car);
                        if(car.x<0 && car.angle == Math.toRadians(180)){
                            car.x = getWidth()+(car.width*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            xTraffic[i].remove(car);
                            xTraffic[i].add(car);
                        }else if(car.x+car.width/2>getWidth() && car.angle == Math.toRadians(0)){
                            car.x = 0-(car.width*2+25)*j;
                            car.velocity = Math.random()+1;
                            car.targetVelocity = car.velocity;
                            xTraffic[i].remove(car);
                            xTraffic[i].add(car);
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
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.green);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.gray);
        g2d.fillRect(getWidth()/2-lanes*laneWidth, 0, lanes*laneWidth*2, getHeight());
        g2d.fillRect(0, getHeight()/2-lanes*laneWidth, getWidth(), lanes*laneWidth*2);
        g2d.setColor(Color.red);
        for(int i=0; i<lanes*2; i++){
            for (Car car : xTraffic[i]) {
                g2d.fillPolygon(car.rect);
            }
            for (Car car : yTraffic[i]) {
                g2d.fillPolygon(car.rect);
            }
        }
    }
}

