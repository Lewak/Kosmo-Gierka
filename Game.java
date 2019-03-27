package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Game
{
    Boss sun;
    NewtonianShit earth;
    Player player;
    Sprite space;
    List<Sprite> SpriteList;
    List<NewtonianShit>BodiesList;
    List<Sprite> LazerList;
    List<Sprite> EnemyLazerList;
    List<Animation> explosionList;
    List<Animation> hitboxList;
    List<Animation> teleportList;
    Animation exhaust;
    List<Enemy> EnemyList;
    List<Sprite>hpWheel;
    List<Sprite>bossHp;
    Sprite winscreen;
    double enemyTimer;
    boolean sunTimer;
    public boolean once;
    Sprite edge;
    Animation bossfight;
    //Text text = new Text();

    Random generator = new Random();
    public double static_cameraX;
    public double static_cameraY;
    List<String> EnemyNames = new ArrayList<>();
    public void initialize()
    {
        SoundEffect.init();
        winscreen = new Sprite("file:winscreen.png", 1920/2, 1080/2);
        hpWheel = new ArrayList<Sprite>();
        bossHp = new ArrayList<Sprite>();
        once = false;
        sunTimer = false;
        edge = new Sprite("file:edge.png", 953, 1000);
        for(int i =0;i<30;i++)
        {
            bossHp.add(new Sprite("file:bosshp.png", 735+i*15, 1000));
        }
        for(int i=0;i<5;i++)
        {
            hpWheel.add(new Sprite("file:hp"+i+".png",75,75));
        }
        EnemyNames.add("file:Euler.png");
        EnemyNames.add("file:Leibnitz.png");
        EnemyNames.add("file:Euclid.png");
        sun = new Boss("file:pedro.png", 1920/2, 1080/2, 10000, 30, false);
        earth = new NewtonianShit("file:earth.png", 450, 1080/2, 100);
        sun.influencers.add(earth);
        earth.orbit(sun, 1);
        earth.influencers.add(sun);
        player = new Player("file:studentos.png", 300, 300, 1, 4);
        player.influencers.add(earth);
        player.influencers.add(sun);
        space = new Sprite("file:spaace.jpg", 1920/2, 1080/2);
        int i = 1500;
        SpriteList = new ArrayList<Sprite>();
        for (int a = 0; a < i; a++) {
            double x =  generator.nextInt(10000)-5000;
            double y = generator.nextInt(6000)-3000;
            SpriteList.add(new Sprite("file:spacedust.png",x, y ));
        }
        int b = 10;
        BodiesList = new ArrayList<NewtonianShit>();
        for (int a = 0; a < b; a++) {
            double x =  generator.nextInt(1920);
            double y = generator.nextInt(1080);
            double z = generator.nextInt(500)+1;
            int direction = generator.nextInt(1);
            if (direction ==0){direction = -1;}
            BodiesList.add(new NewtonianShit("file:kid0.png",x, y, z));
            BodiesList.get(a).orbit(sun,direction);
            BodiesList.get(a).influencers.add(earth);
            BodiesList.get(a).influencers.add(sun);
        }
        EnemyList = new ArrayList<Enemy>();
        for(int a = 0;a<BodiesList.size();a++)
        {
            for(int c = 0; c<BodiesList.size();c++)
            {
                if(a!=c) BodiesList.get(a).influencers.add(BodiesList.get(c));
            }
            player.influencers.add(BodiesList.get(a));
        }
        LazerList = new ArrayList<Sprite>();
        EnemyLazerList = new ArrayList<Sprite>();
        explosionList = new ArrayList<Animation>();
        hitboxList = new ArrayList<Animation>();
        teleportList = new ArrayList<Animation>();
        bossfight = new Animation("Bossfight", 4, 0, 1920/2, 1080/2 );
        exhaust = new Animation("Exhaust", 4, 0,0,0);
        SoundEffect.THEME.play();

    }

    public void update(ArrayList<String> input,double mouseX, double mouseY,boolean mouse1, boolean mouse2, double deltaTime, boolean camLock)
    {

        player.handlePlayerControl(mouseX, mouseY, mouse1, mouse2, input, LazerList, exhaust, camLock, static_cameraX, static_cameraY);
        player.update(deltaTime);
        player.laserCooldown(deltaTime);
        player.hitCooldown(deltaTime);
        earth.update(deltaTime);
        sun.update(deltaTime);
        if (!sun.trigger)
        {
            enemySpawn(deltaTime);
        }
        enemyDespawn();
        laserDespawn();
        destructionDetection();
        for(int i = 0; i< hitboxList.size(); i++)
        {
            hitboxList.get(i).updateAnimation(player.positionX, player.positionY, player.angle);
        }
        for(int i = 0; i<EnemyList.size();i++)
        {
            EnemyList.get(i).randomMovement(deltaTime, player);
            EnemyList.get(i).attack(EnemyLazerList, player);
            EnemyList.get(i).update(deltaTime);

        }
        for(int i = 0; i<BodiesList.size();i++)
        {
            BodiesList.get(i).update(deltaTime);

        }
        for(int i = 0; i<LazerList.size();i++)
        {
            LazerList.get(i).update(deltaTime);

        }
        for(int i = 0; i<EnemyLazerList.size();i++)
        {
            EnemyLazerList.get(i).update(deltaTime);

        }
        if(player.points>100)
        {
            sun.trigger = true;
        }
        sun.phaseCheck();
        sun.phaseWork(teleportList, EnemyLazerList, EnemyList, deltaTime, player, EnemyNames);



    }

    public void drawEverything(GraphicsContext gc, boolean camLock)
    {
        double camX;
        double camY;
        gc.clearRect(0, 0, Main.canvasX, Main.canvasY);
        if(!camLock) {
            camX = player.positionX - 1920 / 2;
            camY = player.positionY - 1080 / 2;
        }
        else{
            camX = static_cameraX - 1920 / 2;
            camY = static_cameraY- 1080 / 2;
        }
        space.render(gc,0,0);
        earth.render(gc, camX, camY);
        for (int i = 0; i<SpriteList.size();i++)
        {
            SpriteList.get(i).render(gc, camX/3, camY/3);
        }
        sun.render(gc, camX, camY);
        for(int i = 0; i<BodiesList.size();i++)
        {
            BodiesList.get(i).render(gc, camX, camY);

        }
        for(int i = 0; i<LazerList.size();i++)
        {
            LazerList.get(i).render(gc, camX, camY);

        }
        for(int i = 0; i<EnemyLazerList.size();i++)
        {
            EnemyLazerList.get(i).render(gc, camX, camY);
        }


        for (int i = 0; i<EnemyList.size();i++)
        {
            EnemyList.get(i).render(gc, camX, camY);
        }

        animationCheck(hitboxList, gc, camX, camY);
        animationCheck(explosionList, gc, camX, camY);
        animationCheck(teleportList, gc, camX, camY);
        if(player.checkEngines)
        {
            Sprite temp = exhaust.getFrame(1);
            if (temp == null)
            {
                exhaust.loop();
                SoundEffect.FLY.play();

            }
            else
            {
                exhaust.updateAnimation(player.positionX, player.positionY, player.angle);
                temp.render(gc, camX, camY);

            }

        }
        else{SoundEffect.FLY.stop();}
        player.render(gc, camX, camY);
        displayGui(gc);



    }

    public void destructionDetection()
    {
        for(int a=0;a<LazerList.size();a++)
        {
            for(int b=0;b<EnemyList.size();b++)
            {
                if(LazerList.get(a).intersects(EnemyList.get(b)))
                {
                    explosionList.add(new Animation("Explosion",6,0,EnemyList.get(b).positionX,EnemyList.get(b).positionY));
                    //player.influencers.remove(BodiesList.get(b));
                    EnemyList.remove(EnemyList.get(b));
                    LazerList.remove(LazerList.get(a));
                    SoundEffect.EXPLODE.play();
                    player.points +=20;
                    a--;
                    break;

                }
            }
        }
        for(int a=0;a<LazerList.size();a++)
        {

            if(LazerList.get(a).intersects(sun)){
                if(sun.trigger)
                {
                    sun.hp--;
                    LazerList.remove(a);
                    SoundEffect.EXPLODE.play();
                    explosionList.add(new Animation("bosshit",2,  sun.angle, sun.positionX, sun.positionY));
                    player.points +=100;
                    a--;
                    break;
                }

            }
        }
        for(int a=0;a<EnemyLazerList.size();a++)
        {
            if(EnemyLazerList.get(a).intersects(player))
            {
                if(player.hit_cooldown)
                {
                    EnemyLazerList.remove(a);
                    hitboxList.add(new Animation("hit", 2, player.angle, player.positionX, player.positionY));
                    player.hp--;
                    SoundEffect.GRUNT.play();
                    player.hit_cooldown = false;

                }
            }

        }
        for(int a=0;a<EnemyList.size();a++)
        {
            if(EnemyList.get(a).intersects(player))
            {
                if(player.hit_cooldown)
                {
                    hitboxList.add(new Animation("hit", 2, player.angle, player.positionX, player.positionY));
                    player.hp--;
                    SoundEffect.GRUNT.play();
                    player.hit_cooldown = false;

                }
            }

        }
        if (player.intersects(sun)&&player.hit_cooldown){
            hitboxList.add(new Animation("hit", 2, player.angle, player.positionX, player.positionY));
            player.hp--;
            SoundEffect.GRUNT.play();
            player.hit_cooldown = false;

        }
    }
    public void laserDespawn()
    {
        for(int a =0;a<LazerList.size();a++)
        {
            double distance = Math.sqrt(Math.pow(LazerList.get(a).positionX - player.positionX, 2) + Math.pow(LazerList.get(a).positionY - player.positionY, 2));
            if (distance>2000)
            {
                LazerList.remove(a);
            }
        }
        for(int a =0;a<EnemyLazerList.size();a++)
        {
            double distance = Math.sqrt(Math.pow(EnemyLazerList.get(a).positionX - player.positionX, 2) + Math.pow(EnemyLazerList.get(a).positionY - player.positionY, 2));
            if (distance>2000)
            {
                EnemyLazerList.remove(a);
            }
        }

    }
    public void animationCheck(List<Animation> animation, GraphicsContext gc, double camX, double camY)
    {
        for(int i=0;i<animation.size();i++)
        {
            Sprite temp = animation.get(i).getFrame(1);
            if (temp==null)
            {
                animation.remove(i);

            }
            else
            {
                temp.render(gc,camX,camY);
            }
        }
    }
    public void enemySpawn(double deltaTime)
    {
       // enemyTimer +=deltaTime;
        if (EnemyList.size()==0&&enemyTimer>30)
        {
            int rand = generator.nextInt(4)+1;
            for(int a=0;a<rand;a++)
            {
                double x = generator.nextInt(1920);
                double y = generator.nextInt(1080);
                while(Math.sqrt(Math.pow(x - player.positionX, 2) + Math.pow(y - player.positionY, 2))<200)
                {
                    x = generator.nextInt(1920);
                    y = generator.nextInt(1080);
                }
                int direction = generator.nextInt(1);
                if (direction ==0){direction = -1;}
                int kind = generator.nextInt(3);
                EnemyList.add(new Enemy(EnemyNames.get(kind), x, y, 1, kind));
                EnemyList.get(a).influencers.add(sun);
                EnemyList.get(a).orbit(sun, direction);
                teleportList.add(new Animation("teleport", 8, 0, EnemyList.get(a).positionX, EnemyList.get(a).positionY ));
            }
            enemyTimer = 0;


        }

    }
    public void enemyDespawn()
    {
        for(int i=0;i<EnemyList.size();i++)
        {
            if(Math.sqrt(Math.pow(EnemyList.get(i).positionX - player.positionX, 2) + Math.pow(EnemyList.get(i).positionY - player.positionY, 2))>2000)
            {
                EnemyList.remove(i);
                enemyTimer = 0;
            }
        }
    }
    public void displayGui(GraphicsContext gc)
    {
        String points = "Points: "+player.points;
        gc.fillText(points, 1700, 50);
        hpWheel.get(player.hp).render(gc, 0, 0);
        if(sun.trigger)
        {
            for(int i =0;i<sun.hp;i++)
            {
                bossHp.get(i).render(gc, 0, 0);
            }
            edge.render(gc, 0, 0);
            Sprite temp;
            if(!sunTimer){ bossfight.loop(); }
            temp = bossfight.getFrame(2);
            if(temp!=null)
            {
                temp.render(gc, 0,0);
            }
            sunTimer = true;

        }

    }


}
