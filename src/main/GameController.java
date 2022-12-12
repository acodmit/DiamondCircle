package main;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

import figure.Figure;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;


import javafx.stage.Stage;
import javafx.util.Duration;
import map.Matrix;
import card.RegularCard;
import card.SpecialCard;
import figure.HoveringFigure;
import figure.RegularFigure;

import static map.Matrix.LOCK;


public class GameController {

    public static int COUNT = 0;
    public static double FIELD_SIZE;

    public static File GAMES_FOLDER = null;

    @FXML
    AnchorPane anchorCard;
    @FXML
    private Button btnFigure0;

    @FXML
    private Button btnFigure1;

    @FXML
    private Button btnFigure2;

    @FXML
    private Button btnFigure3;

    @FXML
    private Button btnFigure4;

    @FXML
    private Button btnFigure5;

    @FXML
    private Button btnFigure6;

    @FXML
    private Button btnFigure7;

    @FXML
    private Button btnFigure8;

    @FXML
    private Button btnFigure9;

    @FXML
    private Button btnFigure10;

    @FXML
    private Button btnFigure11;

    @FXML
    private Button btnFigure12;

    @FXML
    private Button btnFigure13;

    @FXML
    private Button btnFigure14;

    @FXML
    private Button btnFigure15;


    @FXML
    GridPane gridGame;

    @FXML
    private Label lblCurrentTime;

    @FXML
    private Label lblCurrentCard;

    @FXML
    private Label lblCurrentFigure;

    @FXML
    private Label lblCurrentPlayer;

    @FXML
    private Label lblNumberOfFinishedGames;

    @FXML
    private Label lblPlayer3;

    @FXML
    private Label lblPlayer4;

    private Timeline timeline;


    public void initialize() {

        Main.MATRIX = new Matrix();

        timeline = new Timeline(new KeyFrame(Duration.millis(500),e -> mapRefresh()));
        timeline.setCycleCount(Animation.INDEFINITE);

        configureNodes();
        configureGrid();
        configureCardView();
        configureGamesFolder();

    }


    public void configureNodes() {

        if (Matrix.NUMBER_OF_PLAYERS <= 3) {
            lblPlayer4.setVisible(false);
        }
        if (Matrix.NUMBER_OF_PLAYERS == 2) {
            lblPlayer3.setVisible(false);
        }

        int number_of_figures = Matrix.NUMBER_OF_PLAYERS * 4;

        if (number_of_figures <= 12) {
            btnFigure12.setVisible(false);
            btnFigure13.setVisible(false);
            btnFigure14.setVisible(false);
            btnFigure15.setVisible(false);
        }
        if (number_of_figures <= 8) {
            btnFigure8.setVisible(false);
            btnFigure9.setVisible(false);
            btnFigure10.setVisible(false);
            btnFigure11.setVisible(false);
        }
    }

    public void configureGrid() {

        FIELD_SIZE = 350 / (double) Matrix.MATRIX_SIZE;

        for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
            ColumnConstraints column = new ColumnConstraints(FIELD_SIZE);
            RowConstraints row = new RowConstraints(FIELD_SIZE);
            gridGame.getColumnConstraints().add(column);
            gridGame.getRowConstraints().add(row);
        }

        for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
            for (int j = 0; j < Matrix.MATRIX_SIZE; j++) {
                ImageView fieldView = new ImageView();
                fieldView.setFitWidth(FIELD_SIZE);
                fieldView.setFitHeight(FIELD_SIZE);
                gridGame.add(fieldView, i, j);
                Label lblNumber = new Label(Integer.toString(Matrix.FIELDS[j][i].getNumber()));
                lblNumber.setFont(Font.font(16.0));
                gridGame.add(lblNumber, i, j);
                GridPane.setHalignment(lblNumber, HPos.CENTER);
                GridPane.setValignment(lblNumber, VPos.CENTER);
            }
        }
        gridGame.setGridLinesVisible(true);
    }

    public void configureCardView() {
        ImageView cardView = new ImageView();
        cardView.setFitHeight(220);
        cardView.setFitWidth(170);
        anchorCard.getChildren().add(cardView);
    }

    private void configureGamesFolder() {

        int games;
        try {
            GAMES_FOLDER = new File("Resources" + File.separator + "Games");
            games = Objects.requireNonNull(GAMES_FOLDER.list()).length;

            lblNumberOfFinishedGames.setText(lblNumberOfFinishedGames.getText() + " " + games);

        } catch (NullPointerException ex) {
            Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }

    }

    public void showCard(String path) {

        Image cardImage = new Image(path);
        ImageView cardImageView = new ImageView(cardImage);
        cardImageView.setFitWidth(170);
        cardImageView.setFitHeight(220);
        Platform.runLater(() -> anchorCard.getChildren().add(cardImageView));

    }

    public void showField(String path, int X, int Y) {

        Image fieldImage = new Image(path);
        ImageView fieldImageView = new ImageView(fieldImage);
        fieldImageView.setFitWidth(FIELD_SIZE);
        fieldImageView.setFitHeight(FIELD_SIZE);
        Platform.runLater(() -> gridGame.add(fieldImageView, X, Y));
    }

    public void mapRefresh() {
        synchronized (LOCK) {

            //System.out.println(" OSVJEZAVANJE MATRICE POCETAK.");

            while (Main.GAME_PAUSE) {
                try {
                    Matrix.LOCK.wait();
                } catch (InterruptedException ex) {
                    Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
                }
            }

            Main.CURRENT_TIME += 500;

            Platform.runLater(() -> gridGame.getChildren().removeIf(node -> node instanceof ImageView));
            Platform.runLater(() -> anchorCard.getChildren().removeIf(node -> node instanceof ImageView));

            Platform.runLater(() -> lblCurrentTime.setText("Current time playing: " + Main.CURRENT_TIME / 1000 + "s"));
            if (Main.CURRENT_PLAYER != null)
                Platform.runLater(() -> lblCurrentPlayer.setText("Current - " + Main.CURRENT_PLAYER.toString()));
            if (Main.CURRENT_CARD != null)
                Platform.runLater(() -> lblCurrentCard.setText(Main.CURRENT_CARD.toString()));
            if (Main.CURRENT_FIGURE != null)
                Platform.runLater(() -> lblCurrentFigure.setText(Main.CURRENT_FIGURE.toString()));

            String folder = File.separator + "Images" + File.separator;
            if (Main.CURRENT_CARD instanceof RegularCard) {
                if (((RegularCard) Main.CURRENT_CARD).getNumber() == 1) {
                    showCard(folder + "card_1.png");
                } else if (((RegularCard) Main.CURRENT_CARD).getNumber() == 2) {
                    showCard(folder + "card_2.png");
                } else if (((RegularCard) Main.CURRENT_CARD).getNumber() == 3) {
                    showCard(folder + "card_3.png");
                } else if(((RegularCard) Main.CURRENT_CARD).getNumber() == 4){
                    showCard(folder + "card_4.png");
                }
            } else if (Main.CURRENT_CARD instanceof SpecialCard) {
                showCard(folder + "special.png");
            }

            //System.out.println(" OSVJEZAVANJE MATRICE.");

            for (int j = 0; j < Matrix.MATRIX_SIZE; j++) {
                for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
                    if (Matrix.FIELDS[i][j].getHole()) {
                        showField(folder + "hole.png", j, i);
                    } else if (Matrix.FIELDS[i][j].getDiamond()) {
                        showField(folder + "diamond.png", j, i);
                    } else if (Matrix.FIELDS[i][j].getFigure() != null) {

                        if ("YELLOW".equals(Matrix.FIELDS[i][j].getFigure().getColour().toString())) {

                            if (Matrix.FIELDS[i][j].getFigure() instanceof RegularFigure) {
                                showField(folder + "regular_yellow.png", j, i);
                            } else if (Matrix.FIELDS[i][j].getFigure() instanceof HoveringFigure) {
                                showField(folder + "hovering_yellow.png", j, i);
                            } else {
                                showField(folder + "super_fast_yellow.png", j, i);
                            }
                        } else if ("GREEN".equals(Matrix.FIELDS[i][j].getFigure().getColour().toString())) {

                            if (Matrix.FIELDS[i][j].getFigure() instanceof RegularFigure) {
                                showField(folder + "regular_green.png", j, i);
                            } else if (Matrix.FIELDS[i][j].getFigure() instanceof HoveringFigure) {
                                showField(folder + "hovering_green.png", j, i);
                            } else {
                                showField(folder + "super_fast_green.png", j, i);
                            }
                        } else if ("RED".equals(Matrix.FIELDS[i][j].getFigure().getColour().toString())) {

                            if (Matrix.FIELDS[i][j].getFigure() instanceof RegularFigure) {
                                showField(folder + "regular_red.png", j, i);
                            } else if (Matrix.FIELDS[i][j].getFigure() instanceof HoveringFigure) {
                                showField(folder + "hovering_red.png", j, i);
                            } else {
                                showField(folder + "super_fast_red.png", j, i);
                            }
                        } else if ("BLUE".equals(Matrix.FIELDS[i][j].getFigure().getColour().toString())) {

                            if (Matrix.FIELDS[i][j].getFigure() instanceof RegularFigure) {
                                showField(folder + "regular_blue.png", j, i);
                            } else if (Matrix.FIELDS[i][j].getFigure() instanceof HoveringFigure) {
                                showField(folder + "hovering_blue.png", j, i);
                            } else {
                                showField(folder + "super_fast_blue.png", j, i);
                            }

                        }
                    }
                }
            }
        }
    }

    @FXML
    void handleShowFilesBtn(){

        Stage filesStage = new Stage();
        Parent root = null;

        try{
            root = FXMLLoader.load( getClass().getResource("Files.fxml"));
        }catch ( IOException ex){
            Main.LOGGER.log( Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }

        Scene scene = new Scene(root);
        filesStage.setTitle("FinishedGames");
        Image iconTitle = new Image (File.separator + "Images" + File.separator + "pic1.png");
        filesStage.getIcons().add(iconTitle);
        filesStage.setScene(scene);
        filesStage.show();
    }



    @FXML
    void showFigurePath(ActionEvent event) {

        System.out.println( event.getSource().toString());

        String[] lines = event.getSource().toString().split("(Figure)|'");
        int id = Integer.parseInt(lines[3]);
        Figure showingFigure;
        System.out.println(lines[3]);


        showingFigure = Main.MATRIX.players.get(id/4).figures.get(id%4);



        Stage figurePathStage = new Stage();
        Parent root = null;
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("FigurePath.fxml"));

        try{
            root = loader.load();
        }catch ( IOException ex){
            Main.LOGGER.log( Level.WARNING, ex.fillInStackTrace().toString(), ex);
        }

        Scene scene = new Scene(root);
        figurePathStage.setTitle("Figure Path");
        Image iconTitle = new Image (File.separator + "Images" + File.separator + "pic1.png");
        figurePathStage.getIcons().add(iconTitle);
        figurePathStage.setScene(scene);
        figurePathStage.show();
        FigurePathController fpc = loader.getController();
        fpc.show(showingFigure);

    }


        @FXML
        void handleStartBtn () {

            if (COUNT == 0) {
                timeline.play();
                Main.MATRIX.play();

            } else if (COUNT % 2 == 1) {
                timeline.pause();
                Main.GAME_PAUSE = true;

            } else {
                Main.GAME_PAUSE = false;
                synchronized (LOCK) {
                    LOCK.notifyAll();
                }
                timeline.play();
            }
            COUNT++;
        }
}