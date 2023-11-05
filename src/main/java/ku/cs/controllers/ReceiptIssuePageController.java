package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Order;
import ku.cs.models.OrderAllDetail;
import ku.cs.services.DatabaseConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceiptIssuePageController {
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

    @FXML
    private TableView<OrderAllDetail> orderDetailTable1;
    @FXML
    private TableColumn<OrderAllDetail, String> orderDetailMenuNameColumn1;
    @FXML
    private TableColumn<OrderAllDetail, String> orderDetailToppingNameColumn1;
    @FXML
    private TableColumn<OrderAllDetail, Integer> orderDetailQuantityColumn1;
    @FXML
    private TableColumn<OrderAllDetail, Double> orderDetailTotalPriceColumn1;

    @FXML private Label orderTotalPriceLabel;
    @FXML private Label orderTotalQuantityLabel;
    @FXML private Label netPriceLabel;
    @FXML private Label receiptTotalQuantityLabel;
    @FXML private Label queueLabel;
    @FXML private Label receiptDateTimeLabel;
    @FXML private Label discountTextLabel;
    @FXML private Label discountPriceLabel;

    private Order currentOrder;
    private List<OrderAllDetail> orderAllDetails = new ArrayList<>();

    public void initialize() {

        currentOrder = (Order) com.github.saacsos.FXRouter.getData();

        queueLabel.setText(currentOrder.getQueue_number()+"");

        orderTotalQuantityLabel.setText(currentOrder.getOrder_total_quantity()+" แก้ว");
        receiptTotalQuantityLabel.setText(currentOrder.getOrder_total_quantity()+" แก้ว");

        orderTotalPriceLabel.setText(currentOrder.getOrder_price()+" บาท");
        netPriceLabel.setText(currentOrder.getOrder_price()+" บาท");

        discountPriceLabel.setText("");
        discountTextLabel.setVisible(false);

        readDB("od");
        readDB("q");


    }
    private void readDB(String readPrompt){

        if(readPrompt.equals("od")){
            // read for order detail table
            String query = "SELECT m.menu_name, t.topping_name, od.quantity, od.total_price " +
                    "FROM orderdetails as od " +
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
        } else if (readPrompt.equals("q")) {
            // read for order detail table
            String query = "SELECT queue_num, receipt_dateTime, net_price FROM receipts WHERE order_id = ?";

            try {
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, currentOrder.getOrder_id());
                ResultSet resultSet = statement.executeQuery();

                System.out.println("--Getting Latest Queue--");
                while (resultSet.next()) {
                    int queueNum = resultSet.getInt("queue_num");
                    Timestamp receiptDateTime = resultSet.getTimestamp("receipt_dateTime");
                    double netPrice = resultSet.getDouble("net_price");

                    String s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(receiptDateTime);

                    queueLabel.setText("#"+queueNum);
                    receiptDateTimeLabel.setText(s);
                    netPriceLabel.setText(netPrice+" บาท");
                    if(!currentOrder.getUse_phone_number().isBlank()){
                        discountTextLabel.setVisible(true);
                        discountPriceLabel.setText((currentOrder.getOrder_price()-netPrice)+" บาท");
                    }

                    System.out.println(queueNum+": "+s+" "+netPrice);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

        orderDetailMenuNameColumn1.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        orderDetailMenuNameColumn1.setCellFactory(column -> {
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
        orderDetailToppingNameColumn1.setCellValueFactory(new PropertyValueFactory<>("topping_name"));
        orderDetailToppingNameColumn1.setCellFactory(column -> {
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

        orderDetailQuantityColumn1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDetailQuantityColumn1.setCellFactory(column -> {
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
        orderDetailTotalPriceColumn1.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        orderDetailTotalPriceColumn1.setCellFactory(column -> {
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

        orderDetailTable1.setItems(FXCollections.observableList(orderAllDetails));

    }

    public void handlePrintReceiptButton(ActionEvent actionEvent) {
    }

    public void handleHomeButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go to main page");
        }
    }
}
