package ku.cs.form.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AddMenuPageController {

    @FXML
    public void initialize(){

        //setup

    }

    //back button
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go back");
        }
    }
}
