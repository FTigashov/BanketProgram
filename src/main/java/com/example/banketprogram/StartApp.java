package com.example.banketprogram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.Desktop;

public class StartApp extends Application {
    private Stage stage;
    public MainController mainController;
    public OptionsController optionsController;
    public Scene startScene;
    public Scene mainScene;

    public String URL = "";
    public String fileName = "";
    public String DB_URL = "D:\\JavaProjects\\BanketProgram\\src\\main\\resources\\DBNewFormat.xlsx";
    public String MENU_URL = "D:\\JavaProjects\\BanketProgram\\src\\main\\resources\\mainMenuBanket.xlsx";

    public String company = "";
    public String date = "";
    public String time = "";
    public String spot = "ЖР";
    public Integer guestCount = 0;

    public String companyLabel = "";
    public String dateLabel = "";
    public String timeLabel = "";
    public String spotLabel = "";
    public String guestCountLabel = "";
    public ProgressIndicator progressIndicator;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.setTitle("BanketProgram");
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.centerOnScreen();

        startScene = createStartScene();
        mainScene = createMainScene();

        stage.setTitle("BanketProgram");
        stage.setScene(startScene);
        stage.show();
    }

    private Scene createMainScene() throws IOException {
        URL location = getClass().getResource("mainView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        mainScene = new Scene(fxmlLoader.load());
        mainController = fxmlLoader.getController();
        mainController.setStartApp(this);
        return mainScene;
    }

    private Scene createStartScene() throws IOException {
        URL location = getClass().getResource("optionsView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        startScene = new Scene(fxmlLoader.load());
        optionsController = fxmlLoader.getController();
        optionsController.setStartApp(this);
        return startScene;
    }

    public static void main(String[] args) {
        launch();
    }

    public void showErrorShowMenu() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка открытия категории меню");
        alert.setHeaderText("Не удалось открыть выбранную категорию.");
        alert.setContentText("Необходимо выбрать категорию из выпадающего меню.\nПри открытии 'не выбрано' вызывается ошибка.");
        alert.show();
    }

    public void showErrorAddToNewMenu() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка добавления блюда");
        alert.setHeaderText("Не удалось добавить блюдо.");
        alert.setContentText("Убедитесь, что вы выбрали блюдо из меню,\nа также указали его количество.");
        alert.show();
    }

    public void showErrorChangeRow() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка изменения записи");
        alert.setHeaderText("Не удалось изменить количество.");
        alert.setContentText("Убедитесь, что количество установлено корректно.");
        alert.show();
    }

    public void showErrorAddingDuplicateIntoNewMenu(String dishName) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка добавления блюда");
        alert.setHeaderText("Не удалось добавить блюдо.");
        alert.setContentText("Блюдо " + dishName + "\nуже есть в меню.");
        alert.show();
    }

    public void showErrorDeleteRow() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка удаления блюда");
        alert.setHeaderText("Не удалось удалить блюдо.");
        alert.setContentText("Для удаления необходимо выбрать блюдо.");
        alert.show();
    }

    public void showSuccessMessageAboutDeletedTable() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Сообщение об успехе");
        alert.setHeaderText("Таблица успешно очищена.");
        alert.show();
    }

    public String choosePath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        if (file != null) {
            URL = file.getAbsolutePath();
            return URL;
        }
        return null;
    }

    public String chooseDBFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор базы");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            DB_URL = selectedFile.getAbsolutePath();
            return DB_URL;
        }
        return null;
    }

    public String chooseMenuFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор меню");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            MENU_URL = selectedFile.getAbsolutePath();
            return MENU_URL;
        }
        return null;
    }

    public void showErrorStartBanket() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Сообщение об ошибке");
        alert.setHeaderText("Ошибка начала рассчета банкета");
        alert.setContentText("Необходимо выбрать путь хранения и название файла.\nА также убедиться, что выбраны файлы базы и меню");
        alert.show();
    }

    public void setMainScene() {
        stage.setScene(mainScene);
        stage.centerOnScreen();
    }

    public void openConnectionToDBMenu() {
        mainController.connectToDBMenu();
    }

    public void openConnectionToMenu() {
        mainController.connectToMenuFile();
    }


    public void showErrorCreatingFile() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Сообщение об ошибке");
        alert.setHeaderText("Ошибка рассчета банкета");
        alert.setContentText("Необходимо составить меню прежде,\nчем производить рассчет.");
        alert.show();
    }

    public void showSuccessMessageAboutCreatedFile() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Сообщение об успехе");
        alert.setHeaderText("Банкет успешно посчитан и записан в файл с названием " + fileName + ".xlsx");
        alert.show();
        Desktop.getDesktop().open(new File(URL));
    }

    public void setOptionsScene() {
        stage.setScene(startScene);
        stage.centerOnScreen();
    }

    public void stopDBStream() throws IOException {
        DB_URL = "";
        mainController.dbFile.close();
        mainController.bufferedDBInputStream.close();

    }

    public void stopMenuStream() throws IOException {
        MENU_URL = "";
        mainController.file.close();
        mainController.bufferedFileInputStream.close();
        mainController.clearOblist();
    }
}