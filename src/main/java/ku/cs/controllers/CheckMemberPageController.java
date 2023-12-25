package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ku.cs.models.Order;
import ku.cs.models.OrderAllDetail;
import ku.cs.models.QueueNumber;
import ku.cs.services.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.github.saacsos.FXRouter;

public class CheckMemberPageController {
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
    private Label orderTotalPriceLabel;
    @FXML
    private Label orderTotalQuantityLabel;
    @FXML
    private Label orderIDLabel;
    @FXML private Label nameLabel;
    @FXML private Label checkStatusLabel;
    @FXML private TextField phoneTextField;
    @FXML private RadioButton useOfferRadio,notUseOfferRadio;
    @FXML private ToggleGroup radioGroup;
    @FXML private Button checkMemberButton;
    @FXML private Button confirmButton;
    @FXML private Pane receiptPane;
    private Order currentOrder;
    private int usedMemberId;
    private List<OrderAllDetail> orderAllDetails = new ArrayList<>();

    public void initialize() {
        nameLabel.setText("");
        checkStatusLabel.setText("");

        currentOrder = (Order) FXRouter.getData();

        confirmButton.setDisable(true);
        confirmButton.disableProperty().bind(notUseOfferRadio.selectedProperty().not());

        phoneTextField.disableProperty().bind(useOfferRadio.selectedProperty().not());
        checkMemberButton.disableProperty().bind(useOfferRadio.selectedProperty().not());

        orderIDLabel.setText(currentOrder.getOrder_id()+"");
        orderTotalQuantityLabel.setText(currentOrder.getOrder_total_quantity()+"");
        orderTotalPriceLabel.setText(currentOrder.getOrder_price()+"");

        receiptPane.setVisible(false);

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
            String query = "SELECT queue_num, receipt_dateTime FROM receipts WHERE order_id = ?";

            try {
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, currentOrder.getOrder_id()-1);
                ResultSet resultSet = statement.executeQuery();

                System.out.println("--Getting Latest Queue--");
                while (resultSet.next()) {
                    int queueNum = resultSet.getInt("queue_num");
                    LocalDate receiptDate = resultSet.getTimestamp("receipt_dateTime").toLocalDateTime().toLocalDate();

                    setQueueInformation(queueNum,receiptDate);

                    System.out.println(queueNum+": "+receiptDate);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void setQueueInformation(int queueNum, LocalDate receiptDate){
        System.out.println("set queue "+queueNum);
        QueueNumber.setLastQueueNumber(queueNum);
        QueueNumber.setLatestDateInQueue(receiptDate);
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

    @FXML
    public void handleCheckButton(ActionEvent actionEvent){
        String phoneNum = phoneTextField.getText().trim();
        String memberName;
        String query = "SELECT member_id, member_name FROM members WHERE member_phone_number = ?";
        checkStatusLabel.setText("");
        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, phoneNum);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int memberID = resultSet.getInt("member_id");
                memberName = resultSet.getString("member_name");

                nameLabel.setText(memberName);
                checkStatusLabel.setTextFill(Color.GREEN);
                checkStatusLabel.setText("พบรายชื่อ");

                notUseOfferRadio.setDisable(true);
                useOfferRadio.setDisable(true);
                phoneTextField.setEditable(false);

                checkMemberButton.disableProperty().unbind();
                checkMemberButton.setDisable(true);
                confirmButton.disableProperty().unbind();
                confirmButton.setDisable(false);
                usedMemberId = memberID;

                System.out.println(memberID+" "+memberName);
            }
            if(checkStatusLabel.getText().equals("")) {
                checkStatusLabel.setTextFill(Color.RED);
                checkStatusLabel.setText("ไม่พบรายชื่อ");
                nameLabel.setText("");
            }


            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleConfirmButton(ActionEvent actionEvent){
        String phoneNum = phoneTextField.getText();

        checkStatusLabel.disableProperty().unbind();
        checkStatusLabel.setVisible(false);
        notUseOfferRadio.setDisable(true);
        useOfferRadio.setDisable(true);

        if(checkStatusLabel.getText().equals("พบรายชื่อ")){
            currentOrder.setUse_phone_number(phoneNum);
            String updatePhoneNum = "UPDATE orders SET member_id = ? WHERE order_id = ?";

            try {
                Connection connection = DatabaseConnection.getConnection();

                PreparedStatement updateStatement = connection.prepareStatement(updatePhoneNum);
                updateStatement.setInt(1, usedMemberId);
                updateStatement.setInt(2, currentOrder.getOrder_id());
                updateStatement.executeUpdate();


                updateStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }

        receiptPane.setVisible(true);
        confirmButton.disableProperty().unbind();
        confirmButton.setDisable(true);

    }

    @FXML public void handleReceiptIssueButton(){
        System.out.println("phone"+ currentOrder.getUse_phone_number());
        try  {
            Connection connection = DatabaseConnection.getConnection();
            int currentQueue =  QueueNumber.generateQueueNumber();
            double discount = 1.;
            if(checkStatusLabel.getText().equals("พบรายชื่อ")){
                discount = 0.9;
            }

            // เขียนคำสั่ง SQL สำหรับ INSERT ข้อมูล
            String insertSQL = "INSERT INTO receipts (order_id, queue_num, net_price) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setInt(1, currentOrder.getOrder_id());
                preparedStatement.setInt(2, currentQueue);
                preparedStatement.setDouble(3, Math.floor(currentOrder.getOrder_price()*discount));
                // ทำการ INSERT ข้อมูล
                preparedStatement.executeUpdate();
                //clear
                System.out.println(">> เพี่มใบเสร็จที่ผูกกับออร์เดอร์ id "+ currentOrder.getOrder_id()+ " ซึ่งมีราคารวม: "+ currentOrder.getOrder_price() + " บาท\nซึ่งมีคิวหมายเลข " + currentQueue);
                currentOrder.setQueue_number(currentQueue);
                try {
                    FXRouter.goTo("receipt-issue", currentOrder);
                } catch (Exception err){
                    System.out.println("Can't go to receipt issue page");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
