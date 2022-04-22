package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NuPogodi extends Application {

    static Stage classStage = new Stage();



    private Pane root = new Pane();

    private enum UserAction{
        NONE, LEFT, RIGHT, UPLEFT, UPRIGHT
    }
    private enum Nest{
        UPLEFT(110, 230), UPRIGHT(1150, 250), LEFT(100, 414), RIGHT(1200, 410);
        private final int X;
        private final int Y;

        Nest(int x, int y) {
            X = x;
            Y = y;
        }

        public int getX() {
            return X;
        }

        public int getY() {
            return Y;
        }
    }
    private static final int APP_w = 1280;
    private static final int APP_h = 720;


    private Rectangle basket = new Rectangle(70, 70);

    private UserAction action = UserAction.NONE;

    private Timeline timeline = new Timeline();
    private boolean running = true;

    private double time = 0.0;

    private int activeEggs = 1;
    private List<Egg> eggs = new ArrayList<>();

    private int score = 0;
    private Text scoreText = new Text();

    private int lives = 3;

    private ArrayList scoreList = new ArrayList<>();


    public Parent createContent(){
        ImageView bg = new ImageView(new Image(getClass().getResource("resources/game_bg.jpg").toExternalForm()));
        bg.setFitWidth(APP_w);
        bg.setFitHeight(APP_h);


        root.setPrefSize(APP_w, APP_h);

        basket.setTranslateX(250);
        basket.setTranslateY(550);
        basket.setFill(Color.BLUE);

        scoreText.setText(String.valueOf(score));
        scoreText.setFont(Font.font(30));
        scoreText.setTranslateX(1100);
        scoreText.setTranslateY(100);


        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.01), event ->{
            if(!running)
                return;

            switch (action){
                case LEFT:
                    basket.setTranslateX(250);
                    basket.setTranslateY(550);
                    break;
                case RIGHT:
                    basket.setTranslateX(970);
                    basket.setTranslateY(550);
                    break;
                case UPRIGHT:
                    basket.setTranslateX(250);
                    basket.setTranslateY(350);
                    break;
                case UPLEFT:
                    basket.setTranslateX(970);
                    basket.setTranslateY(350);
                    break;
                case NONE:
                    break;
            }
            time+=0.01;
            activeEggs = 1+ (int)(time/30);
            for (int i = eggs.size(); i < activeEggs; ++i)
                generateEgg();


            eggs.forEach(Egg::update);
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().addAll(bg, basket, scoreText);
        return root;
    }

    private void restartGame(){
        stopGame();
        startGame();
    }

    private void stopGame(){
        running = false;
        timeline.stop();


        int pointFin = score;
        String a = JOptionPane.showInputDialog(null,
                "Your nickname",
                "Final score: ",
                JOptionPane.PLAIN_MESSAGE);
        Score score = new Score(pointFin, a);
        if (new File("res.txt").length() != 0) {
            try {
                FileInputStream inp = new FileInputStream("res.txt");
                ObjectInputStream objectInputStream = new ObjectInputStream(inp);
                scoreList = (ArrayList) objectInputStream.readObject ();
                inp.close();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace ( );
            }
        }
        scoreList.add(score);

        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream (new FileOutputStream ("res.txt"));
            objectOutputStream.writeObject (scoreList);
            objectOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Menu menu = new Menu();
        try{
            menu.start(Menu.classStage);
            classStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGame(){
        timeline.play();
        running = true;
    }

    @Override
    public void start(Stage stage) throws Exception {

        classStage = stage;

        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(event ->{
            switch (event.getCode()){
                case A -> action = UserAction.LEFT;
                case D -> action = UserAction.RIGHT;
                case E -> action = UserAction.UPLEFT;
                case Q -> action = UserAction.UPRIGHT;
            }
        });

        final KeyCombination combination = new KeyCodeCombination(KeyCode.Q, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (combination.match(keyEvent))
                    System.exit(0);
            }
        });


        scene.setOnMouseClicked(mouseEvent -> {
            System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
        });

        classStage.setTitle("Pong");
        classStage.setScene(scene);
        classStage.show();
        startGame();
    }

    private class Egg extends Circle {
        private boolean dead = false;
        private Nest from;

        Egg(Nest from){
            super(10,Color.BLACK);

            this.from = from;
            setTranslateX(from.X);
            setTranslateY(from.Y);
        }

        public Nest getFrom() {
            return from;
        }

        public void setFrom(Nest from) {
            this.from = from;
        }

        public void update(){
            if (this.getBoundsInParent().intersects(basket.getBoundsInParent())){
                scoreText.setText(String.valueOf(++score));
                //scoreLabel.setText(String.valueOf(score));
                System.out.println(score);
                repaceEgg(this);
            }
            switch (from){
                case RIGHT ->{
                    if(this.getTranslateX() > 1011) {
                        this.setTranslateX(this.getTranslateX() - ((100 + time) / 200));
                        this.setTranslateY(this.getTranslateY() + ((100 + time) / 420));
                    }
                    else if (this.getTranslateY() < 620){
                        this.setTranslateY(this.getTranslateY() + ((100 + time) / 200));
                    }
                    else {
                        repaceEgg(this);
                        breakEgg();
                    }
                }
                case LEFT -> {
                    if(this.getTranslateX() < 270) {
                        this.setTranslateX(this.getTranslateX() + ((100 + time) / 200));
                        this.setTranslateY(this.getTranslateY() + ((100 + time) / 380));
                    }
                    else if (this.getTranslateY() < 620){
                        this.setTranslateY(this.getTranslateY() + ((100 + time) / 200));
                    }
                    else {
                        repaceEgg(this);
                        breakEgg();
                    }
                }
                case UPRIGHT -> {
                    if(this.getTranslateX() > 1010) {
                        this.setTranslateX(this.getTranslateX() - ((100 + time) / 200));
                        this.setTranslateY(this.getTranslateY() + ((100 + time) / 330));
                    }
                    else if (this.getTranslateY() < 420){
                        this.setTranslateY(this.getTranslateY() + ((100 + time) / 200));
                    }
                    else {
                        repaceEgg(this);
                        breakEgg();
                    }
                }
                case UPLEFT -> {
                    if(this.getTranslateX() < 271) {
                        this.setTranslateX(this.getTranslateX() + ((100 + time) / 200));
                        this.setTranslateY(this.getTranslateY() + ((100 + time) / 330));
                    }
                    else if (this.getTranslateY() < 420){
                        this.setTranslateY(this.getTranslateY() + ((100 + time) / 200));
                    }
                    else {
                        repaceEgg(this);
                        breakEgg();
                    }
                }
            }
        }
    }

    public void generateEgg(){
        switch ((int) (Math.random() / 0.25)) {
            case 0 -> {
                Egg egg = new Egg( Nest.UPLEFT);
                root.getChildren().add(egg);
                eggs.add(egg);
            }
            case 1 -> {
                Egg egg = new Egg( Nest.LEFT);
                root.getChildren().add(egg);
                eggs.add(egg);
            }
            case 2 -> {
                Egg egg = new Egg( Nest.UPRIGHT);
                root.getChildren().add(egg);
                eggs.add(egg);
            }
            default -> {
                Egg egg = new Egg( Nest.RIGHT);
                root.getChildren().add(egg);
                eggs.add(egg);
            }
        }
    }

    public void repaceEgg(Egg egg){
        switch ((int) (Math.random() / 0.25)) {
            case 0 -> {
                egg.setFrom(Nest.UPLEFT);
            }
            case 1 -> {
                egg.setFrom( Nest.LEFT);
            }
            case 2 -> {
                egg.setFrom( Nest.UPRIGHT);
            }
            default -> {
                egg.setFrom( Nest.RIGHT);
            }
        }
        egg.setTranslateX(egg.getFrom().X);
        egg.setTranslateY(egg.getFrom().Y);
    }

    public void breakEgg(){
        Circle brokenEgg = new Circle(10);
        switch (lives--){
            case 3 -> {
                brokenEgg.setTranslateX(1100);
                brokenEgg.setTranslateY(150);
            }
            case 2 -> {
                brokenEgg.setTranslateX(1070);
                brokenEgg.setTranslateY(150);
            }
            case 1 -> {
                brokenEgg.setTranslateX(1040);
                brokenEgg.setTranslateY(150);
            }
            default -> stopGame();
        }
        root.getChildren().add(brokenEgg);
    }
    /*private List<Egg> eggs(){
        return root.getChildren().stream().map(n -> (Egg)n).collect(Collectors.toList());
    }*/


    public static void main(String[] args) {
        launch(args);
    }
}
