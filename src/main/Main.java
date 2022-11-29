package main;

import card.Card;
import figure.Figure;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import map.Matrix;
import player.Player;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Main extends Application {

    public static Logger LOGGER = Logger.getLogger("LOGGER");

    public static Player CURRENT_PLAYER;

    public static Card CURRENT_CARD;

    public static Figure CURRENT_FIGURE;

    public static long CURRENT_TIME;

    public static Boolean GAME_PAUSE = false;

    public static Boolean GAME_FINISHED = false;

    public static Matrix MATRIX = null;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("StartGame.fxml"));
        Scene scene = new Scene(root);
        Image iconTitle = new Image (File.separator + "Images" + File.separator + "pic1.png");
        primaryStage.getIcons().add(iconTitle);
        primaryStage.setTitle("DiamondCircle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        try {
            LOGGER.addHandler(new FileHandler("logs.log", true));
        }catch (Exception ex){
            LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
        }

        launch(args);

    }

}

