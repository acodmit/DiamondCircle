package main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import map.Matrix;


public class StartGameController implements Initializable{

    @FXML
    Parent root;
    @FXML
    Stage stage;
    @FXML
    Scene scene;

    @FXML
    private ChoiceBox<Integer> playersChoice;

    @FXML
    private ChoiceBox<String> matrixChoice;


    private final Integer[] players = { 2, 3, 4};

    private final String[] matrix_type = { "7x7", "8x8", "9x9", "10x10"};




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        playersChoice.getItems().addAll(players);
        matrixChoice.getItems().addAll(matrix_type);
    }

    public void startGame(ActionEvent event){

        if(matrixChoice.getValue() != null && playersChoice.getValue() != null){

            String line = matrixChoice.getValue();
            String[] lines = line.split("x");
            int players_number = playersChoice.getValue();

            Matrix.NUMBER_OF_PLAYERS =  players_number;
            Matrix.MATRIX_SIZE = Integer.parseInt(lines[0]);

            try{
                root = FXMLLoader.load(getClass().getResource("Game.fxml"));
            }catch (IOException ex ){
                Main.LOGGER.log(Level.WARNING, ex.fillInStackTrace().toString(),ex);
            }

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("DiamondCircle");
            stage.show();
        }
    }
}
