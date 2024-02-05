package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class InventoryTable extends Application {

    // Declare tableData as a field
    private ObservableList<Inventory> tableData;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Load data when the application starts
        tableData = loadData();

        showTable(stage);
    }

    @Override
    public void stop() {
        // Save data when the application exits
        saveData();
    }

    private void showTable(Stage stage) {
        Scene scene = new Scene(new BorderPane());
        final Label label = new Label("Inventory of Climbing Holds");

        // Data the TableView displays
        ObservableList<Inventory> tableData = FXCollections.observableArrayList();

        // Create the table
        TableView<Inventory> table = new TableView<>();
        table.setItems(tableData);

        // columns for table
        TableColumn<Inventory, String> holdType = new TableColumn<>("Hold type");
        holdType.setMinWidth(100);
        holdType.setCellValueFactory(new PropertyValueFactory<>("holdType"));

        TableColumn<Inventory, String> manufacture = new TableColumn<>("Manufacture");
        manufacture.setMinWidth(100);
        manufacture.setCellValueFactory(new PropertyValueFactory<>("manufacture"));

        TableColumn<Inventory, String> mountingOption = new TableColumn<>("Mounting Option");
        mountingOption.setMinWidth(200);
        mountingOption.setCellValueFactory(new PropertyValueFactory<>("mountingOption"));

        TableColumn<Inventory, String> texture = new TableColumn<>("Texture");
        texture.setMinWidth(200);
        texture.setCellValueFactory(new PropertyValueFactory<>("texture"));

        TableColumn<Inventory, String> color = new TableColumn<>("Color");
        color.setMinWidth(200);
        color.setCellValueFactory(new PropertyValueFactory<>("color"));

        TableColumn<Inventory, String> size = new TableColumn<>("Size");
        size.setMinWidth(200);
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        table.getColumns().addAll(holdType, manufacture, mountingOption, texture, color, size);

        // TextField for inventory input
        TextField addHoldType = new TextField();
        addHoldType.setPromptText("Hold Type");
        addHoldType.setMaxWidth(holdType.getPrefWidth());

        TextField addManufacture = new TextField();
        addManufacture.setMaxWidth(manufacture.getPrefWidth());
        addManufacture.setPromptText("Manufacture");

        TextField addMountingOption = new TextField();
        addMountingOption.setMaxWidth(mountingOption.getPrefWidth());
        addMountingOption.setPromptText("Mounting Option");

        TextField addTexture = new TextField();
        addTexture.setMaxWidth(texture.getPrefWidth());
        addTexture.setPromptText("Texture");

        TextField addColor = new TextField();
        addColor.setMaxWidth(color.getPrefWidth());
        addColor.setPromptText("Color");

        TextField addSize = new TextField();
        addSize.setMaxWidth(texture.getPrefWidth());
        addSize.setPromptText("Size");

        // Button to add a new hold
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {

            Inventory newInventory = new Inventory(addHoldType.getText(), addManufacture.getText(),
                    addMountingOption.getText(), addTexture.getText(), addColor.getText(), addSize.getText());

            // Add the new element to the table data
            // clears the text field for new entry
            tableData.add(newInventory);
            addHoldType.clear();
            addManufacture.clear();
            addMountingOption.clear();
            addTexture.clear();
            addColor.clear();
            addSize.clear();
        });

        // Button to remove a hold
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            Inventory selectedItem = table.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                // Remove the item from the list of the displayed holds
                tableData.remove(selectedItem);
            }
        });

        // Button to edit a hold
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            Inventory selectedItem = table.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                // Set the values of selected item to the text fields for editing
                addHoldType.setText(selectedItem.getHoldType());
                addManufacture.setText(selectedItem.getManufacture());
                addMountingOption.setText(selectedItem.getMountingOption());
                addTexture.setText(selectedItem.getTexture());
                addColor.setText(selectedItem.getColor());
                addSize.setText(selectedItem.getSize());

                // Remove the item from the list of displayed holds
                tableData.remove(selectedItem);
            }
        });


        HBox textFieldInput = new HBox();
        textFieldInput.getChildren().addAll(addHoldType, addManufacture, addMountingOption, addTexture,
                addColor, addSize, addButton, removeButton, editButton);
        textFieldInput.setSpacing(3);

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, textFieldInput);

        BorderPane root = (BorderPane) scene.getRoot();
        root.setCenter(vbox);

        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(10, 0, 0, 10));


        // Save button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #3f6ab6; -fx-text-fill: white");
        saveButton.setOnAction(e -> saveData());

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #3f6ab6; -fx-text-fill: white");
        // cancelButton.setOnAction(e -> stage.close());
        cancelButton.setOnAction((e) -> {
            // Hide the current stage (Overview)
            stage.hide();

            // Create an instance of the Main class
            Main main = new Main();

            // Start the Main view (show the main stage)
            main.start(new Stage());
        });

        hbox.getChildren().addAll(saveButton, cancelButton);

        // create scene, stage, set title, and show
        root.setBottom(hbox);
        stage.setScene(scene);
        stage.show();
    }

    // Load data from the file
    private ObservableList<Inventory> loadData() {
        ObservableList<Inventory> data = FXCollections.observableArrayList();
        String filePath = "holdinventory.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Assuming your Inventory class has a constructor that accepts the data in the order specified
                Inventory item = new Inventory(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                data.add(item);
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    // Save data to the file
    private void saveData() {
        String filePath = "holdinventory.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Inventory item : tableData) {
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        item.getHoldType(), item.getManufacture(), item.getMountingOption(),
                        item.getTexture(), item.getColor(), item.getSize());
                writer.write(line);
                writer.newLine();
            }

            System.out.println("Data saved successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}






