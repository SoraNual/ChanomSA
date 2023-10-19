package ku.cs.controllers;

import java.awt.geom.QuadCurve2D;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.Menu;
import ku.cs.models.MenuType;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

public class AddMenuPageController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private ComboBox typeComboBox;
    @FXML
    private Label notification;

    @FXML
    public void initialize(){
        //setup
        System.out.println("-------AddMenuPage------");

        ObservableList<String > menuTypes = FXCollections.observableArrayList("ชา","กาแฟ","โซดา","โกโก้","นม" );
        typeComboBox.setItems(menuTypes);
        notification.setVisible(false);
    }

    @FXML
    private void handleAddMenuButton(ActionEvent event) throws SQLException {
        // อ่านข้อมูลเมนูจาก fxml
        String name = nameTextField.getText();
        double price = Double.parseDouble(priceTextField.getText());
        String type = typeComboBox.getValue().toString();
        // check input
        if (Objects.equals(name, "")){
            notification.setVisible(true);
            notification.setText("ชื่อเมนูไม่ถูกต้อง");
            System.out.println("Input Error");
            return;
        }
        try {
            double priceValue = Double.parseDouble(priceTextField.getText());
            System.out.println("Price as a double: " + priceValue);
        } catch (NumberFormatException e) {
            notification.setVisible(true);
            notification.setText("ราคาไม่ถูกต้อง");
            System.err.println("Invalid price format: " + priceTextField.getText());
            return;
        }

        // เชื่อมต่อฐานข้อมูล
        try  {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT menu_id, menu_type, menu_name, menu_price FROM menus";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String menuType = resultSet.getString("menu_type");
                String menuName = resultSet.getString("menu_name");
                if (Objects.equals(name, menuName)){
                    notification.setVisible(true);
                    notification.setText("ชื่อเมนู \""+name+"\" มีอยู่ในระบบแล้ว");
                    System.out.println("Repeat Name");
                    return;
                }

            }


            resultSet.close();
            statement.close();
            connection.close();


            // เขียนคำสั่ง SQL สำหรับ INSERT ข้อมูล
            String insertSQL = "INSERT INTO menus (menu_type, menu_name, menu_price) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, type);
                preparedStatement.setString(2, name);
                preparedStatement.setDouble(3, price);
                // ทำการ INSERT ข้อมูล
                preparedStatement.executeUpdate();
                //clear
                System.out.println(">> เพี่มเมนู "+name+" ประเภท "+type+" ราคา "+price);
                nameTextField.setText("");
                priceTextField.setText("");
                typeComboBox.cancelEdit();
                gotoMenuPage();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void gotoMenuPage(){
        try {
            com.github.saacsos.FXRouter.goTo("menu");
        } catch (Exception err){
            System.out.println("Can't go back");
        }
    }
    //back button
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("menu");
        } catch (Exception err){
            System.out.println("Can't go back");
        }
    }
}
