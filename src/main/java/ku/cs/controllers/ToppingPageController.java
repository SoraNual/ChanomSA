package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.Menu;
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

public class ToppingPageController {
    @FXML
    private TableView<Topping> toppingTable;

    @FXML
    private TableColumn<Topping, String> nameColumn;
    @FXML
    private TableColumn<Topping, Double> priceColumn;
    @FXML
    private TableColumn<Topping, String> detailButtonColumn;


    public void initialize() {
        System.out.println("-------ToppingPage------");
        // Set cell value factories
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("topping_name"));
        nameColumn.setCellFactory(column -> {
            return new TableCell<Topping, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อ cell ว่าง
                    } else {
                        setText(String.valueOf(item)); // แปลงข้อมูลเป็น String แล้วกำหนดให้ setText
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อมีข้อมูล
                    }
                }
            };
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("topping_price"));
        priceColumn.setCellFactory(column -> {
            return new TableCell<Topping, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อ cell ว่าง
                    } else {
                        setText(String.valueOf(item)); // แปลงข้อมูลเป็น String แล้วกำหนดให้ setText
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อมีข้อมูล
                    }
                }
            };
        });
        detailButtonColumn.setCellFactory(column -> {
            return new TableCell<Topping, String>() {
                final Button button = new Button("ดู");

                {
                    button.setOnAction(event -> {
                        // Get the selected menu item
                        Topping topping = getTableView().getItems().get(getIndex());

                        // Now you can navigate to the menuDetailPage with the selected menu
                        goToToppingDetailPage(topping);
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ปุ่มอยู่ตรงกลาง
                    }
                }
            };
        });


        // Retrieve menu items from the database and display in the table
        List<Topping> toppings = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT topping_id, topping_name, topping_price FROM toppings";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int toppingId = resultSet.getInt("topping_id");
                String toppingName = resultSet.getString("topping_name");
                double toppingPrice = resultSet.getDouble("topping_price");
                toppings.add(new Topping(toppingId, toppingName, toppingPrice));
                System.out.println(" "+toppingName+" "+toppingPrice);
            }

            toppingTable.setItems(FXCollections.observableList(toppings));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void goToToppingDetailPage(Topping topping) {
        try {
            // Pass the selected menu to the menuDetailPage
            Map<String, Object> data = new HashMap<>();
            data.put("topping", topping);

            // Navigate to the menuDetailPage with the data
            com.github.saacsos.FXRouter.goTo("topping-detail", data);
        } catch (Exception err) {
            System.out.println("Can't go to toppingDetailPage");
        }
    }


    //AddMenu button
    @FXML
    public void handleAddToppingButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("add-topping");
        } catch (Exception err){
            System.out.println("Can't go to add-topping");
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
