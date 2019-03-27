package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.HashSet;
import java.util.ArrayList;
import java.lang.*;

public class Main extends Application {

    public static final int canvasX = 1920;
    public static final int canvasY = 1080;
    private final double timeMultiplier = 100000000;
    public double oldNanoTime;
    private double mouseX;
    private double mouseY;
    private ArrayList<String> keysPressed;
    private boolean mouse1;
    private boolean mouse2;
    private Game game;
    boolean camLock;
    private boolean C_before;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        game = new Game();
        game.initialize();
        C_before = false;
        theStage.setTitle("SpejzRejz");

        Group root = new Group();
        Scene mainScene = new Scene(root);
        theStage.setScene(mainScene);

        Canvas canvas = new Canvas(canvasX, canvasY);
        root.getChildren().add(canvas);
        theStage.setFullScreen(true);
        keysPressed = new ArrayList<String>();

        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString();
                if ( !keysPressed.contains(code) )
                    keysPressed.add( code );
            }
        });

        mainScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString();
                keysPressed.remove( code );
            }
        });

        mainScene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            }
        });

        mainScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            }
        });
        mainScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouse1 = event.isPrimaryButtonDown();
                mouse2 = event.isSecondaryButtonDown();
            }
        });
        mainScene.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouse1 = event.isPrimaryButtonDown();
                mouse2 = event.isSecondaryButtonDown();
            }
        });


        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Arial",40));
        gc.setFill(Color.WHITE);
        oldNanoTime = System.nanoTime();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {


                    double deltaTime = (currentNanoTime - oldNanoTime) / timeMultiplier;
                    oldNanoTime = currentNanoTime;

                    if (keysPressed.contains("C")) {
                        if (!C_before) {
                            camLock = !camLock;
                        }
                        game.static_cameraX = game.player.positionX;
                        game.static_cameraY = game.player.positionY;
                        C_before = true;
                    } else {
                        C_before = false;
                    }
                    if(!game.sun.isDead)
                    {
                        game.update(keysPressed, mouseX, mouseY, mouse1, mouse2, deltaTime, camLock);
                        game.drawEverything(gc, camLock);


                    }
                    else
                    {
                        game.winscreen.render(gc, 0,0);
                        SoundEffect.THEME.stop();
                        SoundEffect.FLY.stop();
                        SoundEffect.EXPLODE.stop();
                        SoundEffect.GROWL.stop();
                        SoundEffect.SHOOT.stop();
                        if(!game.once)
                        {
                            SoundEffect.DUN.play();
                            game.once = true;
                        }
                    }
                    if (keysPressed.contains("ESCAPE")) {
                        System.exit(0);
                    }
                    if (keysPressed.contains(("R")) || game.player.hp == 0) {
                        game.initialize();
                    }


            }
        }.start();

        theStage.show();
    }


}
