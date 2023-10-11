package ku.cs.form.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MenuPageController {

    @FXML
    private AnchorPane scenePane;

    @FXML
    public void initialize(){

        //setup

    }


    //AddMenu button
    @FXML
    public void handleAddMenuButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("add-menu");
        } catch (Exception err){
            System.out.println("Can't go to add-menu");
        }
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
