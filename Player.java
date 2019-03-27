package sample;

import java.util.ArrayList;
import java.util.List;
public class Player extends NewtonianShit {
    public boolean shoot_cooldown = true;
    public boolean hit_cooldown = false;
    public double shoot_timer = 0;
    public double hit_timer = 0;
    public boolean checkEngines = false;
    public int hp;
    public int points=0;
    public Player(String path, double positionX, double positionY, double mass, int hp) {
        super(path, positionX, positionY, mass);
        this.hp = hp;
    }
    public void handlePlayerControl(double mouseX, double mouseY,boolean mouse1, boolean mouse2, ArrayList<String> keyboardInput, List<Sprite> lazerList, Animation exhaust, boolean camLock, double staticX, double staticY)
    {
        float X = 1920/2;
        float Y = 1080/2;
        if (!camLock)
        {
            if(Y>mouseY)
            {
                //this.angle = Math.asin((mouseX-X)/Math.sqrt(Math.pow(mouseX-X,2) + Math.pow(mouseY-Y,2)))/(2*Math.PI)*360;
                this.angle = Math.toDegrees(Math.asin((mouseX-X)/Math.sqrt(Math.pow(mouseX-X,2) + Math.pow(mouseY-Y,2))));
            }
            else
            {
                //this.angle = 180-Math.asin((mouseX-X)/Math.sqrt(Math.pow(mouseX-X,2) + Math.pow(mouseY-Y,2)))/(2*Math.PI)*360;
                this.angle = 180 - Math.toDegrees(Math.asin((mouseX-X)/Math.sqrt(Math.pow(mouseX-X,2) + Math.pow(mouseY-Y,2))));
            }

        }
        else {
            if (this.positionY > mouseY + staticY - Y) {
                //this.angle = Math.asin((mouseX-X)/Math.sqrt(Math.pow(mouseX-X,2) + Math.pow(mouseY-Y,2)))/(2*Math.PI)*360;
                this.angle = Math.toDegrees(Math.asin((mouseX + staticX - X - this.positionX) / Math.sqrt(Math.pow(mouseX + staticX - X - this.positionX, 2) + Math.pow(mouseY + staticY - Y - this.positionY, 2))));
            } else {
                //this.angle = 180-Math.asin((mouseX-X)/Math.sqrt(Math.pow(mouseX-X,2) + Math.pow(mouseY-Y,2)))/(2*Math.PI)*360;
                this.angle = 180 - Math.toDegrees(Math.asin((mouseX + staticX - X - this.positionX) / Math.sqrt(Math.pow(mouseX + staticX - X - this.positionX, 2) + Math.pow(mouseY + staticY - Y - this.positionY, 2))));
            }

        }
        if (mouse2)
        {
            this.addVelocity(Math.sin(Math.toRadians(angle)), -Math.cos(Math.toRadians(angle)));
            //this.addVelocity(((mouseX-X)/(Math.sqrt(Math.pow(mouseX-X, 2)+ Math.pow(mouseY-Y,2)))), ((mouseY-Y)/(Math.sqrt(Math.pow(mouseX-X, 2)+ Math.pow(mouseY-Y,2)))));
            checkEngines = true;
        }
        else
        {
            checkEngines = false;
        }
        if(mouse1)
        {
            if (shoot_cooldown){
                this.shootLazers(lazerList);
                shoot_cooldown = false;
                SoundEffect.SHOOT.play();
            }
        }
        if (keyboardInput.contains("LEFT"))
        {
            this.addVelocity(-1,0);
        }
        if (keyboardInput.contains("RIGHT"))
        {
            this.addVelocity(1,0);
        }
        if (keyboardInput.contains("UP"))
        {
            this.addVelocity(0,-1);
        }
        if (keyboardInput.contains("DOWN"))
        {
            this.addVelocity(0,1);
        }


    }

    public void checkIfPlayerInMap()
    {
        if(this.positionX>1920 || this.positionX <0 || this.positionY > 1080 || this.positionY <0)
        {
            this.positionX = 100;
            this.positionY = 100;
            this.velocityX = 0;
            this.velocityY = 0;
        }

    }
    public void shootLazers(List<Sprite> lazerList)
    {
        lazerList.add(new Sprite("file:lazor.png", this.positionX,this.positionY));
        lazerList.get(lazerList.size()-1).angle = this.angle;
        lazerList.get(lazerList.size()-1).addVelocity(this.velocityX + 100*Math.sin(Math.toRadians(180-angle)),this.velocityY + 100*Math.cos(Math.toRadians(180-angle)));

    }
    public void laserCooldown(double deltaTime)
    {
        if (shoot_cooldown)
        {
            shoot_timer = 0;
        }
        else
        {
            shoot_timer += deltaTime;
            if (shoot_timer >8)
            {
                shoot_cooldown = true;
                shoot_timer = 0;
            }
        }
    }
    public void hitCooldown(double deltaTime)
    {
        if (hit_cooldown)
        {
            hit_timer = 0;
        }
        else
        {
            hit_timer += deltaTime;
            if (hit_timer >10)
            {
                hit_cooldown = true;
                hit_timer = 0;
            }
        }

    }
}
