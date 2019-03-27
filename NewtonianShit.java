package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class NewtonianShit extends Sprite {
    double accelerationX;
    double accelerationY;
    double mass;
    double velocityX;
    double velocityY;
    static double gConst = 10;
    public ArrayList<NewtonianShit> influencers;

    public NewtonianShit(String path, double positionX, double positionY, double mass) {
        super(path, positionX, positionY);
        accelerationX = 0;
        accelerationY = 0;
        this.mass = mass;
        influencers = new ArrayList<>();
    }

    public void update(double deltaTime)
    {
        accelerationX = 0;
        accelerationY = 0;
        double acceleration;
        for (NewtonianShit n:influencers)
        {
            double distance = Math.sqrt(Math.pow(n.positionX - this.positionX, 2) + Math.pow(n.positionY - this.positionY, 2));
            acceleration = (gConst * n.mass)/(Math.pow(distance, 2));
            //if (acceleration >100){acceleration=100; }
            accelerationX += (n.positionX-this.positionX)*acceleration/distance;
            accelerationY += (n.positionY-this.positionY)*acceleration/distance;
        }
        this.velocityX += accelerationX*deltaTime;
        this.velocityY += accelerationY*deltaTime;


        positionX += velocityX * deltaTime;
        positionY += velocityY * deltaTime;

    }
    public void addVelocity(double velocityX, double velocityY)
    {
        this.velocityX += velocityX;
        this.velocityY += velocityY;
    }
    public void orbit(NewtonianShit target, int direction)
    {
        double distance = Math.sqrt(Math.pow(target.positionX - this.positionX, 2) + Math.pow(target.positionY - this.positionY, 2));
        double velocity = direction*Math.sqrt((gConst*target.mass)/distance);
        double angle = Math.toDegrees((Math.atan((this.positionY-target.positionY)/ (this.positionX-target.positionX))));
        this.velocityX =- Math.sin(angle)*velocity;
        this.velocityY =  Math.cos(angle)*velocity;

    }

}
