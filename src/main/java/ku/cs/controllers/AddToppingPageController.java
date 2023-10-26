package ku.cs.controllers;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.MenuType;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

public class AddToppingPageController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;
    @FXML
    private Label notificationLabel;



    @FXML
    public void initialize(){
        //setup
        notificationLabel.setText("");

    }

    @FXML
    private void handleAddToppingButton(ActionEvent event) throws SQLException {
        // อ่านข้อมูลเมนูจาก fxml
        String name ;
        double price;
        // check input name
        try {
            name = nameTextField.getText();
            if (Objects.equals(name, "")){
                notificationLabel.setText("กรอกชื่อท็อปปิ้ง");return;
            }
        }
        catch (Exception err){
            notificationLabel.setText("กรอกชื่อท็อปปิ้ง");
            return;
        }
        try {
            price = Double.parseDouble(priceTextField.getText());
            String priceString = priceTextField.getText();
            System.out.println("Price as a double: " + price);
            if (price < 0 ){

                notificationLabel.setText("ระบุราคาไม่ถูกต้อง");
                System.out.println("Input Error");
                return;
            }
            if (Objects.equals(priceString, "")){
                notificationLabel.setText("กรอกราคา");
                return;
            }
        } catch (NumberFormatException e) {
            notificationLabel.setText("กรอกราคาท็อปปิ้ง");
            return;
        }


        // เชื่อมต่อฐานข้อมูล
        try  {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT topping_id, topping_name, topping_price FROM toppings";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String topppingName = resultSet.getString("topping_name");
                if (Objects.equals(name, topppingName)){
                    notificationLabel.setText("ชื่อท็อปปิ้ง \""+name+"\" มีอยู่ในระบบแล้ว");
                    return;
                }

            }

            // เขียนคำสั่ง SQL สำหรับ INSERT ข้อมูล
            String insertSQL = "INSERT INTO toppings (topping_name, topping_price) VALUES ( ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, name);
                preparedStatement.setDouble(2, price);
                // ทำการ INSERT ข้อมูล
                preparedStatement.executeUpdate();
                //clear
                System.out.println(">> เพี่มท็อปปิ้ง "+name+" "+" ราคา "+price);
                nameTextField.setText("");
                priceTextField.setText("");
                gotoToppingPage();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void gotoToppingPage(){
        try {
            com.github.saacsos.FXRouter.goTo("topping");
        } catch (Exception err){
            System.out.println("Can't go back");
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
