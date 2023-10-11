package ku.cs.form.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainPageController {

    @FXML private AnchorPane scenePane;

    @FXML
    public void initialize(){

        //setup

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



}
