package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainPageController {

    @FXML private AnchorPane scenePane;

    @FXML
    public void initialize(){
        System.out.println("-------MainPage------");
        //setup

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



}
