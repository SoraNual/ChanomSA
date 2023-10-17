package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Menu;
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

    private String typeToShow ;
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        System.out.println("-------CreateOrderPage------");
        typeToShow = "All";
        updateDatabase();





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
