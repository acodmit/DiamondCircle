package main;

import figure.Figure;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import map.Matrix;

import java.io.File;

public class FigurePathController {

    @FXML
    private Label lblFigure;

    @FXML
    Stage stage;

    @FXML
    GridPane gridPath;

    //private Figure figure;

    @FXML
    public void initialize() {

        System.out.println("IN FIGURE PATH CONTROLLER.");

        for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
            ColumnConstraints column = new ColumnConstraints(GameController.FIELD_SIZE);
            RowConstraints row = new RowConstraints(GameController.FIELD_SIZE);
            gridPath.getColumnConstraints().add(column);
            gridPath.getRowConstraints().add(row);
        }
        gridPath.setGridLinesVisible(true);
    }

    public void show(Figure figure) {

        lblFigure.setText(figure.toString());


        Image fieldImage = new Image(File.separator + "Images" + File.separator + "path.png");
        ImageView fieldImageView;

        //fieldImageView.setFitWidth(GameController.FIELD_SIZE);
        //fieldImageView.setFitHeight(GameController.FIELD_SIZE);
        //gridPath.add(fieldImageView, 1, 1);


        for (int i = 0; i < Matrix.MATRIX_SIZE; i++) {
            for (int j = 0; j < Matrix.MATRIX_SIZE; j++) {

                if (figure.checkPath(Matrix.FIELDS[i][j].getNumber())) {
                    fieldImageView = new ImageView(fieldImage);
                    fieldImageView.setFitWidth(GameController.FIELD_SIZE);
                    fieldImageView.setFitHeight(GameController.FIELD_SIZE);
                    gridPath.add(fieldImageView, j, i);
                }
            }
        }
    }
}