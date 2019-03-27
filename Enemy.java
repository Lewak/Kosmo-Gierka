package sample;

import java.util.List;
import java.util.Random;

public class Enemy extends NewtonianShit {
    private double timer=0;
    private Random generator = new Random();
    private int type;
    private int offshot = generator.nextInt(20)-10;
    private int desynchro = 0;

    public Enemy(String path, double positionX, double positionY, double mass, int type) {
        super(path, positionX, positionY, mass);
        this.type = type;
    }

    public void randomMovement(double deltaTime, Player player)
    {
        timer += deltaTime;
        switch(type)
        {
            case 0:
            if(this.positionY>player.positionY)
            {
                this.angle = Math.asin((player.positionX-this.positionX)/Math.sqrt(Math.pow(player.positionX-this.positionX,2) + Math.pow(player.positionY-this.positionY,2)))/(2*Math.PI)*360;
            }
            else
            {
                this.angle = 180-Math.asin((player.positionX-this.positionX)/Math.sqrt(Math.pow(player.positionX-this.positionX,2) + Math.pow(player.positionY-this.positionY,2)))/(2*Math.PI)*360;
                //this.angle = 180 - Math.asin((mouseX-this.positionX)/Math.sqrt(Math.pow(mouseX-this.positionX,2) + Math.pow(mouseY-this.positionY,2)))/(2*Math.PI)*360;
            }
            if (timer> generator.nextInt(50)+10){
                offshot = generator.nextInt(120)-60;
                timer =0;
            }

            this.addVelocity(((player.positionX-this.positionX)/(Math.sqrt(Math.pow(player.positionX-this.positionX, 2)+ Math.pow(player.positionY-this.positionY,2))))/2, ((player.positionY-this.positionY)/(Math.sqrt(Math.pow(player.positionX-this.positionX, 2)+ Math.pow(player.positionY-this.positionY,2))))/2);
            if (this.velocityX > 20){this.velocityX = 20;}
            if (this.velocityY > 20){this.velocityY = 20;}
            break;
            case 1:
                if(timer>generator.nextInt(50)+10){
                    //this.addVelocity(generator.nextInt(10)-5, generator.nextInt(10)-5);
                    timer = 0;
                    desynchro++;
                }
                break;
            case 2:
                if(this.positionY>player.positionY)
                {
                    this.angle = Math.asin((player.positionX-this.positionX)/Math.sqrt(Math.pow(player.positionX-this.positionX,2) + Math.pow(player.positionY-this.positionY,2)))/(2*Math.PI)*360;
                }
                else
                {
                    this.angle = 180-Math.asin((player.positionX-this.positionX)/Math.sqrt(Math.pow(player.positionX-this.positionX,2) + Math.pow(player.positionY-this.positionY,2)))/(2*Math.PI)*360;
                    //this.angle = 180 - Math.asin((mouseX-this.positionX)/Math.sqrt(Math.pow(mouseX-this.positionX,2) + Math.pow(mouseY-this.positionY,2)))/(2*Math.PI)*360;
                }

                this.addVelocity(((player.positionX-this.positionX)/(Math.sqrt(Math.pow(player.positionX-this.positionX, 2)+ Math.pow(player.positionY-this.positionY,2)))), ((player.positionY-this.positionY)/(Math.sqrt(Math.pow(player.positionX-this.positionX, 2)+ Math.pow(player.positionY-this.positionY,2)))));
                if (this.velocityX > 40){this.velocityX = 40;}
                if (this.velocityY > 40){this.velocityY = 40;}
                break;


        }





    }
    public void attack(List<Sprite> lazerList, Player player)
    {
        double distance = Math.sqrt(Math.pow(player.positionX - this.positionX, 2) + Math.pow(player.positionY - this.positionY, 2));

        switch(type)
        {
            case 0:
                if (timer ==0&&distance <1000&&distance>100)
                {
                    lazerList.add(new Sprite("file:enemylazor.png", this.positionX,this.positionY));
                    lazerList.get(lazerList.size()-1).angle = this.angle;
                    lazerList.get(lazerList.size()-1).addVelocity(this.velocityX + 50*Math.sin(Math.toRadians(180-angle+(offshot/(2*Math.PI)))),this.velocityY + 50*Math.cos(Math.toRadians(180-angle+(offshot/(2*Math.PI)))));
                    SoundEffect.SHOOT.play();
                }
                break;

            case 1:
                if (timer == 0&&desynchro%2==0)
                {
                    for(int n = 0; n<4;n++)
                    {
                        lazerList.add(new Sprite("file:enemylazor.png", this.positionX,this.positionY));
                        lazerList.get(lazerList.size()-1).angle = Math.toRadians(n*90);
                        lazerList.get(lazerList.size()-1).addVelocity(this.velocityX + 50*Math.sin(Math.toRadians(n*90)), this.velocityY + 50*Math.cos(Math.toRadians(n*90)));

                    }
                    SoundEffect.SHOOT.play();

                }
                if (timer == 0&&desynchro%2==1)
                {
                    for(int n = 0; n<4;n++)
                    {
                        lazerList.add(new Sprite("file:enemylazor.png", this.positionX,this.positionY));
                        lazerList.get(lazerList.size()-1).angle = Math.toRadians(n*90+45);
                        lazerList.get(lazerList.size()-1).addVelocity(this.velocityX + 50*Math.sin(Math.toRadians(n*90+45)), this.velocityY + 50*Math.cos(Math.toRadians(n*90+45)));

                    }
                    SoundEffect.SHOOT.play();
                }


        }


    }
}

