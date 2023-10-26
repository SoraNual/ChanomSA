package ku.cs.controllers;

import java.io.File;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.Menu;
import ku.cs.services.DatabaseConnection;
import ku.cs.util.ProjectUtility;

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
    private Label notificationLabel;
    private int menuNow;
    // picture
    @FXML private ImageView menuImageView;
    private Image menuPicture;
    private File pictureFile;

    @FXML
    public void initialize(){
        //setup
        System.out.println("-------AddMenuPage------");

        ObservableList<String > menuTypes = FXCollections.observableArrayList("ชา","กาแฟ","โซดา","โกโก้","นม" );
        typeComboBox.setItems(menuTypes);
        notificationLabel.setText("");
    }

    @FXML
    private void handleAddMenuButton(ActionEvent event) throws SQLException {
        // อ่านข้อมูลเมนูจาก fxml
        String name;
        double price;
        String type;
        // check input name
        try {
            name = nameTextField.getText();
            if (Objects.equals(name, "")){
                notificationLabel.setText("กรอกชื่อเมนู");
                return;
            }
        }
        catch (Exception err){
            notificationLabel.setText("กรอกชื่อเมนู");
            return;

        }
        //type
        try {
             type = typeComboBox.getValue().toString();
        }
        catch (Exception err){
            notificationLabel.setText("กรอกประเภทเมนู");
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
        } catch (NumberFormatException e) {

            notificationLabel.setText("กรอกราคาเมนู");
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
                    notificationLabel.setText("ชื่อเมนู \""+name+"\" มีอยู่ในระบบแล้ว");
                    System.out.println("Repeat Name");
                    return;
                }

            }

            String countQuery = "SELECT COUNT(*) AS menu_count FROM menus";
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            ResultSet countResultSet = countStatement.executeQuery();

            if (countResultSet.next()) {
                menuNow = countResultSet.getInt("menu_count");
                menuNow++; // เพิ่มค่า menuNow อีก 1
            }


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

                addPictureToData();

                handleBackToMenuButton();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void addPictureToData(){
        Menu menu = new Menu(menuNow);
        if(pictureFile != null){
            menu.setPicture(pictureFile);
            System.out.println("add menu picture to data");
        }
        else {
            menu.setPicture(new File(menu.getPicturePath()));
        }
    }
    public void  handleAddPictureButton(ActionEvent actionEvent){
        try {
            pictureFile = ProjectUtility.pictureChooser();
            menuPicture = new Image(String.valueOf(pictureFile.toURI()));
            menuImageView.setImage(menuPicture);
            System.out.println("--AddPicture--");
        }
        catch (Exception err){
            System.out.println("--Not found picture--");
        }

    }
    public void handleDeletePictureButton(ActionEvent actionEvent){
        pictureFile = null;
        menuPicture = null;
        menuImageView.setImage(null);
        System.out.println("--DeletePicture--");
    }


    //back to menu button

    private void handleBackToMenuButton(){
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
