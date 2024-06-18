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
    private MenuItem menuHelp;

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
        menuHelp = new MenuItem("Help");
        menu1.getItems().add(menuStart);
        menu1.getItems().add(menuIntro);
        menu1.getItems().add(menuQuit);
        menu1.getItems().add(menuHelp);
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
    public MenuItem getMenuHelp(){return menuHelp;}

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
        rootPane.resetToDefault();
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

    public void help(){
        rootPane.resetToDefault();
        rootPane.setClip(null);

        Group group = new Group(frame, text, imageView);

        HBox hBox = new HBox();
        Text text1 = new Text("\"Latroncules\" est un jeu de stratégie antique où le but est de prendre les pions de l'adversaire. L'objectif est de prendre plus de pions que son adversaire avant que la partie ne s'arrête.\n" +
                "\n" +
                "1°/ Matériel\n" +
                "\n" +
                "Le plateau est constitué d'une grille 8x8, avec certaines cases \"gravées\" avec des flèches de direction. Le haut et le bas constituent le camp de base des joueurs. La figure 1 ci-dessous donne un exemple de plateau.\n" +
                "\n" +
                "Chaque joueur possède 8 pions de type fantassin, et 8 pions cavaliers.\n" +
                "\n" +
                "2°/ Régles\n" +
                "\n" +
                "2.1°/ Déroulement du jeu\n" +
                "\n" +
                "\n" +
                "On détermine le premier joueur par tirage au sort, ou par accord commun. " +
                "\n" +
                "Chacun son tour, un joueur doit déplacer un de ses pions en suivant les règles données dans la section 2.2. Cette alternance se poursuit jusqu'à ce que la partie s'arrête, selon les conditions données en section 2.4\n" +
                "\n" +
                "2.2°/ Déplacements des pions\n" +
                "\n" +
                "Un fantassin ne peut se déplacer que d'une seule case à la fois, TOUT DROIT, en direction du camp opposé. " +
                "\n" +
                "Il ne peut en aucun cas sauter par dessus un autre pion. Un cavalier se déplace différemment selon le type de case où il se trouve : case \"blanche\" : il peut aller sur n'importe quelle case libre parmi les 8 qui l'entoure. " +
                "\n" +
                "Si l'une de ces cases est occupée par un pion ADVERSE, il peut sauter par dessus à condition que la case d'arrivée se trouvant dans la même direction soit libre (cf. figure 3). " +
                "\n" +
                "case \"fléchée\" : le principe est le même qu'avec les cases blanches, " +
                "\n" +
                "excepté qu'il ne peut aller que dans la direction indiquée par les flèches si le cavalier saute un pion adverse, ce dernier n'est pas pris (comme aux dames) et reste à sa place." +
                "\n" +
                " ATTENTION ! les règles ci-dessus ont une exception : un pion ne peut JAMAIS se déplacer entre DEUX (ou plus) pions ADVERSES, sauf s'il est en mesure de prendre un pion adverse\n" +
                "\n" +
                "2.2°/ Prise de pions\n" +
                "\n" +
                "A part dans les coins, pour prendre un (ou plusieurs) pion adverse, il faut l'entourer avec au moins 2 pions de l'autre couleur (peu importe leur type). " +
                "\n" +
                "Entourer signifie former un alignement avec le pion à prendre, donc avec les 2 pions preneurs positionnés dans les cases adjacentes : " +
                "\n" +
                "gauche et droite (= alignement horizontal), haut et bas (= alignement vertical), haut-gauche et bas-droite (= alignement oblique), haut-droit et bas-gauche (= alignement oblique). " +
                "\n" +
                "Il est possible de prendre un pion se trouvant dans les coins, en format un L avec ce pion et deux autres pions adverses. " +
                "\n" +
                "Comme un pion ne fait qu'un seul déplacement, la prise enchaînée comme aux dames n'est pas possible. En revanche, il est possible dans certaines situation de prendre d'un seul coup plusieurs pions adverses. " +
                "\n" +
                "A noter qu'il Il existe d'autres déplacements possible mais sans prendre de pion. En revanche, il est interdit d'aller à droite car il se retrouve lui-même entouré. " +
                "\n" +
                "2.3° Promotion\n" +
                "\n" +
                "Si un fantassin parvient à rejoindre l'autre bord du plateau (le haut pour les rouges et le bas pour les bleus), il devient automatiquement un cavalier.\n" +
                "\n" +
                "2.4°/ Fin de partie\n" +
                "\n" +
                "La partie s'arrête avec un gagnant quand l'un des joueurs n'a plus de pion, ou bien quand on décide de mettre fin à la partie car il n'est plus possible de prendre d'autres pions. " +
                "\n" +
                "Dans ce cas, c'est le joueur qui a le plus de pions restant sur le plateau qui a gagné. La partie s'arrête également si un joueur ne peut pas jouer autre chose qu'un déplacement interdit, par exemple entre deux pions adverses, ou bien un blocage total. " +
                "\n" +
                "Dans ce cas, il n'y a pas de gagnant et la partie est nulle (NB : cela peut être une stratégie quand on est en mauvaise posture).");

        hBox.getChildren().add(text1);
        VBox vBox =new VBox(group, hBox);
        vBox.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(vBox);
        stage.sizeToScene();
        frame.setWidth(stage.getWidth());
        imageView.setX((stage.getWidth()/2) - imageView.getFitWidth()/2);
        text.setX(0);
        initWidget();
    }
}
