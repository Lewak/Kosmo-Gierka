package sample;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.shape.*;

import java.awt.geom.AffineTransform;

public class Sprite {
    private Image image;
    public double positionX;
    public double positionY;
    public double velocityX;
    public double velocityY;
    public double angle;
    private double width;
    private double height;
    public Sprite(String path, double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        velocityX = 0;
        velocityY = 0;
        this.angle = 0;
        image =  new Image(path);
        width = image.getWidth();
        height = image.getHeight();
    }


    public void render(GraphicsContext gc, double camX, double camY) {
        gc.save();
        Affine rotate = new Affine(new Rotate(angle, positionX-camX, positionY-camY));
        Affine move = new Affine(new Translate(-camX, -camY));
        gc.transform(rotate);
        gc.transform(move);
        gc.drawImage(image, positionX-width/2, positionY-height/2);
        gc.restore();

    }
    public void addVelocity(double velocityX, double velocityY)
    {
        this.velocityX += velocityX;
        this.velocityY += velocityY;
    }
    public void update(double deltaTime)
    {

        positionX += velocityX * deltaTime;
        positionY += velocityY * deltaTime;

    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX-width/2, positionY-height/2, width, height);
    }

    public boolean intersects(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }
}


