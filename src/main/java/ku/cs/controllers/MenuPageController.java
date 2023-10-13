package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.Menu;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuPageController {

    @FXML
    private AnchorPane scenePane;
    @FXML
    private TableView<Menu> menuTable;
    @FXML
    private TableColumn<Menu, String> typeColumn;
    @FXML
    private TableColumn<Menu, String> nameColumn;
    @FXML
    private TableColumn<Menu, Double> priceColumn;


    public void initialize() {
        System.out.println("-------MenuPage------");
        // Initialize the table columns
        // Set the cell factory for each TableColumn object
        typeColumn.setCellFactory(column -> new TableCell<Menu, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item);
                }
            }
        });

        nameColumn.setCellFactory(column -> new TableCell<Menu, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item);
                }
            }
        });

        priceColumn.setCellFactory(column -> new TableCell<Menu, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.toString());
                }
            }
        });



        // Retrieve menu items from the database and display in the table
        List<Menu> menu = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT menu_id, menu_type, menu_name, menu_price FROM menus";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int menuId = resultSet.getInt("menu_id");
                String menuType = resultSet.getString("menu_type");
                String menuName = resultSet.getString("menu_name");
                double menuPrice = resultSet.getDouble("menu_price");
                menu.add(new Menu(menuId,menuType, menuName, menuPrice));
                System.out.println(menuType+" "+menuName+" "+menuPrice);
            }

            menuTable.setItems(FXCollections.observableList(menu));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    //AddMenu button
    @FXML
    public void handleAddMenuButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("add-menu");
        } catch (Exception err){
            System.out.println("Can't go to add-menu");
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
