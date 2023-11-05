package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.OrderAllDetail;
import ku.cs.models.Receipt;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReceiptPageController {
    @FXML
    private TableView<Receipt> receiptsTable;
    @FXML private TableColumn<Receipt, Integer> queueColumn;
    @FXML private TableColumn<Receipt, Integer> orderTotalQuantityColumn;
    @FXML private TableColumn<Receipt, LocalTime> receiptTimeColumn;
    @FXML private TableColumn<Receipt, Integer> receiptIdColumn;
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
    @FXML private Label receiptTotalQuantityLabel;
    @FXML private Label netPriceLabel;
    @FXML private Label receiptDateTimeLabel;
    @FXML private Label discountTextLabel;
    @FXML private Label discountPriceLabel;
    @FXML private DatePicker receiptDatePicker;
    private Receipt currentReceipt;
    private LocalDate currentDatePicked;
    private final ArrayList<OrderAllDetail> orderAllDetails = new ArrayList<>();
    private final ArrayList<Receipt> receipts = new ArrayList<>();
    public void initialize() {
        queueLabel.setText("");
        receiptDateTimeLabel.setText("");
        discountTextLabel.setVisible(false);
        discountPriceLabel.setText("");


        currentReceipt = receiptsTable.getSelectionModel().getSelectedItem();
        receiptTotalQuantityLabel.setText("");

        /*receiptDatePicker.setValue(LocalDate.now());
        handleReceiptDatePicker();*/



        handleReceiptsTable();



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
                statement.setInt(1, currentReceipt.getOrder_id());
                ResultSet resultSet = statement.executeQuery();

                System.out.println("--Update Order Details Table--");
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

        } else if (readPrompt.equals("r")) {
            String query = "SELECT r.receipt_id, r.order_id, mb.member_phone_number, o.order_price, r.queue_num, receipt_dateTime, r.net_price, o.order_total_quantity " +
                    "FROM receipts as r JOIN orders as o ON o.order_id = r.order_id LEFT JOIN members as mb ON mb.member_id = o.member_id " +
                    "WHERE DATE(r.receipt_dateTime) = ?";

            try {
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, currentDatePicked.toString());
                ResultSet resultSet = statement.executeQuery();

                System.out.println("--Update Receipt Table--");
                while (resultSet.next()) {
                    int receiptID = resultSet.getInt("receipt_id");
                    int orderID = resultSet.getInt("order_id");
                    String memPhoneNum = resultSet.getString("member_phone_number");
                    LocalDateTime receiptDateTime = resultSet.getTimestamp("receipt_dateTime").toLocalDateTime();
                    int orderTotalQuantity = resultSet.getInt("order_total_quantity");
                    int queueNum = resultSet.getInt("queue_num");
                    double netPrice = resultSet.getDouble("net_price");
                    double orderPrice = resultSet.getDouble("order_price");

                    Receipt newReceipt = new Receipt(receiptID, orderID, queueNum,netPrice);
                    newReceipt.setReceipt_dateTime(receiptDateTime);
                    newReceipt.setTotal_quantity(orderTotalQuantity);
                    newReceipt.setMember_phone_number(memPhoneNum);
                    newReceipt.setDiscount_price(orderPrice-netPrice);

                    receipts.add(newReceipt);

                    System.out.println("orderID "+ orderID + " orderTotalQuantity " + orderTotalQuantity + " netPrice " +  netPrice +
                            " บาท discount" + newReceipt.getDiscount_price() + " บาท " + " DatePicked " + currentDatePicked + " queue " + queueNum + " DateTime " + receiptDateTime);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            updateReceiptsTable();
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

    private void updateReceiptsTable(){
        queueColumn.setCellValueFactory(new PropertyValueFactory<>("queue_num"));
        queueColumn.setCellFactory(column -> {
            return new TableCell<Receipt, Integer>() {
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
        receiptTimeColumn.setCellValueFactory(new PropertyValueFactory<>("receipt_time"));
        receiptTimeColumn.setCellFactory(column -> {
            return new TableCell<Receipt, LocalTime>() {
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

        orderTotalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("total_quantity"));
        orderTotalQuantityColumn.setCellFactory(column -> {
            return new TableCell<Receipt, Integer>() {
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
        receiptIdColumn.setCellValueFactory(new PropertyValueFactory<>("receipt_id"));
        receiptIdColumn.setCellFactory(column -> {
            return new TableCell<Receipt, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อ cell ว่าง
                    } else {
                        setText(item+""); // แปลงข้อมูลเป็น String แล้วกำหนดให้ setText
                        setAlignment(Pos.CENTER); // ตั้งค่าให้ข้อมูลอยู่ตรงกลางเมื่อมีข้อมูล
                    }
                }
            };
        });
        receiptsTable.setItems(FXCollections.observableList(receipts));
    }

    @FXML private void handleReceiptsTable(){
        receiptsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            currentReceipt = newSelection;
            orderDetailTable.getItems().clear();

            System.out.println(currentReceipt.getMember_phone_number() + currentReceipt.getDiscount_price());
            if(currentReceipt.getMember_phone_number() != null){
                System.out.println("has phone num");
                discountTextLabel.setVisible(true);
                discountPriceLabel.setText(currentReceipt.getDiscount_price()+"");
            }else {
                discountPriceLabel.setText("");
                discountTextLabel.setVisible(false);
            }

            String s = currentReceipt.getReceipt_dateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            receiptDateTimeLabel.setText(s);
            queueLabel.setText("#"+ currentReceipt.getQueue_num());
            receiptTotalQuantityLabel.setText(currentReceipt.getTotal_quantity()+" แก้ว");
            netPriceLabel.setText(currentReceipt.getNet_price()+" บาท");

            if(newSelection != null)
                readDB("od");
        });

    }


    @FXML private void handleReceiptDatePicker(){
        clearReceiptTable();
        clearSelection();

        currentDatePicked = receiptDatePicker.getValue();

        System.out.println(currentDatePicked);

        readDB("r");
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
        receiptTotalQuantityLabel.setText("");
        discountPriceLabel.setText("");
        discountTextLabel.setVisible(false);
        queueLabel.setText("");

    }
    private void clearReceiptTable(){
        receiptsTable.getSelectionModel().clearSelection();
        receiptsTable.getItems().clear();
    }
}
