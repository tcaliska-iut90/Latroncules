package view;

import boardifier.model.Model;
import boardifier.view.RootPane;
import boardifier.view.View;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utils.FileUtils;

import java.io.File;

public class HoleView extends View {

    private MenuItem menuStart;
    private MenuItem menuIntro;
    private MenuItem menuQuit;

    private Button buttonJvJ, buttonJvC, buttonCvC, buttonRedPlayer, buttonBluePlayer;

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

    public void setup(){
        rootPane.getChildren().clear();
        vbox.getChildren().clear();

        buttonJvJ = new Button("Mode joueur v Joueur");
        buttonJvC = new Button("Mode joueur v Computer");
        buttonCvC = new Button("Mode Computer v Computer");

        Image image  = new Image("file:" + FileUtils.getFileFromResources("Images/logo.jpeg").getAbsolutePath());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);

        createMenuBar();
        VBox vBox = new VBox(menuBar,imageView, buttonJvJ, buttonJvC, buttonCvC);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);


        rootPane.getChildren().addAll(vBox);
        vbox.getChildren().add(rootPane);
        stage.sizeToScene();
    }

    public void setupChoice(){
        rootPane.getChildren().clear();
        vbox.getChildren().clear();

        buttonBluePlayer = new Button("L'équipe bleue");
        buttonRedPlayer = new Button("L'équipe rouge");
        HBox hBox = new HBox(buttonBluePlayer, buttonRedPlayer);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        Image image  = new Image("file:" + FileUtils.getFileFromResources("Images/logo.jpeg").getAbsolutePath());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);


        VBox vBox = new VBox(menuBar,imageView, hBox);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(vBox);
        vbox.getChildren().add(rootPane);
        stage.sizeToScene();
    }
}
