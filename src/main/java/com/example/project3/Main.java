package com.example.project3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main extends Application {
    HashTable hashTable;
    AVLTree avlTree;
    private Stage primaryStage;
    private DateScreen dateScreen;
    private MartyrsScreen martyrsScreen;
    File file;

    public Main() {
        hashTable = new HashTable();
        avlTree = new AVLTree();
        martyrsScreen = new MartyrsScreen(hashTable, avlTree);
        dateScreen = new DateScreen(hashTable);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Martyrs project");
        createLoadFileScreen();
    }

    public void createLoadFileScreen() {
        Label titleLabel = new Label("Martyrs project");
        titleLabel.setFont(new Font(24));

        Button loadButton = new Button("Load Martyrs File");
        loadButton.setBackground(Background.fill(Color.HONEYDEW));
        loadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Martyrs File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV files", "*.csv"));
            File selectedFile = null;
            this.file = selectedFile;
            selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                loadMartyrsFromFile(selectedFile);
                createMainScreen();
            }
        });

        VBox root = new VBox(20, titleLabel, loadButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 400, 200);
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#81c483")),
                new Stop(1, Color.web("#fcc200"))
        );
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadMartyrsFromFile(File file) {
        this.file = file;
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("M/d/yyyy"))
                .parseCaseInsensitive()
                .toFormatter(Locale.ENGLISH);

        try (Scanner scanner = new Scanner(file)) {
            scanner.nextLine(); // Skip the header line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                boolean hasEmptyValue = Arrays.stream(parts).anyMatch(String::isEmpty);

                if (hasEmptyValue) {
                    continue; // Skip this line if any value is empty
                }

                String name = parts[0];
                int age = Integer.parseInt(parts[2]);
                String location = parts[3];
                String district = parts[4];
                String gender = parts[5];

                LocalDate date = null;
                try {
                    date = LocalDate.parse(parts[1], DateTimeFormatter.ofPattern("M/d/yyyy"));
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format: " + parts[1]);
                }

                if (date == null) {
                    System.err.println("Invalid date format for line: " + line);
                    continue; // Skip this line if the date could not be parsed
                }

                Martyr martyr = new Martyr(name, age, district, location, gender);

                if (!hashTable.containsDate(date)) {
                    hashTable.insertDate(date, new AVLTree());
                }

                addMartyrToHashTable(martyr, date);

                avlTree.insertDistrict(district);
                avlTree.insertLocation(location);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading martyrs file: " + e.getMessage());
        }
    }

    private void addMartyrToHashTable(Martyr martyr, LocalDate date) {
        if (!hashTable.containsDate(date)) {
            hashTable.insertDate(date, new AVLTree());
        } else {
            AVLTree avlTree = hashTable.getAVLTree(date);
            if (avlTree == null) {
                avlTree = new AVLTree();
                hashTable.insertDate(date, avlTree);
            }
        }
        AVLTree avlTree = hashTable.getAVLTree(date);
        if (avlTree != null) {
            avlTree.insert(martyr);
        } else {
            System.err.println("Error: AVLTree is null for date " + date);
        }
    }

    private void createMainScreen() {
        dateScreen = new DateScreen(hashTable);
        martyrsScreen = new MartyrsScreen(hashTable, avlTree);
        martyrsScreen.setCurrentAVLTree(avlTree, LocalDate.now());

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Martyrs File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV files", "*.csv"));
            File selectedFile = fileChooser.showSaveDialog(primaryStage);

            if (selectedFile != null) {
               saveMartyrsToFile(selectedFile);
            }
        });
        fileMenu.getItems().add(saveMenuItem);
        menuBar.getMenus().add(fileMenu);

        TabPane tabPane = new TabPane();
        Tab dateTab = new Tab("Dates");
        dateTab.setContent(dateScreen.getRoot());
        Tab martyrsTab = new Tab("Martyrs");
        martyrsTab.setContent(martyrsScreen.getRoot());
        Tab menuTab = new Tab("Menu");
        menuTab.setContent(menuBar);
        tabPane.getTabs().addAll(dateTab, martyrsTab, menuTab);

        VBox root = new VBox(10, tabPane);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 1000, 1000);
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#ADD8E6")),
                new Stop(1, Color.web("#C6E2B5"))
        );
        root.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveMartyrsToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Name,Date,Age,Location,District,Gender");
            for (LocalDate date : hashTable.getDates()) {
                AVLTree avlTree = hashTable.getAVLTree(date);
                if (avlTree == null) {
                    System.err.println("No AVLTree found for date: " + date);
                    continue; // Skip this date if avlTree is null
                }
                for (Martyr martyr : avlTree.martyrs) {
                    writer.println(martyr.getName() + "," +
                            date.format(DateTimeFormatter.ofPattern("M/d/yyyy")) + "," +
                            martyr.getAge() + "," +
                            martyr.getLocation() + "," +
                            martyr.getDistrict() + "," +
                            martyr.getGender());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error saving martyrs file: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
