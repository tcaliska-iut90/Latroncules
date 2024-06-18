package view;

import boardifier.model.Model;
import boardifier.view.RootPane;
import boardifier.view.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.FileUtils;

import java.awt.*;
import java.io.File;

public class HoleView extends View {

    private MenuItem menuStart;
    private MenuItem menuIntro;
    private MenuItem menuQuit;

    private Button buttonJvJ, buttonJvC, buttonCvC, buttonRedPlayer, buttonBluePlayer;
    private Button buttonJvIA1, buttonJvIA2, buttonIA1vIA1, buttonIA1vIA2, buttonIA2vIA2;

    private Rectangle frame;
    private ImageView imageView;
    private Text text;

    public HoleView(Model model, Stage stage, RootPane rootPane) {
        super(model, stage, rootPane);
    }

    @Override
    protected void createMenuBar() {
        menuBar = new MenuBar();
        Menu menu1 = new Menu("Game");
        menuStart = new MenuItem("New game");
        menuIntro = new MenuItem("Intro");
        menuQuit = new MenuItem("Quit");
        menu1.getItems().add(menuStart);
        menu1.getItems().add(menuIntro);
        menu1.getItems().add(menuQuit);
        menuBar.getMenus().add(menu1);

        initWidget();
    }

    public void initWidget(){

        frame = new Rectangle(600, 150, Color.LIGHTGREY);
        Image image  = new Image("file:" + FileUtils.getFileFromResources("Images/logo.jpeg").getAbsolutePath());
        imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        imageView.setX((frame.getWidth()/2) - (imageView.getFitWidth()/2));
        imageView.setY(0);

        text = new Text("LATRONCULES");
        text.setFont(new Font(20));
        text.setFill(Color.BLACK);
        text.setX((frame.getWidth()/2) - 75);
        text.setY(imageView.getFitHeight()+20);


        buttonJvJ = new Button("Mode joueur v Joueur");
        buttonJvC = new Button("Mode joueur v Computer");
        buttonCvC = new Button("Mode Computer v Computer");

        buttonBluePlayer = new Button("L'équipe rouge");
        buttonRedPlayer = new Button("L'équipe bleue");


    }

    public MenuItem getMenuStart() {
        return menuStart;
    }

    public MenuItem getMenuIntro() {
        return menuIntro;
    }

    public MenuItem getMenuQuit() {
        return menuQuit;
    }

    public void dialogError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Button getButtonJvJ() {
        return buttonJvJ;
    }

    public Button getButtonJvC() {
        return buttonJvC;
    }

    public Button getButtonCvC() {
        return buttonCvC;
    }

    public Button getButtonBluePlayer() {
        return buttonBluePlayer;
    }

    public Button getButtonRedPlayer() {
        return buttonRedPlayer;
    }

    public Button getButtonJvIA1() {
        return buttonJvIA1;
    }

    public Button getButtonJvIA2() {
        return buttonJvIA2;
    }

    public Button getButtonIA1vIA1() {
        return buttonIA1vIA1;
    }

    public Button getButtonIA1vIA2() {
        return buttonIA1vIA2;
    }

    public Button getButtonIA2vIA2() {
        return buttonIA2vIA2;
    }
    public void setup(){
        rootPane.setClip(null);
        Group group = new Group(frame, text, imageView);

        HBox hBox = new HBox(buttonJvJ, buttonJvC, buttonCvC);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10, 0, 10, 0));

        VBox vBox =new VBox(group, hBox);
        vBox.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(vBox);
        stage.sizeToScene();
    }

    public void setupChoice(){
        rootPane.resetToDefault();
        rootPane.setClip(null);


        Group group = new Group(frame, text, imageView);


        HBox hBox = new HBox(buttonBluePlayer, buttonRedPlayer);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10, 0, 10, 0));

        VBox vBox =new VBox(group, hBox);
        vBox.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(vBox);
        stage.sizeToScene();
    }

    public void setupAIChoice(boolean isJvC){
        rootPane.resetToDefault();
        rootPane.setClip(null);

        Group group = new Group(frame, text, imageView);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10, 0, 10, 0));

        if (isJvC) {
            buttonJvIA1 = new Button("Joueur v IA1");
            buttonJvIA2 = new Button("Joueur v IA2");
            hBox.getChildren().addAll(buttonJvIA1, buttonJvIA2);
        } else {
            buttonIA1vIA1 = new Button("IA1 v IA1");
            buttonIA1vIA2 = new Button("IA1 v IA2");
            buttonIA2vIA2 = new Button("IA2 v IA2");
            hBox.getChildren().addAll(buttonIA1vIA1, buttonIA1vIA2, buttonIA2vIA2);
        }

        VBox vBox =new VBox(group, hBox);
        vBox.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(vBox);
        stage.sizeToScene();
    }
}
