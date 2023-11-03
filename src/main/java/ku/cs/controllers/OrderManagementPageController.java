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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class OrderManagementPageController {
    // Order table
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> queueColumn;
    @FXML private TableColumn<Order, Integer> orderTotalQuantityColumn;
    @FXML private TableColumn<Order, LocalTime> orderTimeColumn;
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
    @FXML private Label useMembershipLabel;
    @FXML private ComboBox<String> orderStatusComboBox;
    @FXML private Button cancelOrderButton;
    @FXML private Button advanceOrderStatusButton;
    private Order currentOrder;
    private final ArrayList<OrderAllDetail> orderAllDetails = new ArrayList<>();
    private final ArrayList<Order> orders = new ArrayList<>();
    private final ObservableList<String> orderStatusList = FXCollections.observableArrayList("ยังไม่ชำระเงิน","ชำระเงินแล้ว","รอรับสินค้า","รับสินค้าแล้ว","ถูกปฏิเสธ");
    private String readPrompt;
    private String currentOrderStatus;
    public void initialize() {
        queueLabel.setText("");
        useMembershipLabel.setText("");

        currentOrder = ordersTable.getSelectionModel().getSelectedItem();
        orderTotalQuantityLabel.setText("");
        orderTotalPriceLabel.setText("");

        orderStatusComboBox.setItems(orderStatusList);
        orderStatusComboBox.getSelectionModel().selectFirst();
        currentOrderStatus = orderStatusComboBox.getSelectionModel().getSelectedItem();
        System.out.println(currentOrderStatus);

        handleStatusChangingButtons();
        handleOrderTable();
        readDB("o");


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

                System.out.println("--Update Order Details Table--");
                while (resultSet.next()) {
                    String menuName = resultSet.getString("menu_name");
                    String toppingName = resultSet.getString("topping_name");
                    int quantity = resultSet.getInt("quantity");
                    double orderPrice = resultSet.getDouble("total_price");

                    if(currentOrder.getUse_phone_number() != null)
                        useMembershipLabel.setText("มีการใช้สิทธิ์ของสมาชิก");
                    else
                        useMembershipLabel.setText("");
                    queueLabel.setText("#"+currentOrder.getQueue_number());
                    orderTotalQuantityLabel.setText(currentOrder.getOrder_total_quantity()+" แก้ว");
                    orderTotalPriceLabel.setText(currentOrder.getOrder_price()+" บาท");

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
            String query = "SELECT o.order_id, o.order_dateTime, o.order_total_quantity, mb.member_phone_number, r.queue_num, r.net_price " +
                    "FROM orders as o JOIN receipts as r ON r.order_id = o.order_id LEFT JOIN members as mb ON mb.member_id = o.member_id " +
                    "WHERE o.status = ? AND DATE(o.order_dateTime) = ?";

            try {
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, currentOrderStatus);
                statement.setString(2, LocalDate.now().toString());
                ResultSet resultSet = statement.executeQuery();

                System.out.println("--Update Order Table--");
                while (resultSet.next()) {
                    int orderID = resultSet.getInt("order_id");
                    LocalDateTime orderDateTime = resultSet.getTimestamp("order_dateTime").toLocalDateTime();
                    int orderTotalQuantity = resultSet.getInt("order_total_quantity");
                    String phoneNumber = resultSet.getString("member_phone_number");
                    int queueNum = resultSet.getInt("queue_num");
                    double netPrice = resultSet.getDouble("net_price");

                    Order order = new Order(orderID, orderTotalQuantity, netPrice, currentOrderStatus, phoneNumber);
                    order.setQueue_number(queueNum);
                    order.setOrder_dateTime(orderDateTime);
                    orders.add(order);

                    System.out.println("orderID "+ orderID + " orderTotalQuantity " + orderTotalQuantity + " netPrice " +  netPrice + " Status " + currentOrderStatus +
                            " phone " + phoneNumber + " queue " + queueNum + " DateTime " + orderDateTime);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            updateOrdersTable();
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
                        setText(item); // แปลงข้อมูลเป็น String แล้วกำหนดให้ setText
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
                        setText(item); // แปลงข้อมูลเป็น String แล้วกำหนดให้ setText
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
        queueColumn.setCellValueFactory(new PropertyValueFactory<>("queue_number"));
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
        orderTimeColumn.setCellValueFactory(new PropertyValueFactory<>("order_time"));
        orderTimeColumn.setCellFactory(column -> {
            return new TableCell<Order, LocalTime>() {
                @Override
                protected void updateItem(LocalTime item, boolean empty) {
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

        orderTotalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("order_total_quantity"));
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
                        setText(item); // แปลงข้อมูลเป็น String แล้วกำหนดให้ setText
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อมีข้อมูล
                    }
                }
            };
        });
        ordersTable.setItems(FXCollections.observableList(orders));
    }

    private void updateOrderStatusDB(){
        String updatePhoneNum = "UPDATE orders SET status = ? WHERE order_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement updateStatement = connection.prepareStatement(updatePhoneNum);
            updateStatement.setString(1, currentOrder.getStatus());
            updateStatement.setInt(2, currentOrder.getOrder_id());
            updateStatement.executeUpdate();


            updateStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML private void handleOrderTable(){
        ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            currentOrder = newSelection;
            orderDetailTable.getItems().clear();
            if(newSelection != null)
                readDB("od");
        });

    }
    @FXML private void handleAdvanceOrderStatusChangeButton(){
        String nextStatus = orderStatusList.get(orderStatusList.indexOf(currentOrderStatus)+1);
        currentOrder.setStatus(nextStatus);

        updateOrderStatusDB();
        clearSelection();
        clearOrderTable();
        readDB("o");
    }

    @FXML private void handleCancelOrderStatusChangeButton(){
        currentOrder.setStatus("ถูกปฏิเสธ");

        updateOrderStatusDB();
        clearSelection();
        clearOrderTable();
        readDB("o");
    }
    @FXML private void handleOrderStatusComboBox(){
        clearOrderTable();
        clearSelection();

        currentOrderStatus = orderStatusComboBox.getSelectionModel().getSelectedItem();

        handleStatusChangingButtons();
        System.out.println(currentOrderStatus);

        readDB("o");
    }
    @FXML public void handleStatusChangingButtons(){

        cancelOrderButton.setVisible(currentOrderStatus.equals("ยังไม่ชำระเงิน"));


        if(currentOrderStatus.equals("ยังไม่ชำระเงิน")){
            advanceOrderStatusButton.setVisible(true);
            advanceOrderStatusButton.setText("ยืนยันการชำระเงิน");
        } else if (currentOrderStatus.equals("ชำระเงินแล้ว")) {
            advanceOrderStatusButton.setVisible(true);
            advanceOrderStatusButton.setText("รอรับสินค้า");
        } else if (currentOrderStatus.equals("รอรับสินค้า")) {
            advanceOrderStatusButton.setVisible(true);
            advanceOrderStatusButton.setText("รับสินค้าแล้ว");
        } else if (currentOrderStatus.equals("รับสินค้าแล้ว") || currentOrderStatus.equals("ถูกปฏิเสธ")) {
            advanceOrderStatusButton.setVisible(false);
        }


    }
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go to main");
        }
    }

    private void clearSelection(){
        System.out.println("CLEARING SELECTION");
        orderDetailTable.getItems().clear();
        useMembershipLabel.setText("");
        orderTotalQuantityLabel.setText("");
        orderTotalPriceLabel.setText("");
        queueLabel.setText("");

    }
    private void clearOrderTable(){
        ordersTable.getSelectionModel().clearSelection();
        ordersTable.getItems().clear();
    }
}
