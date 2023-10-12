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

        // เชื่อมต่อกับฐานข้อมูล testbubbletea
        Connection connection = DatabaseConnection.getConnection();

        // ดำเนินการเพิ่มเมนูลงในฐานข้อมูล testbubbletea
        String sql = "INSERT INTO menus (type,name, price) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, name);
        preparedStatement.setDouble(3, price);
        preparedStatement.executeUpdate();

        // ปิดการเชื่อมต่อ
        connection.close();
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
