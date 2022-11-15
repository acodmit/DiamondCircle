package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

import javafx.util.Duration;
import map.Matrix;
import card.RegularCard;
import card.SpecialCard;
import figure.HoveringFigure;
import figure.RegularFigure;

import static map.Matrix.LOCK;


public class GameController {

    public static double FIELD_SIZE;

    public static File GAMES_FOLDER = null;

    @FXML
    AnchorPane anchorCard;

    @FXML
    private Button btnFigure1;

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
    private Button btnFigure16;

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
    GridPane gridGame;

    @FXML
    private Label lblCurrentCard;

    @FXML
    private Label lblCurrentFigure;

    @FXML
    private Label lblCurrentPlayer;

    @FXML
    private Label lblNumberOfFinishedGames;

    @FXML
    private Label lblPlayer1;

    @FXML
    private Label lblPlayer2;

    @FXML
    private Label lblPlayer3;

    @FXML
    private Label lblPlayer4;


    public void initialize() {
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
            btnFigure13.setVisible(false);
            btnFigure14.setVisible(false);
            btnFigure15.setVisible(false);
            btnFigure16.setVisible(false);
        }
        if (number_of_figures <= 8) {
            btnFigure9.setVisible(false);
            btnFigure10.setVisible(false);
            btnFigure11.setVisible(false);
            btnFigure12.setVisible(false);
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

        for (int j = 0; j < Matrix.MATRIX_SIZE; j++) {
            for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
                ImageView fieldView = new ImageView();
                fieldView.setFitWidth(FIELD_SIZE);
                fieldView.setFitHeight(FIELD_SIZE);
                gridGame.add(fieldView, i, j);
                Label lblNumber = new Label(Integer.toString(Matrix.FIELDS[i][j].getNumber()));
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
            GAMES_FOLDER = new File("/C:/Users/Lenovo/IdeaProjects/DiamondCircle5/Resources/Games/");
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


        synchronized (Matrix.LOCK) {
            while (Main.GAME_PAUSE) {
                try {
                    Matrix.LOCK.wait();
                } catch (InterruptedException ex) {
                    Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(), ex);
                }
            }

            Platform.runLater(() -> gridGame.getChildren().removeIf(node -> node instanceof ImageView));

            if (Main.CURRENT_PLAYER != null)
                Platform.runLater(() -> lblCurrentPlayer.setText(Main.CURRENT_PLAYER.toString()));
            if (Main.CURRENT_CARD != null)
                Platform.runLater(() -> lblCurrentCard.setText(Main.CURRENT_CARD.toString()));
            if (Main.CURRENT_FIGURE != null)
                Platform.runLater(() -> lblCurrentFigure.setText(Main.CURRENT_FIGURE.toString()));

            String folder = "file:/C:/Users/Lenovo/IdeaProjects/DiamondCircle4/Resources/Images/";
            if (Main.CURRENT_CARD instanceof RegularCard) {
                if (((RegularCard) Main.CURRENT_CARD).getNumber() == 1) {
                    showCard(folder + "card_1.png");
                } else if (((RegularCard) Main.CURRENT_CARD).getNumber() == 2) {
                    showCard(folder + "card_2.png");
                } else if (((RegularCard) Main.CURRENT_CARD).getNumber() == 3) {
                    showCard(folder + "card_3.png");
                } else {
                    showCard(folder + "card_4.png");
                }
            } else if (Main.CURRENT_CARD instanceof SpecialCard) {
                showCard(folder + "special.png");
            }
            for (int j = 0; j < Matrix.MATRIX_SIZE; j++) {
                for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
                    if (Matrix.FIELDS[i][j].getHole()) {
                        showField(folder + "hole.png", i, j);
                    } else if (Matrix.FIELDS[i][j].getDiamond()) {
                        showField(folder + "diamond.png", i, j);
                    } else if (Matrix.FIELDS[i][j].getFigure() != null) {

                        if ("YELLOW".equals(Matrix.FIELDS[i][j].getFigure().getColour().toString())) {

                            if (Matrix.FIELDS[i][j].getFigure() instanceof RegularFigure) {
                                showField(folder + "regular_yellow.png", i, j);
                            } else if (Matrix.FIELDS[i][j].getFigure() instanceof HoveringFigure) {
                                showField(folder + "hovering_yellow.png", i, j);
                            } else {
                                showField(folder + "super_fast_yellow.png", i, j);
                            }
                        } else if ("GREEN".equals(Main.CURRENT_FIGURE.getColour().toString())) {

                            if (Matrix.FIELDS[i][j].getFigure() instanceof RegularFigure) {
                                showField(folder + "regular_green.png", i, j);
                            } else if (Matrix.FIELDS[i][j].getFigure() instanceof HoveringFigure) {
                                showField(folder + "hovering_green.png", i, j);
                            } else {
                                showField(folder + "super_fast_green.png", i, j);
                            }
                        } else if ("RED".equals(Main.CURRENT_FIGURE.getColour().toString())) {

                            if (Matrix.FIELDS[i][j].getFigure() instanceof RegularFigure) {
                                showField(folder + "regular_red.png", i, j);
                            } else if (Matrix.FIELDS[i][j].getFigure() instanceof HoveringFigure) {
                                showField(folder + "hovering_red.png", i, j);
                            } else {
                                showField(folder + "super_fast_red.png", i, j);
                            }
                        } else {

                            if (Matrix.FIELDS[i][j].getFigure() instanceof RegularFigure) {
                                showField(folder + "regular_blue.png", i, j);
                            } else if (Matrix.FIELDS[i][j].getFigure() instanceof HoveringFigure) {
                                showField(folder + "hovering_blue.png", i, j);
                            } else {
                                showField(folder + "super_fast_blue.png", i, j);
                            }

                        }
                    }
                }
            }

            Matrix.LOCK.notifyAll();
        }

    }
        @FXML
        void start () {

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> mapRefresh()));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

            Main.MATRIX.play();

        }

}