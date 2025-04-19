package com.example.project3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class DateScreen {
    private HashTable hashTable;
    private AVLTree avlTree = new AVLTree();
    private MartyrsScreen martyrsScreen;
    private VBox root;
    private DatePicker datePicker;
    private ListView<String> dateListView;
    private ListView<String> AlldateListView;
    private Label dateInfoLabel;
    private Label totalMartyrsLabel;
    private Label maxDistrictLabel;
    private Label maxLocationLabel;
    private Label maxAgeLabel;
    private Label minAgeLabel;
    private Label navigate;
    private Label tableSize;
    private int currentUpIndex = -1;
    private int currentDownIndex = -1;
    private LocalDate selectedDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DateScreen(HashTable hashTable) {
        this.hashTable = hashTable;
        this.martyrsScreen = new MartyrsScreen(hashTable, avlTree);
        root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Date Management");
        titleLabel.setFont(new Font(24));
        root.getChildren().add(titleLabel);

        datePicker = new DatePicker();
        datePicker.setPromptText("Select a date");
        root.getChildren().add(datePicker);

        Button addDateButton = new Button("Add Date");
        addDateButton.setBackground(Background.fill(Color.LIGHTCYAN));
        addDateButton.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                if (!hashTable.containsDate(selectedDate)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Add Date");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to add this date?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        hashTable.insertDate(selectedDate, new AVLTree());
                        updateDateListView();
                        updateAllDateListView();
                        datePicker.setValue(null);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("This date already exists.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a date.");
                alert.showAndWait();
            }
        });
        root.getChildren().add(addDateButton);

        Button deleteDateButton = new Button("Delete Date");
        deleteDateButton.setBackground(Background.fill(Color.LIGHTCYAN));
        deleteDateButton.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null && hashTable.containsDate(selectedDate)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Date");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this date?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    hashTable.deleteDate(selectedDate);
                    updateDateListView();
                    updateAllDateListView();
                    datePicker.setValue(null);
                    tableSize.setText("The size: " + hashTable.getSize());
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a valid date to delete.");
                alert.showAndWait();
            }
        });
        root.getChildren().add(deleteDateButton);

        DatePicker updateDatePicker = new DatePicker();

        Button updateDateButton = new Button("Update Date");
        updateDateButton.setBackground(Background.fill(Color.LIGHTCYAN));
        updateDateButton.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            LocalDate newDate = updateDatePicker.getValue();

            if (selectedDate != null && newDate != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Date");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to update this date?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    hashTable.updateDate(selectedDate, newDate);
                    updateDateListView();
                    updateAllDateListView();
                    datePicker.setValue(null);
                    updateDatePicker.setValue(null);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select both old and new dates.");
                alert.showAndWait();
            }
        });
        root.getChildren().addAll(updateDatePicker, updateDateButton);
        tableSize = new Label();
        tableSize.setText("The size : " + hashTable.getSize());
        root.getChildren().add(tableSize);

        dateListView = new ListView<>();
        dateListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedDate = LocalDate.parse(newValue, DATE_FORMATTER);
                datePicker.setValue(selectedDate);
                updateDateInfo(selectedDate);
            }
        });
        updateDateListView();
        AlldateListView = new ListView<>();
        AlldateListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedDate = LocalDate.parse(newValue, DATE_FORMATTER);
                datePicker.setValue(selectedDate);
                updateDateInfo(selectedDate);
            }
        });
        updateAllDateListView();

        HBox view = new HBox(dateListView, AlldateListView);
        view.setSpacing(10);
        view.setAlignment(Pos.CENTER);

        HBox.setHgrow(dateListView, Priority.ALWAYS);
        HBox.setHgrow(AlldateListView, Priority.ALWAYS);

        root.getChildren().add(view);

        HBox navigating = new HBox();
        navigate = new Label();
        Button printHashTableUpButton = new Button("Date Up");
        printHashTableUpButton.setBackground(Background.fill(Color.GAINSBORO));

        printHashTableUpButton.setOnAction(event -> {
            navigateUp();
        });
        navigating.getChildren().add(printHashTableUpButton);

        Button printHashTableDownButton = new Button("Date Down");
        printHashTableDownButton.setBackground(Background.fill(Color.GAINSBORO));

        printHashTableDownButton.setOnAction(event -> {
            navigateDown();
        });
        navigating.getChildren().add(printHashTableDownButton);
        navigating.getChildren().add(navigate);
        navigating.setSpacing(10);
        root.getChildren().add(navigating);

        dateInfoLabel = new Label();
        totalMartyrsLabel = new Label();
        maxDistrictLabel = new Label();
        maxLocationLabel = new Label();
        maxAgeLabel = new Label();
        minAgeLabel = new Label();
        root.getChildren().addAll(dateInfoLabel, totalMartyrsLabel, maxDistrictLabel, maxLocationLabel, maxAgeLabel, minAgeLabel);
    }

    private void navigateUp() {
        if (selectedDate != null) {
            int startIndex = hashTable.getDateIndex(selectedDate);
            if (startIndex != -1) {
                if (currentUpIndex == -1 || currentUpIndex != startIndex) {
                    currentUpIndex = startIndex;
                }
                String result = hashTable.getNextHashTableEntryUpFrom(currentUpIndex);
                if (!result.equals("No more entries.")) {
                    currentUpIndex++;
                    navigate.setText(result);
                    String[] parts = result.split(", Date: ");
                    if (parts.length > 1) {
                        selectedDate = LocalDate.parse(parts[1], DATE_FORMATTER);
                        updateDateInfo(selectedDate);
                        datePicker.setValue(selectedDate);
                    }
                } else {
                    navigate.setText(result);
                    currentUpIndex = -1;
                }
            } else {
                navigate.setText("Selected date not found.");
            }
        } else {
            navigate.setText("Please select a date first.");
        }
    }

    private void navigateDown() {
        if (selectedDate != null) {
            int startIndex = hashTable.getDateIndex(selectedDate);
            if (startIndex != -1) {
                if (currentDownIndex == -1 || currentDownIndex != startIndex) {
                    currentDownIndex = startIndex;
                }
                String result = hashTable.getNextHashTableEntryDownFrom(currentDownIndex);
                if (!result.equals("No more entries.")) {
                    currentDownIndex--;
                    navigate.setText(result);
                    String[] parts = result.split(", Date: ");
                    if (parts.length > 1) {
                        selectedDate = LocalDate.parse(parts[1], DATE_FORMATTER);
                        updateDateInfo(selectedDate);
                        datePicker.setValue(selectedDate);
                    }
                } else {
                    navigate.setText(result);
                    currentDownIndex = -1;
                }
            } else {
                navigate.setText("Selected date not found.");
            }
        } else {
            navigate.setText("Please select a date first.");
        }
    }

    private void updateDateListView() {
        ObservableList<String> dateList = FXCollections.observableArrayList();
        for (LocalDate date : hashTable.getDates()) {
            dateList.add(date.format(DATE_FORMATTER));
        }
        dateListView.setItems(dateList);
    }
    private void updateAllDateListView() {
        ObservableList<String> dateList = FXCollections.observableArrayList();
        for (LocalDate date : hashTable.getAllDates()) {
            dateList.add(date.format(DATE_FORMATTER));
        }
        AlldateListView.setItems(dateList);
    }

    private void updateDateInfo(LocalDate selectedDate) {
        dateInfoLabel.setText("Selected Date: " + selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        if (hashTable.containsDate(selectedDate)) {
            AVLTree avlTree = hashTable.getAVLTree(selectedDate);
            int totalMartyrs = avlTree.getSize();
            totalMartyrsLabel.setText("Total Martyrs: " + totalMartyrs);

            List<Martyr> sortedMartyrs = avlTree.getSortedMartyrs();
            if (!sortedMartyrs.isEmpty()) {
                String maxDistrict = sortedMartyrs.get(0).getDistrict();
                String maxLocation = sortedMartyrs.get(0).getLocation();
                int maxAge = sortedMartyrs.get(0).getAge();
                int minAge = sortedMartyrs.get(0).getAge();
                for (Martyr martyr : sortedMartyrs) {
                    maxAge = Math.max(maxAge, martyr.getAge());
                    minAge = Math.min(minAge, martyr.getAge());
                }
                maxDistrictLabel.setText("District with most martyrs: " + maxDistrict);
                maxLocationLabel.setText("Location with most martyrs: " + maxLocation);
                if (maxAge == minAge){
                    maxAgeLabel.setText("Maximum and Minimum Age: " + maxAge);
                    minAgeLabel.setText("    ");
                }else{
                    maxAgeLabel.setText("Maximum Age: " + maxAge);
                    minAgeLabel.setText("Minimum Age: " + minAge);

                }
            }
            else{
                maxDistrictLabel.setText("District with most martyrs: N/A");
                maxLocationLabel.setText("Location with most martyrs: N/A");
                maxAgeLabel.setText("Maximum Age: N/A");
                minAgeLabel.setText("Minimum Age: N/A");
            }
        } else {
            totalMartyrsLabel.setText("Total Martyrs: N/A");
            maxDistrictLabel.setText("District with most martyrs: N/A");
            maxLocationLabel.setText("Location with most martyrs: N/A");
            maxAgeLabel.setText("Maximum Age: N/A");
            minAgeLabel.setText("Minimum Age: N/A");
        }
    }

    public VBox getRoot() {
        return root;
    }
}
