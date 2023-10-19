package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import ku.cs.models.Menu;
import ku.cs.models.OrderAllDetail;
import ku.cs.models.OrderDetail;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderPageController {
    // chooseMenuPane
    @FXML
    private Pane chooseMenuPane;
    // menu tableView
    @FXML
    private TableView<Menu> menuTable;
    @FXML
    private TableColumn<Menu, String> nameColumn;
    @FXML
    private TableColumn<Menu, String> addButtonColumn;
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

    //will add to order detail
    private int menuIDToAddInOrderDetail;
    private String menuNameToAddInOrderDetail;
    private String menuTypeToAddInOrderDetail;
    private double menuPriceToAddInOrderDetail;
    private int toppingIDToAddInOrderDetail;
    private String toppingNameToAddInOrderDetail;
    private int quantityToAddInOrderDetail;
    private double totalPriceToAddInOrderDetail;

    private  List<OrderAllDetail> orderAllDetails = new ArrayList<>();
    private int orderNow = 0;

     //orderDetail tableView
     @FXML
     private TableView<OrderAllDetail> orderDetailTable;
     @FXML
     private TableColumn<OrderDetail, String> orderDetailMenuNameColumn;
    @FXML
    private TableColumn<OrderDetail, String> orderDetailToppingNameColumn;
    @FXML
    private TableColumn<OrderDetail, String> orderDetailQuantityColumn;
    @FXML
    private TableColumn<OrderDetail, String> orderDetailTotalPriceColumn;
     @FXML
     private TableColumn<OrderDetail, String> orderDetailDeleteButtonColumn;


    private String typeToShow ;
    public void initialize() {
        System.out.println("-------CreateOrderPage------");

        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT COUNT(*) AS order_count FROM orders";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                orderNow = resultSet.getInt("order_count");
            }
            orderNow+= 1;
            System.out.println(">> เตรียมเพี่มออร์เดอร์ ที่ "+ orderNow+" <<");
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        addButtonColumn.setCellFactory(column -> {
            return new TableCell<Menu, String>() {
                final Button button = new Button("+");

                {
                    button.setOnAction(event -> {
                        // Access the menu_name from the selected row
                        Menu menu = getTableView().getItems().get(getIndex());
                        menuIDToAddInOrderDetail = menu.getMenu_id();
                        menuNameToAddInOrderDetail = menu.getMenu_name();
                        menuTypeToAddInOrderDetail = menu.getMenu_type();
                        menuPriceToAddInOrderDetail = menu.getMenu_price();

                        // Now you can send the menu_name to another action
                        handleButtonAction();
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);
                    }
                }
            };
        });

        typeToShow = "All";
        updateDatabase();
        setupTopping();
        addMenuPane.setVisible(false);
        quantityLabel.setText("1");


    }
    private void handleButtonAction() {

        System.out.println("Button clicked for menu_name: " + menuNameToAddInOrderDetail);
        addMenuPane.setVisible(true);
        //chooseMenuPane
        chooseMenuPane.setVisible(false);
        nameLabel.setText(menuNameToAddInOrderDetail);
        typeLabel.setText(menuTypeToAddInOrderDetail);
        priceLabel.setText(String.valueOf(menuPriceToAddInOrderDetail));

        updatePriceWithTopping();
        toppingComboBox.getSelectionModel().select(0);
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
        toppingComboBox.getSelectionModel().select(0);
    }
    @FXML
    public void handlePlusQuantityButton(ActionEvent actionEvent) {
        int currentQuantity = Integer.parseInt(quantityLabel.getText());

        if (currentQuantity < 10) {
            currentQuantity++;
        }


        quantityLabel.setText(Integer.toString(currentQuantity));
        updatePriceWithTopping();
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
        updatePriceWithTopping();
    }
    private void updatePriceWithTopping(){
        quantityToAddInOrderDetail = Integer.parseInt(quantityLabel.getText());
        double toppingPrice = 0;
        String selectedTopping = (String) toppingComboBox.getValue();
        System.out.println(" "+selectedTopping+" ");

        String query = "SELECT topping_id, topping_name, topping_price FROM toppings WHERE topping_name = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedTopping);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("--Update Topping to set--");
            while (resultSet.next()) {
                toppingIDToAddInOrderDetail = resultSet.getInt("topping_id");
                toppingNameToAddInOrderDetail = resultSet.getString("topping_name");
                toppingPrice = resultSet.getDouble("topping_price");

                System.out.println(" "+toppingPrice+" ");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        totalPriceToAddInOrderDetail = (menuPriceToAddInOrderDetail+toppingPrice)*quantityToAddInOrderDetail;
        totalLabel.setText("รวม :"+totalPriceToAddInOrderDetail+" บาท");
    }
    @FXML
    public void updatePriceWithToppingAction(ActionEvent actionEvent){
        updatePriceWithTopping();
    }
    @FXML
    public void handleCancelMenuButton(ActionEvent actionEvent){
        addMenuPane.setVisible(false);
        chooseMenuPane.setVisible(true);

    }
    @FXML
    public void handleAcceptMenuButton(ActionEvent actionEvent) {
        addMenuPane.setVisible(false);
        chooseMenuPane.setVisible(true);
        updatePriceWithTopping();

        // Check if the menu and topping already exist in orderAllDetails
        boolean found = false;
        for (OrderAllDetail detail : orderAllDetails) {
            if (detail.getMenu_id() == menuIDToAddInOrderDetail && detail.getTopping_id() == toppingIDToAddInOrderDetail) {
                found = true;
                // Update the quantity if it's within the limit (not exceeding 10)
                int newQuantity = detail.getQuantity() + quantityToAddInOrderDetail;
                if (newQuantity <= 10) {
                    detail.setQuantity(newQuantity);
                } else {
                    // Handle exceeding quantity (e.g., show an error message)
                }
                break;
            }
        }

        if (!found) {
            orderAllDetails.add(new OrderAllDetail(1, orderNow, menuIDToAddInOrderDetail, menuNameToAddInOrderDetail,
                    toppingIDToAddInOrderDetail, toppingNameToAddInOrderDetail, quantityToAddInOrderDetail, totalPriceToAddInOrderDetail));
        }

        System.out.println("เพิ่มรายการ มีเมนูที่ " + menuIDToAddInOrderDetail + " ชื่อ " + menuNameToAddInOrderDetail +
                " ท็อปปิ้งที่ " + toppingIDToAddInOrderDetail + " ชื่อ " + toppingNameToAddInOrderDetail +
                " จำนวน " + quantityToAddInOrderDetail + " แก้ว ราคา : " + totalPriceToAddInOrderDetail);

        updateOrderTable();
    }
    private void updateOrderTable(){

        orderDetailMenuNameColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        orderDetailToppingNameColumn.setCellValueFactory(new PropertyValueFactory<>("topping_name"));

        orderDetailQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDetailTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        orderDetailDeleteButtonColumn.setCellFactory(column -> {
            return new TableCell<OrderDetail, String>() {
                final Button button = new Button("x");

                {
                    button.setOnAction(event -> {

                        // Now you can send the menu_name to another action
                        handleDeleteButtonAction();
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(button);
                    }
                }
            };
        });

        orderDetailTable.setItems(FXCollections.observableList(orderAllDetails));

    }
    private void handleDeleteButtonAction(){
        // Get the selected row from the table view
        OrderAllDetail selectedOrderAllDetail = orderDetailTable.getSelectionModel().getSelectedItem();

        if (selectedOrderAllDetail != null) {
            // Remove the selected order detail from the list
            orderAllDetails.remove(selectedOrderAllDetail);

            // Update the table view
            orderDetailTable.setItems(FXCollections.observableList(orderAllDetails));
        }
    }
    @FXML
    public void handleAcceptCreateOrderButton(ActionEvent actionEvent){
        try  {
            Connection connection = DatabaseConnection.getConnection();

            System.out.println(">> เพี่มออเดอร์ ที่ "+ orderNow+" <<");
            //add order detail

            String insertOrderDetailSQL = "INSERT INTO orderdetails (order_id, menu_id, topping_id, quantity,total_price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement orderDetailStatement = connection.prepareStatement(insertOrderDetailSQL);

            // Replace placeholders with actual data
            for (OrderAllDetail orderDetail : orderAllDetails) {
                // Replace placeholders with actual data
                orderDetailStatement.setInt(1, orderNow); // Use the same order ID for all order details
                orderDetailStatement.setInt(2, orderDetail.getMenu_id());
                orderDetailStatement.setInt(3, orderDetail.getTopping_id());
                orderDetailStatement.setInt(4, orderDetail.getQuantity());
                orderDetailStatement.setDouble(5, orderDetail.getTotal_price());

                // Execute the insert query for each order detail
                int detailRowsInserted = orderDetailStatement.executeUpdate();

                if (detailRowsInserted > 0) {
                    System.out.println("A new order detail was inserted successfully.");
                } else {
                    System.out.println("Failed to insert order detail.");
                }
            }


            //Update order with orderDetail
            // Calculate the total order price
            String calculateOrderPriceSQL = "SELECT SUM(total_price) AS total_order_price FROM orderdetails WHERE order_id = ?";
            PreparedStatement calculateOrderPriceStatement = connection.prepareStatement(calculateOrderPriceSQL);
            calculateOrderPriceStatement.setInt(1, orderNow); // Set the order_id

            ResultSet totalPriceResult = calculateOrderPriceStatement.executeQuery();

            double totalOrderPrice = 0; // Initialize total order price
            if (totalPriceResult.next()) {
                totalOrderPrice = totalPriceResult.getDouble("total_order_price");
            }
            System.out.println("Total Order Price: " + totalOrderPrice);

            // Update the orders table with the calculated order price
            String updateOrderPriceSQL = "INSERT INTO orders ( order_price, status) VALUES (?, ?)";
            PreparedStatement updateOrderPriceStatement = connection.prepareStatement(updateOrderPriceSQL);
            updateOrderPriceStatement.setDouble(1, totalOrderPrice);
            updateOrderPriceStatement.setString(2, "ยังไม่ชำระเงิน"); // Set the order_id

            // Execute the update query
            int rowsUpdated = updateOrderPriceStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Order price updated successfully.");
            } else {
                System.out.println("Failed to update order price.");
            }

            // Close the prepared statements
            calculateOrderPriceStatement.close();
            updateOrderPriceStatement.close();


        }
         catch (SQLException e) {
        e.printStackTrace();
        }





    }
    // change type
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
