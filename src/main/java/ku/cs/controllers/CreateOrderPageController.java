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
import java.util.List;

public class CreateOrderPageController {
    // menu tableView
    @FXML
    private TableView<Menu> menuTable;
    @FXML
    private TableColumn<Menu, String> nameColumn;
    // addMenuPane
    @FXML
    private Pane addMenuPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private ComboBox toppingComboBox;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label totalLabel;


    private String typeToShow ;
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        System.out.println("-------CreateOrderPage------");
        typeToShow = "All";
        updateDatabase();
        setupTopping();
        //addMenuPane.setVisible(false);
        quantityLabel.setText("1");








    }

    private void updateDatabase(){
        // Retrieve menu items from the database and display in the table
        List<Menu> menu = new ArrayList<>();

        String query;
        if ("All".equals(typeToShow)) {
            query = "SELECT menu_id, menu_type, menu_name, menu_price FROM menus";
        } else {
            query = "SELECT menu_id, menu_type, menu_name, menu_price FROM menus WHERE menu_type = ?";
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            if (!"All".equals(typeToShow)) {
                statement.setString(1, typeToShow);
            }

            ResultSet resultSet = statement.executeQuery();

            System.out.println("--Update database to "+typeToShow+" --");
            while (resultSet.next()) {
                int menuId = resultSet.getInt("menu_id");
                String menuName = resultSet.getString("menu_name");
                double menuPrice = resultSet.getDouble("menu_price");
                menu.add(new Menu(menuId,typeToShow, menuName, menuPrice));

                System.out.println(typeToShow+" "+menuName+" "+menuPrice);
            }

            menuTable.setItems(FXCollections.observableList(menu));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setupTopping(){
        List<String> toppingList = new ArrayList<>();

        String query = "SELECT topping_id, topping_name, topping_price FROM toppings";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("--Update Topping to set--");
            while (resultSet.next()) {
                int toppingId = resultSet.getInt("topping_id");
                String toppingName = resultSet.getString("topping_name");
                double toppingPrice = resultSet.getDouble("topping_price");
                toppingList.add(toppingName);

                System.out.println(" "+toppingName+" ");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<String> allToppingList = FXCollections.observableArrayList(toppingList);
        toppingComboBox.setItems(allToppingList);
    }
    @FXML
    public void handleOpenPaneButton(ActionEvent actionEvent){


    }
    @FXML
    public void handlePlusQuantityButton(ActionEvent actionEvent) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());

        if (currentQuantity < 5) {
            currentQuantity++;
        }


        quantityLabel.setText(Integer.toString(currentQuantity));
        updatePriceWithTopping(currentQuantity);
    }
    @FXML
    public void handleMinusQuantityButton(ActionEvent actionEvent) {
        // Get the current value of the quantityLabel
        int currentQuantity = Integer.parseInt(quantityLabel.getText());

        // Ensure the quantity doesn't go below zero
        if (currentQuantity > 1) {
            currentQuantity--;
        }

        // Update the text of the quantityLabel with the new value
        quantityLabel.setText(Integer.toString(currentQuantity));
        updatePriceWithTopping(currentQuantity);
    }
    private void updatePriceWithTopping(int currentQuantity ){
        int price;
        price = currentQuantity;
        totalLabel.setText("รวม :"+price+" บาท");
    }
    @FXML
    public void handleAllButton(ActionEvent actionEvent){
        typeToShow = "All";
        updateDatabase();
    }
    @FXML
    public void handleTeaButton(ActionEvent actionEvent){
        typeToShow = "ชา";
        updateDatabase();
    }
    @FXML
    public void handleCoffeeButton(ActionEvent actionEvent){
        typeToShow = "กาแฟ";
        updateDatabase();
    }
    @FXML
    public void handleSodaButton(ActionEvent actionEvent){
        typeToShow = "โซดา";
        updateDatabase();
    }
    @FXML
    public void handleCocaoButton(ActionEvent actionEvent){
        typeToShow = "โกโก้";
        updateDatabase();
    }
    @FXML
    public void handleMilkButton(ActionEvent actionEvent){
        typeToShow = "นม";
        updateDatabase();
    }



    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go back");
        }
    }
}
