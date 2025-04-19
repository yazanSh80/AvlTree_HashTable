package com.example.project3;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MartyrsScreen {
    private HashTable hashTable;
    private AVLTree avlTree;
    private VBox root;
    private ListView<Martyr> martyrListView;
    private DatePicker datePicker;
    private LocalDate selectedDate;
    private Label heightLabel;
    private Label sizeLabel;
    private TextField nameField;
    private TextField ageField;
    private ComboBox<String> districtComboBox;
    private ComboBox<String> locationComboBox;
    private ToggleGroup genderGroup;
    private RadioButton maleRadio;
    private RadioButton femaleRadio;
    private LocalDate currentSelectedDate;
    private AVLTree currentAVLTree;
    List<Martyr> martyrs ;

    public MartyrsScreen(HashTable hashTable, AVLTree avlTree) {
        this.hashTable = hashTable;
        this.avlTree = avlTree;

        root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Martyrs Management");
        titleLabel.setFont(new Font(24));
        root.getChildren().add(titleLabel);

        datePicker = new DatePicker();
        datePicker.setPromptText("Select a date");
        root.getChildren().add(datePicker);

        martyrListView = new ListView<>();
        Button activate = new Button("Show");
        activate.setBackground(Background.fill(Color.PALETURQUOISE));

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedDate = newValue;

            if (newValue != null) {
                this.avlTree = hashTable.getAVLTree(selectedDate);
                if (avlTree != null) {
                    this.martyrs = avlTree.getAllMartyrs();
                    martyrListView.getItems().setAll(martyrs);
                    activate.setOnAction(e-> {
                        if (hashTable.containsDate(selectedDate)){
                            updateMartyrListView();
                            updateTreeInfo();
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Please select a valid date.");
                            alert.showAndWait();
                        }

                    });
                } else {
                    martyrListView.getItems().clear();
                }
            } else {
                martyrListView.getItems().clear();
            }
        });

        root.getChildren().add(activate);

        root.getChildren().add(martyrListView);
        heightLabel = new Label();
        sizeLabel = new Label();
        updateTreeInfo();
        root.getChildren().addAll(heightLabel, sizeLabel);
        HBox fieldsHBox = new HBox(10);
        fieldsHBox.setAlignment(Pos.CENTER);

        nameField = new TextField();
        nameField.setPromptText("Name");
        ageField = new TextField();
        ageField.setPromptText("Age");
        districtComboBox = new ComboBox<>();
        List<String> districts = avlTree.getDistricts();
        if (districtComboBox != null) {
            districtComboBox.getItems().addAll(districts);
        }
        districtComboBox.setPromptText("District");

        locationComboBox = new ComboBox<>();
        List<String> locations = avlTree.getLocation();
        if (locationComboBox != null) {
            locationComboBox.getItems().addAll(locations);
        }
        locationComboBox.setPromptText("Location");

        genderGroup = new ToggleGroup();
        maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(genderGroup);

        fieldsHBox.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Age:"), ageField,
                new Label("District:"), districtComboBox,
                new Label("Location:"), locationComboBox,
                new Label("Gender:"), maleRadio, femaleRadio
        );

        root.getChildren().add(fieldsHBox);
        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER);

        Button addMartyrButton = new Button("Add Martyr");
        addMartyrButton.setBackground(Background.fill(Color.LIGHTCYAN));

        addMartyrButton.setOnAction(event -> {
            if (validateInputs()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Add Martyr");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to add this martyr?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Martyr newMartyr = createMartyrFromFields();
                    insert(newMartyr); // Call the insert method
                    clearFields();
                }
            }
        });

        Button updateMartyrButton = new Button("Update Martyr");
        updateMartyrButton.setBackground(Background.fill(Color.LIGHTCYAN));
//        updateMartyrButton.setOnAction(event -> {
//            if (validateInputs() && martyrListView.getSelectionModel().getSelectedItem() != null) {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setTitle("Update Martyr");
//                alert.setHeaderText(null);
//                alert.setContentText("Are you sure you want to update this martyr?");
//                Optional<ButtonType> result = alert.showAndWait();
//                if (result.isPresent() && result.get() == ButtonType.OK) {
//                    Martyr updatedMartyr = createMartyrFromFields();
//                    Martyr selectedMartyr = martyrListView.getSelectionModel().getSelectedItem();
//                    update(selectedMartyr, updatedMartyr); // Call the update method
//                    clearFields();
//                }
//            } else {
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("Error");
//                alert.setHeaderText(null);
//                alert.setContentText("Please select a martyr to update.");
//                alert.showAndWait();
//            }
//        });

        Button deleteMartyrButton = new Button("Delete Martyr");
        deleteMartyrButton.setBackground(Background.fill(Color.LIGHTCYAN));
        deleteMartyrButton.setOnAction(event -> {
            if (martyrListView.getSelectionModel().getSelectedItem() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Martyr");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this martyr?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Martyr selectedMartyr = martyrListView.getSelectionModel().getSelectedItem();
                    delete(selectedMartyr); // Call the delete method
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a martyr to delete.");
                alert.showAndWait();
            }
        });
        buttonsHBox.getChildren().addAll(addMartyrButton, updateMartyrButton, deleteMartyrButton);
        root.getChildren().add(buttonsHBox);

        HBox printButtonsHBox = new HBox(10);
        printButtonsHBox.setAlignment(Pos.CENTER);

        Button printLevelByLevelButton = new Button("Print Level by Level");
        printLevelByLevelButton.setBackground(Background.fill(Color.AZURE));

        printLevelByLevelButton.setOnAction(event -> printLevelByLevel());

        Button printTableSortedByAgeButton = new Button("Print Table Sorted by Age");
        printTableSortedByAgeButton.setBackground(Background.fill(Color.AZURE));

        printTableSortedByAgeButton.setOnAction(event -> printSortedByAge());

        printButtonsHBox.getChildren().addAll(printLevelByLevelButton, printTableSortedByAgeButton);
        root.getChildren().add(printButtonsHBox);
    }
    private void clearFields() {
        nameField.clear();
        ageField.clear();
        districtComboBox.getSelectionModel().clearSelection();
        districtComboBox.setValue("District");
        locationComboBox.getSelectionModel().clearSelection();
        locationComboBox.setValue("Location");
        genderGroup.selectToggle(null);
    }

    private boolean validateInputs() {
        if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || districtComboBox.getSelectionModel().isEmpty() || locationComboBox.getSelectionModel().isEmpty() || genderGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the required fields.");
            alert.showAndWait();
            return false;
        }
        try {
            int age = Integer.parseInt(ageField.getText());
            if (age <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Age must be a positive number.");
                alert.showAndWait();
                return false;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Age must be a valid number.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private Martyr createMartyrFromFields() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String district = districtComboBox.getValue();
        String location = locationComboBox.getValue();
        String gender;
        if (((RadioButton) genderGroup.getSelectedToggle()).getText().equals("Male")){
            gender = "M" ;
        }else gender = "F";

        return new Martyr(name, age, district, location, gender);
    }
    private void updateTreeInfo() {
        heightLabel.setText("AVL Tree Height: " + avlTree.calculateHeight());
        sizeLabel.setText("AVL Tree Size: " + avlTree.getSize());
    }
    public void setCurrentAVLTree(AVLTree currentAVLTree, LocalDate currentSelectedDate) {
        this.currentAVLTree = currentAVLTree;
        this.currentSelectedDate = currentSelectedDate;
        this.selectedDate = currentSelectedDate;
        this.avlTree = currentAVLTree;
        updateMartyrListView();
        updateTreeInfo();
    }
    private void printLevelByLevel() {
        avlTree = new AVLTree();
        ObservableList<Martyr> martyrs = martyrListView.getItems();
        for (Martyr martyr : martyrs) {
            avlTree.insert(martyr);
        }
        martyrListView.getItems().clear();
        List<Martyr> levelByLevelMartyrs = avlTree.printLevelByLevel();
        if (!levelByLevelMartyrs.isEmpty()) martyrListView.getItems().setAll(levelByLevelMartyrs);
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The list is empty.");
            alert.showAndWait();
        }
    }
    private void printSortedByAge(){
        avlTree = new AVLTree();
        ObservableList <Martyr> martyrs = martyrListView.getItems();
        for (Martyr martyr : martyrs){
            avlTree.insert(martyr);
        }
        martyrListView.getItems().clear();
        List<Martyr> ageSorted = avlTree.getAllMartyrsSortedByAge();
        if (!ageSorted.isEmpty()) martyrListView.getItems().setAll(ageSorted);
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The list is empty.");
            alert.showAndWait();
        }

    }
    public void insert(Martyr martyr) {
        avlTree.insert(martyr);
        martyrs.add(martyr);
        martyrListView.getItems().addAll(martyrs);
        updateTreeInfo();
    }

//    public void update(Martyr oldMartyr, Martyr newMartyr) {
//        avlTree.update(oldMartyr, newMartyr);
//        int index = martyrs.indexOf(oldMartyr);
//        if (index != -1) {
//            martyrs.set(index, newMartyr); // Update the martyr in the list
//        }
//        updateMartyrListView();
//        updateTreeInfo();
//    }
    public void delete(Martyr martyr) {
        avlTree.delete(martyr);
        martyrs.remove(martyr);
        updateMartyrListView();
        updateTreeInfo();
    }
    private void updateMartyrListView() {
        if (selectedDate != null) {
            AVLTree tree = hashTable.getAVLTree(selectedDate);
            if (tree != null) {
                List<Martyr> martyrs = tree.getAllMartyrs();
                martyrListView.getItems().clear();
                martyrListView.getItems().addAll(martyrs);
            }
        }
    }

    public VBox getRoot() {
        return root;
    }
}
