package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import ku.cs.models.Menu;
import ku.cs.models.OrderAllDetail;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckMemberPageController {
    @FXML
    private TableView<OrderAllDetail> orderDetailTable;
    @FXML
    private TableColumn<OrderAllDetail, String> orderDetailMenuNameColumn;
    @FXML
    private TableColumn<OrderAllDetail, String> orderDetailToppingNameColumn;
    @FXML
    private TableColumn<OrderAllDetail, String> orderDetailQuantityColumn;
    @FXML
    private TableColumn<OrderAllDetail, String> orderDetailTotalPriceColumn;
    @FXML
    private TableColumn<OrderAllDetail, ImageView> menuPictureColumn;

    private double orderTotalPrice;
    @FXML
    private Label orderTotalPriceLabel;
    private int orderTotalQuantity;
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

    private List<OrderAllDetail> orderAllDetails = new ArrayList<>();

    public void initialize() {
        nameLabel.setText("");
        checkStatusLabel.setText("");
        menuPictureColumn.setCellFactory(column -> new TableCell<OrderAllDetail, ImageView>() {
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
        menuPictureColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));

        phoneTextField.disableProperty().bind(useOfferRadio.selectedProperty());
        checkMemberButton.disableProperty().bind(useOfferRadio.selectedProperty());

    }

    private void updateOrderTable(){

        orderDetailMenuNameColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        orderDetailToppingNameColumn.setCellValueFactory(new PropertyValueFactory<>("topping_name"));

        orderDetailQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDetailTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("total_price"));


        orderDetailTable.setItems(FXCollections.observableList(orderAllDetails));
        updateOrderTotalPriceAndQuantityLabel();


    }
    private void updateOrderTotalPriceAndQuantityLabel(){
        orderTotalPrice = 0;
        orderTotalQuantity = 0;
        for (OrderAllDetail detail : orderAllDetails) {
            orderTotalPrice += detail.getTotal_price();
            orderTotalQuantity += detail.getQuantity();
        }
        orderTotalPriceLabel.setText(String.valueOf(orderTotalPrice+" บาท"));
        orderTotalQuantityLabel.setText(String.valueOf(orderTotalQuantity+" แก้ว"));

    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("main");
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
        String query = "UPDATE orders SET use_phone_number = ? WHERE order_id = ?";
        /*checkStatusLabel.setText("");
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
        }*/
        try {
            com.github.saacsos.FXRouter.goTo("back");
        } catch (Exception err){
            System.out.println("Can't go back");
        }
    }
}
