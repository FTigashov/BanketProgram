package com.example.banketprogram;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.*;


public class MainController {
    @FXML
    private MenuItem exitBtn;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem optionsBtn;

    @FXML
    private Button cancelBtn;
    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;
    @FXML
    private Button addToNewMenuBtn;

    @FXML
    private TableColumn<NewMenuRecord, Double> allWeightCol;

    @FXML
    private TextField allWeightLabel;

    @FXML
    private Button clearTable;

    @FXML
    private TextField sumPrice;

    @FXML
    private TableColumn<NewMenuRecord, Double> countCol;

    @FXML
    private TextField countField;

    @FXML
    private TextField countField1;

    @FXML
    private Button createFile;

    @FXML
    private TableColumn<MainMenuRecord, String> dishNameCol;

    @FXML
    private TableColumn<NewMenuRecord, String> dishNameCol1;

    @FXML
    private TableColumn<NewMenuRecord, Double> sumPriceCol;

    @FXML
    private TextField dishNameLabel;

    @FXML
    private TextField dishNameLabel1;

    @FXML
    private TableView<MainMenuRecord> menuTable;

    @FXML
    private TableView<NewMenuRecord> newMenuTable;

    @FXML
    private TableColumn<MainMenuRecord, Double> priceCol;

    @FXML
    private TableColumn<NewMenuRecord, Double> priceCol1;

    @FXML
    private TextField priceLabel;

    @FXML
    private TextField priceLabel1;

    @FXML
    private Button removeCurrentRow;

    @FXML
    private Button saveChangesInRow;

    @FXML
    private TableColumn<MainMenuRecord, Double> weightCol;

    @FXML
    private TableColumn<NewMenuRecord, Double> weightCol1;

    @FXML
    private TextField weightLabel;

    @FXML
    private TextField weightLabel1;


    @FXML
    private ChoiceBox<String> dishCategory;
    public FileInputStream file;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private Row row;


    private XSSFSheet newMenuSheet;
    private XSSFSheet dishesWithProductsSheet;
    private XSSFSheet listOfProductsSheet;

    public  int indexInDataBook = 7;
    public  int indexForNextDish = 7;
    public  int indexForProducts = 7;
    private String dishName;

    public double col1;
    public double col2;
    public double col3;
    public double col4;
    public double col5;
    private int count;
    private XSSFSheet dataBaseSheet;
    public FileInputStream dbFile;
    private XSSFWorkbook dataBaseBook;

    private List<String> listLeftDishes = new ArrayList<>();
    private FileOutputStream doneFileOutputStream;
    private CellStyle defaultBorderStyle;
    private CellStyle nameBoldStyle;
    private CellStyle beginTableNameBoldStyle;
    public BufferedInputStream bufferedFileInputStream;
    public BufferedInputStream bufferedDBInputStream;
    private DataInputStream fileInput;


    @FXML
    public void initialize() {
        categoryInit();
        launchChoiceRowInMainTable();
        launchChoiceRowInNewTable();
//        connectToMenuFile();
//        connectToDBMenu();
//        openMiniSnacksAndKanape();
        searchMethod();



    }

    @FXML
    void exitProgram(ActionEvent event) {
        ButtonType yes = new ButtonType("Да", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Отмена", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", yes, no);
        alert.setTitle("Выход из приложения");
        alert.setHeaderText("Вы уверены, что хотите выйти?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == yes) System.exit(0);
        else if (option.get() == no) alert.close();
    }

    @FXML
    void openOptionsView(ActionEvent event) {
        startApp.setOptionsScene();
    }

    private void categoryInit() {
        dishCategory.setValue("Мини-закуски, канапе");
        dishCategory.getItems().add("Мини-закуски, канапе");
        dishCategory.getItems().add("Холодные закуски");
        dishCategory.getItems().add("Салаты");
        dishCategory.getItems().add("Горячие закуски");
        dishCategory.getItems().add("Паста и Ризотто");
        dishCategory.getItems().add("Рыба и морепродукты(за 100г)");
        dishCategory.getItems().add("Горячие блюда");
        dishCategory.getItems().add("Блюда на углях(за 100г)");
        dishCategory.getItems().add("Горячие блюда на банкет");
        dishCategory.getItems().add("Гарниры");
        dishCategory.getItems().add("Пицца");
        dishCategory.getItems().add("Торт 1кг");
        dishCategory.getItems().add("Мини-пирожные, печенье, конфеты");
        dishCategory.getItems().add("Десерт");
        dishCategory.getItems().add("Напитки");
    }

    private void searchMethod() {
        searchField.setOnKeyReleased(event -> {
            if (searchField.getText().isEmpty()) openMiniSnacksAndKanape();
            else {
                oblist.clear();
                String searchText = searchField.getText().trim();
                XSSFSheet sheet = workbook.getSheetAt(0);
                for (int rowNum = 13; rowNum < 307; rowNum++) {
                    Row getTextRow = sheet.getRow(rowNum);
                    if (getTextRow != null && getTextRow.getCell(0) != null && getTextRow.getCell(0).getCellType() != null) {
                        Cell dishNameCell = getTextRow.getCell(0);
                        String getText = dishNameCell.getStringCellValue().trim();
                        if (getText.toLowerCase().startsWith(searchText.toLowerCase()) || getText.toLowerCase().contains(searchText.toLowerCase())) {
                            if (getTextRow.getCell(1) != null) {
                                String dishWeight = String.format("%.0f", getTextRow.getCell(1).getNumericCellValue());
                                String dishPrice = String.format("%.0f", getTextRow.getCell(4).getNumericCellValue());
                                oblist.add(new MainMenuRecord(getText, dishWeight, dishPrice));
                            }
                        }
                    } else continue;
                }
                setMainTable(oblist);
            }
        });
    }

    public void connectToMenuFile() {
        try {
            fileInput = new DataInputStream(new BufferedInputStream(new FileInputStream(startApp.MENU_URL), 200));
            workbook = new XSSFWorkbook(fileInput);
            System.out.println("Меню подключено");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectToDBMenu() {
        try {
            DataInputStream DBInput = new DataInputStream(new BufferedInputStream(new FileInputStream(startApp.DB_URL), 200));
            dataBaseBook = new XSSFWorkbook(DBInput);
            System.out.println("База подключена");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void launchChoiceRowInMainTable() {
        menuTable.setRowFactory(event -> {
            TableRow<MainMenuRecord> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                clearFieldToChange();
                MainMenuRecord clickedRow = row.getItem();
                if (clickedRow != null) {
                    dishNameLabel.setText(clickedRow.getDishName().trim());
                    weightLabel.setText(String.valueOf(clickedRow.getWeight()));
                    priceLabel.setText(String.valueOf(clickedRow.getPrice()));
                }
            });
            return row;
        });
    }

    private void launchChoiceRowInNewTable() {
        newMenuTable.setRowFactory(event -> {
            TableRow<NewMenuRecord> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                clearFieldsToAdd();
                NewMenuRecord clickedRow = row.getItem();
                if (clickedRow != null) {
                    dishNameLabel1.setText(clickedRow.getDishName().trim());
                    weightLabel1.setText(clickedRow.getWeight());
                    priceLabel1.setText(clickedRow.getPrice());
                    countField1.setText(clickedRow.getCount());
                    sumPrice.setText(clickedRow.getSumPrice());
                    allWeightLabel.setText(clickedRow.getAllWeight());
                }
            });
            return row;
        });
    }

    private ObservableList<MainMenuRecord> oblist = FXCollections.observableArrayList();
    private ObservableList<NewMenuRecord> oblistForNewMenu = FXCollections.observableArrayList();

    @FXML
    void openCurrentCategory(MouseEvent event) {
        clearFieldsToAdd();
        switch (dishCategory.getValue().trim()) {
            case "Мини-закуски, канапе":
                openMiniSnacksAndKanape();
                break;
            case "Холодные закуски":
                openColdSnacks();
                break;
            case "Салаты":
                openSaladCategory();
                break;
            case "Горячие закуски":
                openHotSnacks();
                break;
            case "Паста и Ризотто":
                openPastaAndRisotto();
                break;
            case "Рыба и морепродукты(за 100г)":
                openFishAndSeaProductsCategory();
                break;
            case "Горячие блюда":
                openHotDishesCategory();
                break;
            case "Блюда на углях(за 100г)":
                openMangalDishes();
                break;
            case "Горячие блюда на банкет":
                openHotDishesOnBanket();
                break;
            case "Гарниры":
                openGarnirsCategory();
                break;
            case "Пицца":
                openPizzaCategory();
                break;
            case "Торт 1кг":
                openCakeCategory();
                break;
            case "Мини-пирожные, печенье, конфеты":
                openMiniCakesCategory();
                break;
            case "Десерт":
                dessertCategory();
                break;
            case "Напитки":
                drinksCategory();
                break;
        }
    }

    private void openMiniSnacksAndKanape() {
        oblist.clear();
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int rowNum = 13; rowNum < 62; rowNum++) {
            Row row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openColdSnacks() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 63; rowNum < 103; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openSaladCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 105; rowNum < 147; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openHotSnacks() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 148; rowNum < 167; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openPastaAndRisotto() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 168; rowNum < 176; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }
    private void drinksCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 297; rowNum < 307; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void dessertCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 285; rowNum < 296; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openMiniCakesCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 270; rowNum < 284; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openCakeCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 256; rowNum < 269; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openPizzaCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 241; rowNum < 255; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openGarnirsCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 228; rowNum < 240; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openHotDishesOnBanket() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 214; rowNum < 224; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openMangalDishes() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 207; rowNum < 216; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openHotDishesCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 217; rowNum < 227; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }

    private void openFishAndSeaProductsCategory() {
        oblist.clear();
        sheet = workbook.getSheetAt(0);
        for (int rowNum = 177; rowNum < 187; rowNum++) {
            row = sheet.getRow(rowNum);
            String dishName = row.getCell(0).getStringCellValue().trim();
            String dishWeight = String.format("%.0f", row.getCell(1).getNumericCellValue());
            String dishPrice = String.format("%.0f", row.getCell(4).getNumericCellValue());
            oblist.add(new MainMenuRecord(dishName, dishWeight, dishPrice));
        }
        setMainTable(oblist);
    }


    public void setMainTable(ObservableList<MainMenuRecord> oblist) {
        dishNameCol.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        menuTable.setItems(oblist);
    }

    @FXML
    void addToNewMenu(MouseEvent event) {
        if (dishNameLabel.getText().isEmpty() || countField.getText().isEmpty()) {
            startApp.showErrorAddToNewMenu();
        } else {
            String dishNameForAdd = dishNameLabel.getText().trim();
            String weightToAdd = weightLabel.getText().trim();
            String priceToAdd = priceLabel.getText().trim();
            String countToAdd = countField.getText().trim();
            String allWeightToAdd = String.format("%.0f", Double.parseDouble(weightToAdd) * Double.parseDouble(countToAdd));
            String sumPriceToAdd = String.format("%.0f", Double.parseDouble(priceToAdd) * Double.parseDouble(countToAdd));

            for (NewMenuRecord element : oblistForNewMenu) {
                if (element.getDishName().equals(dishNameForAdd)) {
                    startApp.showErrorAddingDuplicateIntoNewMenu(dishNameForAdd);
                    clearFieldsToAdd();
                    return;
                }
            }
            oblistForNewMenu.add(new NewMenuRecord(dishNameForAdd,
                    weightToAdd,
                    priceToAdd,
                    countToAdd,
                    allWeightToAdd,
                    sumPriceToAdd));
            setNewTableMenu(oblistForNewMenu);
            clearFieldsToAdd();
        }
    }

    private void setNewTableMenu(ObservableList<NewMenuRecord> oblistForNewMenu) {
//        newMenuTable.getItems().clear();

        dishNameCol1.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        weightCol1.setCellValueFactory(new PropertyValueFactory<>("weight"));
        priceCol1.setCellValueFactory(new PropertyValueFactory<>("price"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        allWeightCol.setCellValueFactory(new PropertyValueFactory<>("allWeight"));
        sumPriceCol.setCellValueFactory(new PropertyValueFactory<>("sumPrice"));

        newMenuTable.setItems(oblistForNewMenu);
    }

    private void clearFieldsToAdd() {
        dishNameLabel.setText("");
        weightLabel.setText("");
        priceLabel.setText("");
        countField.setText("");
    }

    private void clearFieldToChange() {
        dishNameLabel1.setText("");
        weightLabel1.setText("");
        priceLabel1.setText("");
        countField1.setText("");
        sumPrice.setText("");
        allWeightLabel.setText("");
    }

    @FXML
    void saveChangesInRow(MouseEvent event) {
        NewMenuRecord record = newMenuTable.getSelectionModel().getSelectedItem();
        oblistForNewMenu.remove(record);
        //TODO
        oblistForNewMenu.add(new NewMenuRecord(dishNameLabel1.getText().trim(),
                weightLabel1.getText().trim(),
                priceLabel1.getText().trim(),
                countField1.getText().trim(),
                String.format("%.0f", Double.parseDouble(countField1.getText().trim()) * Double.parseDouble(weightLabel1.getText().trim())),
                String.format("%.0f", Double.parseDouble(countField1.getText().trim()) * Double.parseDouble(priceLabel1.getText().trim()))));
        setNewTableMenu(oblistForNewMenu);
        clearFieldToChange();
    }

    @FXML
    void DeleteThisRow(MouseEvent event) {
        NewMenuRecord record = newMenuTable.getSelectionModel().getSelectedItem();
        if (record == null) {
            startApp.showErrorDeleteRow();
            return;
        } else {
            oblistForNewMenu.remove(record);
            setNewTableMenu(oblistForNewMenu);
            clearFieldToChange();
        }
    }

    @FXML
    void clearTable(MouseEvent event) {
        newMenuTable.getItems().clear();
        oblistForNewMenu.clear();
        startApp.showSuccessMessageAboutDeletedTable();
    }

    @FXML
    void makeFile(MouseEvent event) {

        try {
            doneFileOutputStream = new FileOutputStream(startApp.URL + "\\" + startApp.fileName + ".xlsx");
            XSSFWorkbook newBook = new XSSFWorkbook();

            nameBoldStyle = newBook.createCellStyle();
            Font font = newBook.createFont();
            font.setBold(true);
            nameBoldStyle.setFont(font);
            nameBoldStyle.setBorderTop(BorderStyle.THIN);
            nameBoldStyle.setBorderRight(BorderStyle.THIN);
            nameBoldStyle.setBorderLeft(BorderStyle.THIN);
            nameBoldStyle.setBorderBottom(BorderStyle.THIN);

            beginTableNameBoldStyle = newBook.createCellStyle();
            beginTableNameBoldStyle.setFont(font);
            beginTableNameBoldStyle.setBorderTop(BorderStyle.THICK);
            beginTableNameBoldStyle.setBorderRight(BorderStyle.THICK);
            beginTableNameBoldStyle.setBorderLeft(BorderStyle.THICK);
            beginTableNameBoldStyle.setBorderBottom(BorderStyle.THICK);

            newMenuSheet = newBook.createSheet("Меню на банкет");
            dishesWithProductsSheet = newBook.createSheet("Вхождение продуктов");
            listOfProductsSheet = newBook.createSheet("Продукты на банкет");

            defaultBorderStyle = newBook.createCellStyle();
            defaultBorderStyle.setBorderTop(BorderStyle.THIN);
            defaultBorderStyle.setBorderRight(BorderStyle.THIN);
            defaultBorderStyle.setBorderLeft(BorderStyle.THIN);
            defaultBorderStyle.setBorderBottom(BorderStyle.THIN);

            for (int i = 0; i < 5; i++) {
                Row rowInNewMenuSheet = newMenuSheet.createRow(i);
                Row rowInDishesWithProductsSheet = dishesWithProductsSheet.createRow(i);
                Row rowInListOfProductsSheet = listOfProductsSheet.createRow(i);
                switch (i) {
                    case 0:
                        createEnterInfo(rowInNewMenuSheet, startApp.companyLabel, startApp.company);
                        createEnterInfo(rowInDishesWithProductsSheet, startApp.companyLabel, startApp.company);
                        createEnterInfo(rowInListOfProductsSheet, startApp.companyLabel, startApp.company);
                        break;
                    case 1:
                        createEnterInfo(rowInNewMenuSheet, startApp.dateLabel, startApp.date);
                        createEnterInfo(rowInDishesWithProductsSheet, startApp.dateLabel, startApp.date);
                        createEnterInfo(rowInListOfProductsSheet, startApp.dateLabel, startApp.date);
                        break;
                    case 2:
                        createEnterInfo(rowInNewMenuSheet, startApp.timeLabel, startApp.time);
                        createEnterInfo(rowInDishesWithProductsSheet, startApp.timeLabel, startApp.time);
                        createEnterInfo(rowInListOfProductsSheet, startApp.timeLabel, startApp.time);
                        break;
                    case 3:
                        createEnterInfo(rowInNewMenuSheet, startApp.spotLabel, startApp.spot);
                        createEnterInfo(rowInDishesWithProductsSheet, startApp.spotLabel, startApp.spot);
                        createEnterInfo(rowInListOfProductsSheet, startApp.spotLabel, startApp.spot);
                        break;
                    case 4:
                        createEnterInfo(rowInNewMenuSheet, startApp.guestCountLabel, startApp.guestCount);
                        createEnterInfo(rowInDishesWithProductsSheet, startApp.guestCountLabel, startApp.guestCount);
                        createEnterInfo(rowInListOfProductsSheet, startApp.guestCountLabel, startApp.guestCount);
                        break;
                }
            }

            makeBreakBetweenInfoAndMenu();

            if (oblistForNewMenu.size() == 0) {
                startApp.showErrorCreatingFile();
                return;
            } else {
                int rowNum = 7;
                for (int i = 0; i < oblistForNewMenu.size(); i++) {
                    Row myRow = newMenuSheet.createRow(rowNum);
                    dishName = oblistForNewMenu.get(i).getDishName();
                    count = Integer.parseInt(oblistForNewMenu.get(i).getCount());
                    myRow.createCell(0).setCellValue(dishName);
                    myRow.getCell(0).setCellStyle(defaultBorderStyle);
                    myRow.createCell(1).setCellValue(Integer.parseInt(oblistForNewMenu.get(i).getWeight()));
                    myRow.getCell(1).setCellStyle(defaultBorderStyle);
                    myRow.createCell(2).setCellValue(count);
                    myRow.getCell(2).setCellStyle(defaultBorderStyle);
                    myRow.createCell(3).setCellValue(Integer.parseInt(oblistForNewMenu.get(i).getAllWeight()));
                    myRow.getCell(3).setCellStyle(defaultBorderStyle);
                    myRow.createCell(4).setCellValue(Integer.parseInt(oblistForNewMenu.get(i).getPrice()));
                    myRow.getCell(4).setCellStyle(defaultBorderStyle);
                    myRow.createCell(5).setCellValue(Integer.parseInt(oblistForNewMenu.get(i).getSumPrice()));
                    myRow.getCell(5).setCellStyle(defaultBorderStyle);
                    makeSecondAndThirdSheet();
                    rowNum += 1;
                }
            }
            removeDuplicates();
            autoSizeColumns();

            //TODO
            newBook.write(doneFileOutputStream);
            doneFileOutputStream.close();
            setDefaultCounters();
//            startApp.showSuccessMessageAboutCreatedFile();

            if (listLeftDishes.size() != 0) {
                System.out.println("Блюда, которые не были найдены: ");
                for (String leftDish : listLeftDishes) {
                    System.out.println(leftDish);
                }
            }
            System.out.println("Сделано!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void setDefaultCounters() {
        indexInDataBook = 7;
        indexForNextDish = 7;
        indexForProducts = 7;
    }

    private void removeDuplicates() {
        int length = 1500;
        for (int rowNum = 7; rowNum < length; rowNum++) {
            Row row = listOfProductsSheet.getRow(rowNum);
            if (row != null) {
                Cell cell = row.getCell(0);
                Cell valueCell = row.getCell(1);
                double valueCellMain = valueCell.getNumericCellValue();

                for (int rowForView = rowNum + 1; rowForView < length;) {
                    Row rowView = listOfProductsSheet.getRow(rowForView);
                    if (rowView != null) {
                        Cell cellView = rowView.getCell(0);
                        String cellText = cell.getStringCellValue().trim();
                        String cellViewText = cellView.getStringCellValue().trim();
                        if (cellText.equals(cellViewText)) {
                            Cell valueCellView = rowView.getCell(1);
                            double valueCellViewToAdd = valueCellView.getNumericCellValue();
                            valueCellMain += valueCellViewToAdd;
                            row.getCell(1).setCellValue(valueCellMain);
                            listOfProductsSheet.removeRow(rowView);
                            listOfProductsSheet.shiftRows(rowForView + 1, length, -1);
                        } else {
                            rowForView += 1;
                        }
                    } else break;
                }
            } else break;
        }
    }

    private void makeSecondAndThirdSheet() {
        indexInDataBook = findIndex(dishName);
        if (indexInDataBook < 0) {
            System.out.println("Блюдо " + dishName + " не найдено!");
            listLeftDishes.add(dishName);
            return;
        } else {
            System.out.println("Индекс блюда " + indexInDataBook);
            Row currentRow = dishesWithProductsSheet.createRow(indexForNextDish);
            currentRow.createCell(0).setCellValue(dishName);
            currentRow.createCell(1).setCellValue(col1);
            currentRow.createCell(2).setCellValue(count);
            currentRow.createCell(3).setCellValue(col1 * count);
            currentRow.createCell(4).setCellValue(col4);
            currentRow.createCell(5).setCellValue(col4 * count);

            currentRow.getCell(0).setCellStyle(nameBoldStyle);
            currentRow.getCell(1).setCellStyle(nameBoldStyle);
            currentRow.getCell(2).setCellStyle(nameBoldStyle);
            currentRow.getCell(3).setCellStyle(nameBoldStyle);
            currentRow.getCell(4).setCellStyle(nameBoldStyle);
            currentRow.getCell(5).setCellStyle(nameBoldStyle);

            indexForNextDish += 1;
            findProducts();
        }
    }

    private void findProducts() {
        for (int i = indexInDataBook + 1; i < 1000; i++) {
            Row row = dataBaseSheet.getRow(i);
            if (row != null && row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK) {
                System.out.println("Индекс продукта " + i);
                Row newRow = dishesWithProductsSheet.createRow(indexForNextDish);
                newRow.createCell(0).setCellValue(row.getCell(0).getStringCellValue().trim());
                newRow.createCell(1).setCellValue(row.getCell(1).getNumericCellValue());
                newRow.createCell(2).setCellValue(count);
                newRow.createCell(3).setCellValue(row.getCell(1).getNumericCellValue() * count);
                newRow.createCell(4).setCellValue(row.getCell(4).getNumericCellValue());
                newRow.createCell(5).setCellValue(row.getCell(4).getNumericCellValue() * count);

                newRow.getCell(0).setCellStyle(defaultBorderStyle);
                newRow.getCell(1).setCellStyle(defaultBorderStyle);
                newRow.getCell(2).setCellStyle(defaultBorderStyle);
                newRow.getCell(3).setCellStyle(defaultBorderStyle);
                newRow.getCell(4).setCellStyle(defaultBorderStyle);
                newRow.getCell(5).setCellStyle(defaultBorderStyle);

                Row productRow = listOfProductsSheet.createRow(indexForProducts);
                productRow.createCell(0).setCellValue(row.getCell(0).getStringCellValue().trim());
                productRow.createCell(1).setCellValue(row.getCell(1).getNumericCellValue() * count);
                productRow.createCell(2);

                productRow.getCell(0).setCellStyle(defaultBorderStyle);
                productRow.getCell(1).setCellStyle(defaultBorderStyle);
                productRow.getCell(2).setCellStyle(defaultBorderStyle);

                indexForNextDish += 1;
                indexForProducts += 1;
            } else {
                Row emptyRow = dishesWithProductsSheet.createRow(indexForNextDish);
                emptyRow.createCell(0).setCellStyle(defaultBorderStyle);
                emptyRow.createCell(1).setCellStyle(defaultBorderStyle);
                emptyRow.createCell(2).setCellStyle(defaultBorderStyle);
                emptyRow.createCell(3).setCellStyle(defaultBorderStyle);
                emptyRow.createCell(4).setCellStyle(defaultBorderStyle);
                emptyRow.createCell(5).setCellStyle(defaultBorderStyle);
                indexForNextDish += 1;
                break;
            }
        }
    }

    private int findIndex(String dishName) {
        System.out.println("currDish: " + dishName);
        for (int sheetNum = 0; sheetNum < 12; sheetNum++) {
            dataBaseSheet = dataBaseBook.getSheetAt(sheetNum);
            for (int i = 3; i < 2000; i++) {
//                System.out.println(i);
                Row row = dataBaseSheet.getRow(i);
                if (row != null && row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK) {
                    String checkWord = row.getCell(0).getStringCellValue().trim();
//                    System.out.println("CheckWord " + checkWord);
                    if (checkWord.equals(dishName)) {
                        System.out.println("Здесь");
                        col1 = row.getCell(1).getNumericCellValue();
                        System.out.println(col1);
                        col2 = row.getCell(2).getNumericCellValue();
                        col3 = row.getCell(3).getNumericCellValue();
                        col4 = row.getCell(4).getNumericCellValue();
                        col5 = row.getCell(5).getNumericCellValue();
                        return i;
                    }
                } else {
//                    System.out.println("Ряд пуст");
                }
            }
        }
        return -1;
    }

    private void makeBreakBetweenInfoAndMenu() {
        Row rowInNewMenuSheet = newMenuSheet.createRow(5);
        Row rowInDishesWithProductsSheet = dishesWithProductsSheet.createRow(5);
        Row rowInListOfProductsSheet = listOfProductsSheet.createRow(5);

        createSpaceBetween(rowInNewMenuSheet);
        createSpaceBetween(rowInDishesWithProductsSheet);
        createSpaceBetween(rowInListOfProductsSheet);

        rowInNewMenuSheet = newMenuSheet.createRow(6);
        rowInDishesWithProductsSheet = dishesWithProductsSheet.createRow(6);
        rowInListOfProductsSheet = listOfProductsSheet.createRow(6);

        rowInNewMenuSheet.createCell(0).setCellValue("Наименование блюд");
        rowInNewMenuSheet.createCell(1).setCellValue("Брутто 1 ед. гр");
        rowInNewMenuSheet.createCell(2).setCellValue("Кол-во ед.");
        rowInNewMenuSheet.createCell(3).setCellValue("Вых. Брутто, гр");
        rowInNewMenuSheet.createCell(4).setCellValue("Стоимость руб");
        rowInNewMenuSheet.createCell(5).setCellValue("Сумма руб");

        rowInNewMenuSheet.getCell(0).setCellStyle(beginTableNameBoldStyle);
        rowInNewMenuSheet.getCell(1).setCellStyle(beginTableNameBoldStyle);
        rowInNewMenuSheet.getCell(2).setCellStyle(beginTableNameBoldStyle);
        rowInNewMenuSheet.getCell(3).setCellStyle(beginTableNameBoldStyle);
        rowInNewMenuSheet.getCell(4).setCellStyle(beginTableNameBoldStyle);
        rowInNewMenuSheet.getCell(5).setCellStyle(beginTableNameBoldStyle);

        rowInDishesWithProductsSheet.createCell(0).setCellValue("Наименование блюд");
        rowInDishesWithProductsSheet.createCell(1).setCellValue("Брутто 1 ед. гр");
        rowInDishesWithProductsSheet.createCell(2).setCellValue("Кол-во ед.");
        rowInDishesWithProductsSheet.createCell(3).setCellValue("Вых. Брутто, гр");
        rowInDishesWithProductsSheet.createCell(4).setCellValue("Нетто 1 ед. гр");
        rowInDishesWithProductsSheet.createCell(5).setCellValue("Вых. Нетто, гр");

        rowInDishesWithProductsSheet.getCell(0).setCellStyle(beginTableNameBoldStyle);
        rowInDishesWithProductsSheet.getCell(1).setCellStyle(beginTableNameBoldStyle);
        rowInDishesWithProductsSheet.getCell(2).setCellStyle(beginTableNameBoldStyle);
        rowInDishesWithProductsSheet.getCell(3).setCellStyle(beginTableNameBoldStyle);
        rowInDishesWithProductsSheet.getCell(4).setCellStyle(beginTableNameBoldStyle);
        rowInDishesWithProductsSheet.getCell(5).setCellStyle(beginTableNameBoldStyle);

        rowInListOfProductsSheet.createCell(0).setCellValue("Наименование продуктов");
        rowInListOfProductsSheet.createCell(1).setCellValue("Вых. Брутто, гр");
        rowInListOfProductsSheet.createCell(2);
        rowInListOfProductsSheet.getCell(0).setCellStyle(beginTableNameBoldStyle);
        rowInListOfProductsSheet.getCell(1).setCellStyle(beginTableNameBoldStyle);
        rowInListOfProductsSheet.getCell(2).setCellStyle(beginTableNameBoldStyle);

        listOfProductsSheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 2));


    }

    private void autoSizeColumns() {
        newMenuSheet.autoSizeColumn(0);
        newMenuSheet.autoSizeColumn(1);
        newMenuSheet.autoSizeColumn(2);
        newMenuSheet.autoSizeColumn(3);
        newMenuSheet.autoSizeColumn(4);
        newMenuSheet.autoSizeColumn(5);

        dishesWithProductsSheet.setColumnWidth(0, 25 * 450);
//        dishesWithProductsSheet.setDefaultColumnWidth(20);
//        dishesWithProductsSheet.autoSizeColumn(1);
//        dishesWithProductsSheet.autoSizeColumn(2);
//        dishesWithProductsSheet.autoSizeColumn(3);
//        dishesWithProductsSheet.autoSizeColumn(4);
//        dishesWithProductsSheet.autoSizeColumn(5);

        listOfProductsSheet.autoSizeColumn(0);
//        listOfProductsSheet.autoSizeColumn(1);
//        listOfProductsSheet.autoSizeColumn(2);
//        listOfProductsSheet.autoSizeColumn(3);
//        listOfProductsSheet.autoSizeColumn(4);
//        listOfProductsSheet.autoSizeColumn(5);
    }

    private void createSpaceBetween(Row row) {
        row.createCell(0).setCellValue("");
    }

    private void createEnterInfo(Row row, String label, String labelText) {
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(labelText);
        row.getCell(0).setCellStyle(defaultBorderStyle);
        row.getCell(1).setCellStyle(defaultBorderStyle);
    }

    private void createEnterInfo(Row row, String label, Integer labelText) {
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(labelText);
        row.getCell(0).setCellStyle(defaultBorderStyle);
        row.getCell(1).setCellStyle(defaultBorderStyle);
    }

    public StartApp startApp;

    public void setStartApp(StartApp startApp) {
        this.startApp = startApp;
    }

    public void clearOblist() {
        oblist.clear();
        menuTable.setItems(oblist);
    }
}