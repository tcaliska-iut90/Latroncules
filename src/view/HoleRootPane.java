package view;

import boardifier.view.RootPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.swing.text.html.ImageView;

public class HoleRootPane extends RootPane {

    public HoleRootPane() {
        super();
    }

    @Override
    public void createDefaultGroup() {
        Rectangle frame = new Rectangle(600, 100, Color.LIGHTGREY);
        Text text = new Text("Playing to The Hole");
        text.setFont(new Font(15));
        text.setFill(Color.BLACK);
        text.setX(10);
        text.setY(50);

        // put shapes in the group
        group.getChildren().clear();
        group.getChildren().addAll(frame, text);
    }

    /*
    Button buttonJvJ = new Button("Mode joueur v Joueur");

        Image image = new Image("file:/home/userdepinfo/S2/SAE/SAE126/Latroncules(Graphique)/Images/logo.jpeg");
        ImageView imageView = new ImageView(image);
        imageView.setX(50);
        imageView.setY(0);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
     */
}
