package main;


import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.awt.*;
import java.io.File;
import java.util.logging.Level;

public class FilesController {

    private String chosenFile = "";;

    @FXML
    private ListView<String> gamesList = new ListView<>();;


    @FXML
    void handleOpenBtn() {

        try {
            File file = new File("Resources" + File.separator + "Games" + File.separator + chosenFile);
            Desktop desktop = Desktop.getDesktop();
            if(file.exists()) {
                desktop.open(file);
            }
        } catch(Exception ex) {
            Main.LOGGER.log(Level.WARNING,ex.fillInStackTrace().toString(),ex);
        }

    }

    @FXML
    public void initialize() {

        gamesList.setEditable(true);
        File gamesFolder = new File("Resources" + File.separator + "Games");
        String[] files = gamesFolder.list();


        gamesList.getItems().addAll(files);

        gamesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        gamesList.getSelectionModel().selectedItemProperty().addListener(
                (arg0, arg1, arg2) -> {
                    chosenFile = gamesList.getSelectionModel().getSelectedItem();
                });
    }

}
