package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MainPageController {

    @FXML private AnchorPane scenePane;

    @FXML
    public void initialize(){
        System.out.println("-------MainPage------");
        //
        System.out.println(Timestamp.valueOf(LocalDateTime.now()));

    }


    //go to create-order page
    @FXML
    public void handleCreateOrderPageButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("create-order");
        } catch (Exception err){
            System.out.println("Can't go to CreateOrderPage");
        }
    }
    //go to menu page
    @FXML
    public void handleMenuPageButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("menu");
        } catch (Exception err){
            System.out.println("Can't go to MenuPage");
        }
    }
    //go to topping page
    @FXML
    public void handleToppingPageButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("topping");
        } catch (Exception err){
            System.out.println("Can't go to ToppingPage");
        }
    }

    @FXML
    public void handleMemberPageButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("member");
        } catch (Exception err){
            System.out.println("Can't go to memberPage");
        }
    }
    @FXML
    public void handleOrderPageButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("order-management");
        } catch (Exception err){
            System.out.println("Can't go to orderPage");
            err.printStackTrace();
        }
    }
    @FXML
    public void handleReceiptPageButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("receipt");
        } catch (Exception err){
            System.out.println("Can't go to receiptPage");
            err.printStackTrace();
        }
    }
    @FXML
    public void handleSalesPageButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("sales-main");
        } catch (Exception err){
            System.out.println("Can't go to salesRecordsMainPage");
        }
    }



}
