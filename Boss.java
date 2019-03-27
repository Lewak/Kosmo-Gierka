package sample;

import java.util.List;
import java.util.Random;

public class Boss extends NewtonianShit {
    public int phase;
    int hp;
    double timer;
    double timer1;
    public boolean trigger;
    public boolean isDead = false;
    Random generator = new Random();

    public Boss(String path, double positionX, double positionY, double mass, int hp, boolean trigger) {
        super(path, positionX, positionY, mass);
        this.hp = hp;
        this.trigger = trigger;
    }

    public void phaseCheck() {
        if(trigger)
        {
            if(this.hp>20)
            {
                this.phase = 0;
            }
            else if(this.hp>10)
            {
                this.phase = 1;
            }
            else if(this.hp>0)
            {
                this.phase = 2;
            }
            else
            {
                this.phase = 3;
            }
        }
        else{
            //this.hp = 30;
        }
    }

    public void phaseWork(List<Animation> TeleportList, List<Sprite> EnemyLazorList, List<Enemy> Enemy, double deltaTime, Player player, List<String> EnemyNames) {
        if (trigger) {
            timer += deltaTime;
            switch (phase) {
                case 0:
                    if (timer > 50) {
                        int tempmass = 0;
                        TeleportList.add(new Animation("teleport", 8, 0, this.positionX, this.positionY));
                        while (tempmass == 0) {
                            tempmass = generator.nextInt(5000) - 2500;
                        }
                        this.mass = tempmass;
                        if (Enemy.size() == 0) {
                            SpawnMore(2, 5, Enemy, TeleportList, player, EnemyNames);
                        }
                        SoundEffect.GROWL.play();
                        timer = 0;
                    }
                    break;
                case 1:
                    timer1+=deltaTime;
                    if (timer1>1){
                        EnemyLazorList.add(new Sprite("file:enemylazor.png", this.positionX, this.positionY ));
                        int tempangle = generator.nextInt(360);
                        EnemyLazorList.get(EnemyLazorList.size()-1).angle = tempangle;
                        EnemyLazorList.get(EnemyLazorList.size()-1).addVelocity(50*Math.sin(Math.toRadians(180-tempangle)), 50*Math.cos(Math.toRadians(180-tempangle)));
                        timer1 =0;
                        SoundEffect.SHOOT.play();
                    }
                    if(timer>30)
                    {
                        int tempmass = 0;
                        TeleportList.add(new Animation("teleport", 8, 0, this.positionX, this.positionY));
                        while (tempmass == 0) {
                            tempmass = generator.nextInt(30000) - 15000;
                        }
                        this.mass = tempmass;
                        SoundEffect.GROWL.play();
                        timer = 0;

                    }
                    break;
                case 2:
                    timer1+=deltaTime;
                    if (timer > 40) {

                        int tempmass = 0;
                        TeleportList.add(new Animation("teleport", 8, 0, this.positionX, this.positionY));
                        while (tempmass == 0) {
                            tempmass = generator.nextInt(30000) - 15000;
                        }
                        this.mass = tempmass;
                        if (Enemy.size() == 0) {
                            SpawnMore(2, 5, Enemy, TeleportList, player, EnemyNames);
                        }
                        SoundEffect.GROWL.play();
                        timer = 0;
                    }
                    if (timer1>0.5){
                        EnemyLazorList.add(new Sprite("file:enemylazor.png", this.positionX, this.positionY ));
                        int tempangle = generator.nextInt(360);
                        EnemyLazorList.get(EnemyLazorList.size()-1).angle = tempangle;
                        EnemyLazorList.get(EnemyLazorList.size()-1).addVelocity(50*Math.sin(Math.toRadians(180-tempangle)), 50*Math.cos(Math.toRadians(180-tempangle)));
                        SoundEffect.SHOOT.play();
                        timer1 =0;

                    }
                    break;
                case 3:
                    this.isDead = true;

                    break;




            }
        }


    }

    public void SpawnMore(int min, int max, List<Enemy> EnemyList, List<Animation> teleportList, Player player, List<String> EnemyNames) {
        int rand = generator.nextInt(max) + min;
        for (int a = 0; a < rand; a++) {
            double x = generator.nextInt(1920);
            double y = generator.nextInt(1080);
            while (Math.sqrt(Math.pow(x - player.positionX, 2) + Math.pow(y - player.positionY, 2)) < 200) {
                x = generator.nextInt(1920);
                y = generator.nextInt(1080);
            }
            int kind = generator.nextInt(3);
            EnemyList.add(new Enemy(EnemyNames.get(kind), x, y, 1, kind));
            EnemyList.get(a).influencers.add(this);
            //EnemyList.get(a).orbit(this, direction);
            teleportList.add(new Animation("teleport", 8, 0, EnemyList.get(a).positionX, EnemyList.get(a).positionY));

        }

    }
}