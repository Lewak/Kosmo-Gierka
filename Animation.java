package sample;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    public double positionX;
    public double positionY;
    public double startTime;
    public int frames;
    public String name;
    public ArrayList<Sprite> frameList = new ArrayList<Sprite>();
    public double angle;
    public Animation(String name, int frames, double angle, double positionX, double positionY) {
        startTime = System.nanoTime()/100000000.0;
        this.name = name;
        this.frames = frames;
        this.positionY = positionY;
        this.positionX = positionX;
        for(int i=0;i<frames;i++)
        {
            frameList.add(new Sprite("file:"+name+i+".png",positionX, positionY ));

        }
    }

    public void updateAnimation(double positionX, double positionY, double angle)
    {
        for(int i=0;i<this.frames;i++)
        {
            frameList.get(i).positionX = positionX;
            frameList.get(i).positionY = positionY;
            frameList.get(i).angle = angle;
        }

    }
    public void loop()
    {
        this.startTime = System.nanoTime()/100000000.0;
    }
    public Sprite getFrame(int multiplier)
    {
        double currentTime = System.nanoTime()/100000000.0;
        for(int i=0;i<this.frames;i++)
        {
            if(multiplier*i+1>currentTime-startTime && currentTime-startTime>multiplier*i)
            {
                return this.frameList.get(i);

            }
        }
        return null;


    }

}
