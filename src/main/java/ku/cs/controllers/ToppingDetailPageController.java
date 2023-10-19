package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import ku.cs.models.Menu;
import ku.cs.models.MenuType;
import ku.cs.models.Topping;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToppingDetailPageController {
    // detailPane
    @FXML
    private Pane detailPane;
    @FXML
    private Label toppingIDLabel;
    @FXML
    private Label toppingNameLabel;
    @FXML
    private Label toppingTypeLabel;
    @FXML
    private Label toppingPriceLabel;
    // EditDetailPane
    @FXML
    private Pane editDetailPane;
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private Label notificationLabel;
    Topping topping = new Topping();
    private String nameUse;
    private double priceUse;




    public void initialize() {
        System.out.println("-------ToppingDetailPage------");

        // Retrieve data using com.github.saacsos.FXRouter.getData()
        Map<String, Object> data = (Map<String, Object>) com.github.saacsos.FXRouter.getData();

        // Check if data is not null
        if (data != null) {
            System.out.println(data);

            // You can now access the data and work with it
            topping = (Topping) data.get("topping");
            System.out.println("Menu ID: " + topping.getTopping_id());
            System.out.println("Menu Name: " + topping.getTopping_name());
            System.out.println("Menu Price: " + topping.getTopping_price());
        }
        nameUse = topping.getTopping_name();
        priceUse = topping.getTopping_price();



        detailPane.setVisible(true);
        editDetailPane.setVisible(false);


        notificationLabel.setText("");
        updateTopping();
    }
    private void updateTopping(){
        toppingNameLabel.setText(nameUse);
        toppingPriceLabel.setText(String.valueOf(priceUse));


    }

    //Edit button
    @FXML
    public void handleEditButton(ActionEvent actionEvent){
        detailPane.setVisible(false);
        editDetailPane.setVisible(true);

        nameTextField.setText(nameUse);
        priceTextField.setText(String.valueOf(priceUse));

        notificationLabel.setText("");
    }
    //Cancel button
    @FXML
    public void handleCancelButton(ActionEvent actionEvent){
        detailPane.setVisible(true);
        editDetailPane.setVisible(false);

    }
    //Confirm Button
    @FXML
    public void handleConfirmButton(ActionEvent actionEvent) {
        // Get the updated values from the text fields and combo box
        String updatedName = nameTextField.getText();
        double updatedPrice;
        try {
            updatedPrice = Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            notificationLabel.setText("ระบุราคาไม่ถูกต้อง");
            return; // Exit the method if the price is not a valid number
        }

        // Check for duplicate menu names, excluding the current topping's topping_id
        if (CheckToppingName(updatedName, topping.getTopping_id())) {
            // Update the menu in the database
            boolean isUpdateSuccessful = updateMenuInDatabase(updatedName, updatedPrice);

            if (isUpdateSuccessful) {
                notificationLabel.setText("Topping updated successfully.");
            } else {
                notificationLabel.setText("Failed to update topping.");
                return;
            }
        } else {
            notificationLabel.setText("ชื่อนี้มีอยู่แล้ว");
            return;
        }

        detailPane.setVisible(true);
        editDetailPane.setVisible(false);
    }

    private boolean CheckToppingName(String toppingName, int currentToppingId) {
        // Check if the updated Topping name is unique (not repeated) in the database, excluding the current menu_id
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT COUNT(*) as topping_count FROM toppings WHERE topping_name = ? AND topping_id != ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, toppingName);
            statement.setInt(2, currentToppingId); // Exclude the current menu's menu_id
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int nameCount = resultSet.getInt("name_count");
                return nameCount == 0; // If count is 0, the name is unique
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true; // In case of an error, treat it as a non-unique name
    }

    private boolean updateMenuInDatabase(String name, double price) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String updateSQL = "UPDATE toppings SET topping_name = ?, topping_price = ? WHERE menu_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateSQL);

            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, topping.getTopping_id()); // You should have the menu object or topping_id available

            int rowsUpdated = statement.executeUpdate();

            nameUse = name;
            priceUse = price;
            updateTopping();

            statement.close();
            connection.close();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //back button
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("topping");
        } catch (Exception err){
            System.out.println("Can't go back");
        }
    }
}
