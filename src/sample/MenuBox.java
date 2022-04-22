package sample;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
//javafx.media in virtual machine

public class MenuBox extends Pane {
    private VBox box;

    public MenuBox(int width, int height){

        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Colors.MENU_BG);

        Rectangle lineTop = new Rectangle(width, 2);
        lineTop.setFill(Colors.MENU_BORDER);
        lineTop.setStroke(Color.BLACK);

        Rectangle lineBot = new Rectangle(width, 2);
        lineBot.setTranslateY(height - 2);
        lineBot.setFill(Colors.MENU_BORDER);
        lineBot.setStroke(Color.BLACK);

        box = new VBox(5);
        box.setTranslateX(25);
        box.setTranslateY(25);

        getChildren().addAll(bg, lineTop, lineBot, box);
    }
    public void addItems(MenuItem... items){
        for(MenuItem item : items)
            addItem(item);
    }

    public void setItems(MenuItem... items){
        box.getChildren().clear();
        addItems(items);
    }

    public void addItem(MenuItem item){
        box.getChildren().add(item);
    }
    public void setItems(ArrayList<Score> scores){
        box.getChildren().clear();
        for (Score score : scores) {
            box.getChildren().add(new MenuItem(score.toString(), 510));
        }
    }

}
