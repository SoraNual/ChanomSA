package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class OrderManagementPageController {
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go to main");
        }
    }
}
