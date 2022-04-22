package sample;

import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class MenuItem extends StackPane {
    //private static Font font;


    private Text text;
    private Rectangle selection;
    private DropShadow shadow;

    public MenuItem(String name, int width){
        setAlignment(Pos.CENTER_LEFT);


        text = new Text(name);
        text.setTranslateX(5);
        text.setFont(new Font("Verdana", 20));
        /*font = Font.loadFont(MenuItem.class.getResource("img/ccoverbyteoffregular.ttf").toExternalForm(), 26);
        font = Font.loadFont("file:img/fonts/Red October-Regular.ttf", 26);
        text.setFont(Font.loadFont("file:img/fonts/RobotoCondensed-Regular.ttf", 26));*/
        text.setFill(Colors.MENU_TEXT);
        //text.setStroke(Color.BLACK);

        shadow = new DropShadow(5, Color.BLACK);
        text.setEffect(shadow);

        selection = new Rectangle(width - 45, 30);
        selection.setFill(Colors.MENU_ITEM_SELECTOR);
        selection.setStroke(Color.BLACK);
        selection.setVisible(false);

        GaussianBlur blur = new GaussianBlur(8);
        selection.setEffect(blur);

        getChildren().add(selection);
        getChildren().add(text);

        setOnMouseEntered(e ->{
            onSelect();
        });
        setOnMouseExited(e ->{
            offSelect();
        });
        setOnMousePressed(e ->{
            text.setFill(Color.YELLOW);
        });
    }

    private void onSelect(){
        text.setFill(Color.BLACK);
        selection.setVisible(true);

        shadow.setRadius(1);
    }

    private void offSelect(){
        text.setFill(Colors.MENU_TEXT);
        selection.setVisible(false);

        shadow.setRadius(5);
    }

    public void setOnAction(Runnable action){
        setOnMouseClicked(e ->{
            FillTransition ft = new FillTransition(Duration.seconds(0.45), selection, Color.YELLOW, Colors.MENU_ITEM_SELECTOR);
            ft.setOnFinished(event -> action.run());
            ft.play();
        });
    }
}
