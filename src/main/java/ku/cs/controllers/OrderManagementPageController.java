package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Order;
import ku.cs.models.OrderAllDetail;
import ku.cs.services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class OrderManagementPageController {
    // Order table
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> queueColumn;
    @FXML private TableColumn<Order, Integer> orderTotalQuantityColumn;
    @FXML private TableColumn<Order, Timestamp> orderDateTimeColumn;
    @FXML private TableColumn<Order, String> phoneColumn;
    // Order Details table
    @FXML
    private TableView<OrderAllDetail> orderDetailTable;
    @FXML
    private TableColumn<OrderAllDetail, String> orderDetailMenuNameColumn;
    @FXML
    private TableColumn<OrderAllDetail, String> orderDetailToppingNameColumn;
    @FXML
    private TableColumn<OrderAllDetail, Integer> orderDetailQuantityColumn;
    @FXML
    private TableColumn<OrderAllDetail, Double> orderDetailTotalPriceColumn;

    @FXML private Label queueLabel;
    @FXML private Label orderTotalQuantityLabel;
    @FXML private Label orderTotalPriceLabel;
    @FXML private ComboBox<String> orderStatusComboBox;

    private Order currentOrder;
    private ArrayList<OrderAllDetail> orderAllDetails;
    private ArrayList<Order> orders;
    private ObservableList<String> orderStatusList = FXCollections.observableArrayList("ยังไม่ชำระเงิน","ชำระเงินแล้ว","รอรับสินค้า","รับสินค้าแล้ว","ถูกปฎิเสธ");
    private String readPrompt;
    private String currentOrderStatus;
    public void initialize() {
        queueLabel.setText("");

        currentOrder = ordersTable.getSelectionModel().getSelectedItem();
        orderTotalQuantityLabel.setText("");
        orderTotalPriceLabel.setText("");

        orderStatusComboBox.setItems(orderStatusList);
        orderStatusComboBox.getSelectionModel().selectFirst();
        currentOrderStatus = orderStatusComboBox.getSelectionModel().getSelectedItem();
        System.out.println(currentOrderStatus);





    }
    private void readDB(String readPrompt){
        if(readPrompt.equals("od")){
            // read for order detail table
            String query = "SELECT m.menu_name, t.topping_name, od.quantity, od.total_price " +
                    "FROM orderdetails as od JOIN orders as o ON od.order_id = o.order_id " +
                    "JOIN menus as m ON m.menu_id = od.menu_id JOIN toppings as t ON t.topping_id = od.topping_id " +
                    "WHERE od.order_id = ? ORDER BY m.menu_id;";

            try {
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, currentOrder.getOrder_id());
                ResultSet resultSet = statement.executeQuery();

                System.out.println("--Update Order Table--");
                while (resultSet.next()) {
                    String menuName = resultSet.getString("menu_name");
                    String toppingName = resultSet.getString("topping_name");
                    int quantity = resultSet.getInt("quantity");
                    double orderPrice = resultSet.getDouble("total_price");

                    OrderAllDetail orderAllDetail = new OrderAllDetail(menuName,toppingName,quantity,orderPrice);
                    orderAllDetails.add(orderAllDetail);

                    System.out.println(menuName+" "+toppingName+" "+quantity+" "+orderPrice);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            updateOrderDetailsTable();
        } else if (readPrompt.equals("o")) {
            //TODO
        }
    }
    private void updateOrderDetailsTable(){

        orderDetailMenuNameColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        orderDetailMenuNameColumn.setCellFactory(column -> {
            return new TableCell<OrderAllDetail, String>() {
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
        orderDetailToppingNameColumn.setCellValueFactory(new PropertyValueFactory<>("topping_name"));
        orderDetailToppingNameColumn.setCellFactory(column -> {
            return new TableCell<OrderAllDetail, String>() {
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

        orderDetailQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDetailQuantityColumn.setCellFactory(column -> {
            return new TableCell<OrderAllDetail, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
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
        orderDetailTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        orderDetailTotalPriceColumn.setCellFactory(column -> {
            return new TableCell<OrderAllDetail, Double>() {
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



        orderDetailTable.setItems(FXCollections.observableList(orderAllDetails));


    }

    private void updateOrdersTable(){
        queueColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        queueColumn.setCellFactory(column -> {
            return new TableCell<Order, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
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
        orderDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("topping_name"));
        orderDateTimeColumn.setCellFactory(column -> {
            return new TableCell<Order, Timestamp>() {
                @Override
                protected void updateItem(Timestamp item, boolean empty) {
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

        orderTotalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderTotalQuantityColumn.setCellFactory(column -> {
            return new TableCell<Order, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
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
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("use_phone_number"));
        phoneColumn.setCellFactory(column -> {
            return new TableCell<Order, String>() {
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
        ordersTable.setItems(FXCollections.observableList(orders));
    }

    @FXML private void handleOrderStatusComboBox(){
        currentOrderStatus = orderStatusComboBox.getSelectionModel().getSelectedItem();
        System.out.println(currentOrderStatus);
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go to main");
        }
    }
}
