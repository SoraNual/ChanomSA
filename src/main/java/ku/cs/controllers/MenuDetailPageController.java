package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ku.cs.models.Menu;
import ku.cs.models.MenuType;
import ku.cs.services.DatabaseConnection;
import ku.cs.util.ProjectUtility;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuDetailPageController {
    // detailPane
    @FXML
    private Pane detailPane;
    @FXML
    private Label menuIDLabel;
    @FXML
    private Label menuNameLabel;
    @FXML
    private Label menuTypeLabel;
    @FXML
    private Label menuPriceLabel;
    // EditDetailPane
    @FXML
    private Pane editDetailPane;
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private ComboBox typeComboBox;
    @FXML
    private ImageView menuEditImageView;
    @FXML
    private Label notificationLabel;
    // picture
    @FXML private ImageView menuAllImageView;
    private Image menuPicture;
    private File pictureFile;
    Menu menu = new Menu();
    private String nameUse;
    private String typeUse;
    private double priceUse;
    private int menuNow;




    public void initialize() {
        System.out.println("-------MenuDetailPage------");

        // Retrieve data using com.github.saacsos.FXRouter.getData()
        Map<String, Object> data = (Map<String, Object>) com.github.saacsos.FXRouter.getData();

        // Check if data is not null
        if (data != null) {
            System.out.println(data);

            // You can now access the data and work with it
            menu = (Menu) data.get("menu");
            System.out.println("Menu Type: " + menu.getMenu_type());
            System.out.println("Menu Name: " + menu.getMenu_name());
            System.out.println("Menu Price: " + menu.getMenu_price());

        }
        nameUse = menu.getMenu_name();
        typeUse = menu.getMenu_type();
        priceUse = menu.getMenu_price();
        menuNow = menu.getMenu_id();

        menuPicture = menu.getPicture();
        menuAllImageView.setImage(menuPicture);



        detailPane.setVisible(true);
        editDetailPane.setVisible(false);

        ObservableList<String > menuTypes = FXCollections.observableArrayList("ชา","กาแฟ","โซดา","โกโก้","นม" );
        typeComboBox.setItems(menuTypes);

        notificationLabel.setText("");
        updateMenu();
    }
    private void updateMenu(){
        menuNameLabel.setText(nameUse);
        menuTypeLabel.setText(typeUse);
        menuPriceLabel.setText(String.valueOf(priceUse));

        menuPicture = menu.getPicture();
        menuAllImageView.setImage(menuPicture);
        menuEditImageView.setImage(menuPicture);


    }

    //Edit button
    @FXML
    public void handleEditButton(ActionEvent actionEvent){
        detailPane.setVisible(false);
        editDetailPane.setVisible(true);

        nameTextField.setText(nameUse);
        //typeComboBox
        typeComboBox.setValue(typeUse);
        priceTextField.setText(String.valueOf(priceUse));
        notificationLabel.setText("");

        menuPicture = menu.getPicture();
        menuEditImageView.setImage(menuPicture);
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
        String updatedType = (String) typeComboBox.getValue(); // Cast to String

        try {
            updatedPrice = Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            notificationLabel.setText("ระบุราคาไม่ถูกต้อง");
            return; // Exit the method if the price is not a valid number
        }

        // Check for duplicate menu names, excluding the current menu's menu_id
        if (CheckMenuName(updatedName, menu.getMenu_id())) {
            // Update the menu in the database
            boolean isUpdateSuccessful = updateMenuInDatabase(updatedName, updatedPrice, updatedType);

            if (isUpdateSuccessful) {
                notificationLabel.setText("Menu updated successfully.");
            } else {
                notificationLabel.setText("Failed to update menu.");
                return;
            }
        } else {
            notificationLabel.setText("ชื่อนี้มีอยู่แล้ว");
            return;
        }

        detailPane.setVisible(true);
        editDetailPane.setVisible(false);
    }

    private boolean CheckMenuName(String menuName, int currentMenuId) {
        // Check if the updated menu name is unique (not repeated) in the database, excluding the current menu_id
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT COUNT(*) as name_count FROM menus WHERE menu_name = ? AND menu_id != ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, menuName);
            statement.setInt(2, currentMenuId); // Exclude the current menu's menu_id
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

    private boolean updateMenuInDatabase(String name, double price, String type) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String updateSQL = "UPDATE menus SET menu_name = ?, menu_price = ?, menu_type = ? WHERE menu_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateSQL);

            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setString(3, type);
            statement.setInt(4, menu.getMenu_id()); // You should have the menu object or menu_id available

            int rowsUpdated = statement.executeUpdate();

            nameUse = name;
            typeUse = type;
            priceUse = price;
            updateMenu();
            addPictureToData();
            menuPicture = menu.getPicture();
            menuAllImageView.setImage(menuPicture);


            statement.close();
            connection.close();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void addPictureToData(){
        Menu menu = new Menu(menuNow);
        if(pictureFile != null){
            menu.setPicture(pictureFile);
            System.out.println("add menu picture to data");
        }
        else {
            menu.setPicture(new File(menu.getEditPicturePath()));
        }
    }
    public void  handleAddPictureButton(ActionEvent actionEvent){
        try {
            pictureFile = ProjectUtility.pictureChooser();
            menuPicture = new Image(String.valueOf(pictureFile.toURI()));
            menuAllImageView.setImage(menuPicture);
            menuEditImageView.setImage(menuPicture);
            System.out.println("--AddPicture--");
        }
        catch (Exception err){
            System.out.println("--Not found picture--");
        }

    }
    public void handleDeletePictureButton(ActionEvent actionEvent){
        pictureFile = null;
        menuPicture = null;
        menuEditImageView.setImage(null);
        System.out.println("--DeletePicture--");
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
