package com.example.banketprogram;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class OptionsController {

    @FXML
    private TextField chosenDBField;

    @FXML
    private TextField chosenMenuField;

    @FXML
    private Button clearDBPathBtn;

    @FXML
    private Button clearMenuPathBtn;

    @FXML
    private Button openDBPathBtn;

    @FXML
    private Button openMenuPathBtn;


    @FXML
    private Label companyLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label guestCountLabel;

    @FXML
    private Label spotLabel;

    @FXML
    private Label timeLabel;
    @FXML
    private TextField companyField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField fileNameField;

    @FXML
    private TextField filePathField;

    @FXML
    private TextField guestCountField;

    @FXML
    private Button openPathBtn;

    @FXML
    private TextField spotField;

    @FXML
    private Button startBanketProgram;

    @FXML
    public ProgressIndicator progressBar;

    @FXML
    private TextField timeField;

    @FXML
    void beginBanket(MouseEvent event) {
        if (filePathField.getText().length() == 0 || fileNameField.getText().length() == 0 || chosenDBField.getText().length() == 0 || chosenMenuField.getText().length() == 0) {
            startApp.showErrorStartBanket();
        } else {
            startApp.fileName = fileNameField.getText().trim();

            if (companyField.getText().length() != 0 ||
                    dateField.getText().length() != 0 ||
                    timeField.getText().length() != 0 ||
                    spotField.getText().length() != 0 ||
                    guestCountField.getText().length() != 0 ) {
                startApp.company = companyField.getText().trim();
                startApp.date = dateField.getText().trim();
                startApp.time = timeField.getText().trim();
                startApp.spot = spotField.getText().trim();
                startApp.guestCount = Integer.parseInt(guestCountField.getText().trim());
            }

            startApp.companyLabel = companyLabel.getText().trim();
            startApp.dateLabel = dateLabel.getText().trim();
            startApp.timeLabel = timeLabel.getText().trim();
            startApp.spotLabel = spotLabel.getText().trim();
            startApp.guestCountLabel = guestCountLabel.getText().trim();

            startApp.setMainScene();
        }
    }

    @FXML
    void clearChosenDB(MouseEvent event) throws IOException {
        chosenDBField.clear();
        startApp.stopDBStream();
    }

    @FXML
    void clearChosenMenu(MouseEvent event) throws IOException {
        chosenMenuField.clear();
        startApp.stopMenuStream();
    }

    @FXML
    void openDBPath(MouseEvent event) {
        chosenDBField.setText(startApp.chooseDBFile());
    }

    @FXML
    void openMenuPath(MouseEvent event) {
        chosenMenuField.setText(startApp.chooseMenuFile());
    }

    @FXML
    void openPath(MouseEvent event) {
        filePathField.setText(startApp.choosePath());
    }

    private StartApp startApp;

    public void setStartApp(StartApp startApp) {
        this.startApp = startApp;
    }

    @FXML
    void saveDBPath(MouseEvent event) {
        startApp.openConnectionToDBMenu();
    }

    @FXML
    void saveMenuPath(MouseEvent event) {
        startApp.openConnectionToMenu();
    }
}
