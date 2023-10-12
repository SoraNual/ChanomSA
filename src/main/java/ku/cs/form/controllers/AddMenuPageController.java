package ku.cs.form.controllers;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ku.cs.models.MenuType;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddMenuPageController {
    private DatabaseConnection databaseConnection;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private ComboBox typeComboBox;

    @FXML
    public void initialize(){
        //setup
        ObservableList<MenuType> menuTypes = FXCollections.observableArrayList(MenuType.ชา, MenuType.กาแฟ, MenuType.โซดา);
        typeComboBox.setItems(menuTypes);
    }

    @FXML
    private void handleAddMenuButton(ActionEvent event) throws SQLException {
        // อ่านข้อมูลเมนูจาก fxml
        String name = nameTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        String type = typeComboBox.getValue().toString();

        // เชื่อมต่อฐานข้อมูล
        try (Connection connection = DatabaseConnection.getConnection()) {
            // เขียนคำสั่ง SQL สำหรับ INSERT ข้อมูล
            String insertSQL = "INSERT INTO menus (menu_type, menu_name, menu_price) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, type);
                preparedStatement.setString(2, name);
                preparedStatement.setDouble(3, price);
                // ทำการ INSERT ข้อมูล
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //back button
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go back");
        }
    }
}
