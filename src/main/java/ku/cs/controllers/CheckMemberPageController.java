package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import ku.cs.models.Menu;
import ku.cs.models.Order;
import ku.cs.models.OrderAllDetail;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private Order currentOrder;
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

        readDB();
    }

    private void readDB(){

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
        updateOrderTable();
    }
    private void updateOrderTable(){

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
    public void handleBackButton(ActionEvent actionEvent){
        try {
            FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go back");
        }
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
                checkStatusLabel.setText("พบรายชื่อ");

                notUseOfferRadio.setDisable(true);
                useOfferRadio.setDisable(true);
                phoneTextField.setEditable(false);

                checkMemberButton.disableProperty().unbind();
                checkMemberButton.setDisable(true);
                confirmButton.disableProperty().unbind();
                confirmButton.setDisable(false);

                System.out.println(memberID+" "+memberName);
            }
            if(checkStatusLabel.getText().equals("")) {
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
        if(checkStatusLabel.getText().equals("พบรายชื่อ")){
            String updatePhoneNum = "UPDATE orders SET use_phone_number = ? WHERE order_id = ?";
            checkStatusLabel.setText("");
            try {
                Connection connection = DatabaseConnection.getConnection();

                PreparedStatement updateStatement = connection.prepareStatement(updatePhoneNum);
                updateStatement.setString(1, phoneTextField.getText());
                updateStatement.setInt(2, currentOrder.getOrder_id());
                updateStatement.executeUpdate();


                updateStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go main");
        }
    }
}
