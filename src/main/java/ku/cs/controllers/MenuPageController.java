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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.Menu;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuPageController {
    @FXML
    private TableView<Menu> menuTable;
    @FXML
    private TableColumn<Menu, String> typeColumn;
    @FXML
    private TableColumn<Menu, String> nameColumn;
    @FXML
    private TableColumn<Menu, Double> priceColumn;
    @FXML
    private TableColumn<Menu, String> detailButtonColumn;
    @FXML
    private TableColumn<Menu, ImageView> pictureColumn;

    private String typeToShow ;


    public void initialize() {
        System.out.println("-------MenuPage------");
        // Set cell value factories
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("menu_type"));
        typeColumn.setCellFactory(column -> {
            return new TableCell<Menu , String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อ cell ว่าง
                    } else {
                        setText(item);
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อมีข้อมูล
                    }
                }
            };
        });
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        nameColumn.setCellFactory(column -> {
            return new TableCell<Menu , String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อ cell ว่าง
                    } else {
                        setText(item);
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อมีข้อมูล
                    }
                }
            };
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("menu_price"));
        priceColumn.setCellFactory(column -> {
            return new TableCell<Menu, Double>() {
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
        pictureColumn.setCellFactory(column -> new TableCell<Menu, ImageView>() {
            final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(50); // Adjust the desired width
                imageView.setFitHeight(50); // Adjust the desired height
            }

            @Override
            protected void updateItem(ImageView image, boolean empty) {
                super.updateItem(image, empty);
                if (empty || image == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(image.getImage());
                    setGraphic(imageView);
                }
            }
        });
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));


        detailButtonColumn.setCellFactory(column -> {
            return new TableCell<Menu, String>() {
                final Button button = new Button("ดู");

                {
                    button.setOnAction(event -> {
                        // Get the selected menu item
                        Menu menu = getTableView().getItems().get(getIndex());

                        // Now you can navigate to the menuDetailPage with the selected menu
                        goToMenuDetailPage(menu);
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
                String menuType = resultSet.getString("menu_type");
                String menuName = resultSet.getString("menu_name");
                double menuPrice = resultSet.getDouble("menu_price");
                menu.add(new Menu(menuId,menuType, menuName, menuPrice));

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

    private void goToMenuDetailPage(Menu menu) {
        try {
            // Pass the selected menu to the menuDetailPage
            Map<String, Object> data = new HashMap<>();
            data.put("menu", menu);

            // Navigate to the menuDetailPage with the data
            com.github.saacsos.FXRouter.goTo("menu-detail", data);
        } catch (Exception err) {
            System.out.println("Can't go to menuDetailPage");
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
