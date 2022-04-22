package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;


public class Menu extends Application {
    static Stage classStage = new Stage();

    private MenuItem newMenuItem;
    private Rectangle masker;
    private Pane rootPane;
    private Parent createContent(){
        rootPane = new Pane();


        ImageView imageView = new ImageView(new Image(getClass().getResource("resources/soviet_bg.jpg").toExternalForm()));
        imageView.setFitHeight(720);
        imageView.setFitWidth(1280);

        //TODO sepia

        masker= new Rectangle(1280, 720);
        masker.setOpacity(0);
        masker.setMouseTransparent(true);

        MenuBox menuBox = new MenuBox(250, 155);
        menuBox.setTranslateX(250);
        menuBox.setTranslateY(230);

        MenuBox menuBox2 = new MenuBox(510, 350);
        menuBox2.setTranslateX(520);
        menuBox2.setTranslateY(230);
        menuBox2.setVisible(false);


        newMenuItem = new MenuItem("NEW", 250);
        //TODO
        /*newMenuItem.setOnAction(() ->{
            FadeTransition ft = new FadeTransition(Duration.seconds(1.5), masker);
            ft.setToValue(1);

            ft.setOnFinished(e -> {
                rootPane.getChildren().setAll(new PogodiScreen(1280, 720));
            });
            ft.play();
        });*/
        menuBox.addItem(newMenuItem);

        /*MenuItem settingsMenuItem = new MenuItem("SETTINGS", 250);
        settingsMenuItem.setOnAction(()->{
            menuBox2.setVisible(true);
            menuBox2.setItems(
                    new MenuItem("GAMEPLAY", 510),
                    new MenuItem("CONTROLS", 510),
                    new MenuItem("DISPLAY", 510),
                    new MenuItem("AUDIO", 510)
            );

        });
        menuBox.addItem(settingsMenuItem);*/

        MenuItem scoreMenuItem = new MenuItem("SCORE", 250);
        scoreMenuItem.setOnAction(()->{
            ArrayList scores = new ArrayList<>();
            menuBox2.setVisible(true);
            if (new File("res.txt").length() != 0) {
                try {
                    FileInputStream inp = new FileInputStream("res.txt");
                    ObjectInputStream objectInputStream = new ObjectInputStream(inp);
                    scores = (ArrayList) objectInputStream.readObject ();
                    inp.close();
                    objectInputStream.close();
                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace ( );
                }
            }

            Collections.sort(scores);

            menuBox2.setItems(scores);
        });
        menuBox.addItem(scoreMenuItem);


        MenuItem exitMenuItem = new MenuItem("EXIT", 250);
        exitMenuItem.setOnAction(() -> System.exit(0));
        menuBox.addItem(exitMenuItem);

        //TODO menu music
        //Media media = new Media("");

        rootPane.getChildren().addAll(imageView, menuBox, menuBox2, masker);
        return rootPane;
    }



    @Override
    public void start(Stage stage) throws Exception {
        classStage = stage;

        Scene scene = new Scene(createContent());

        NuPogodi nuPogodi = new NuPogodi();

        newMenuItem.setOnAction(() ->{
            FadeTransition ft = new FadeTransition(Duration.seconds(1.5), masker);
            ft.setToValue(1);

            ft.setOnFinished(e -> {
                /*rootPane.getChildren().setAll(new PogodiScreen(1280, 720));*/
                try {
                    nuPogodi.start(NuPogodi.classStage);
                    stage.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
            ft.play();
        });


        classStage.setTitle("Menu");
        classStage.setScene(scene);
        classStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
