package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.Member;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class AddMemberPageController {
    private ArrayList<Member> members = new ArrayList<>();
    @FXML private Label errorLabel;
    @FXML private TextField nameTextField;
    @FXML private TextField phoneTextField;


    public void initialize(){
        members = (ArrayList<Member>) com.github.saacsos.FXRouter.getData();
        errorLabel.setText("");
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("member",members);
        } catch (Exception err){
            System.out.println("Can't go to member");
        }
    }

    @FXML
    public void handleAddMemberButton(ActionEvent actionEvent){
        String name = nameTextField.getText().trim();
        String phoneNum = phoneTextField.getText().trim();
        if(!name.isBlank() && !phoneNum.isBlank()){
            if(phoneNum.length() != 10){
                errorLabel.setText("โปรดกรอกเบอร์โทรให้ครบ 10 หลัก");
            }
            else {
                try {
                    // เช็คว่าเป็นตัวเลขไหม
                    Integer.parseInt(phoneNum);
                    // เชื่อมต่อฐานข้อมูล
                    try  {
                        Connection connection = DatabaseConnection.getConnection();

                        // เขียนคำสั่ง SQL สำหรับ INSERT ข้อมูล
                        String insertSQL = "INSERT INTO members (member_name, member_phone_number) VALUES (?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                            preparedStatement.setString(1, name);
                            preparedStatement.setString(2, phoneNum);
                            // ทำการ INSERT ข้อมูล
                            preparedStatement.executeUpdate();
                            //clear
                            System.out.println(">> เพี่มสมาชิกชื่อ "+name+" เบอร์: "+ phoneNum);
                            nameTextField.setText("");
                            phoneTextField.setText("");
                            errorLabel.setText("");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } catch (Exception err){
                    errorLabel.setText("โปรดกรอกเพียงตัวเลขเท่านั้นในช่องเบอร์โทร");
                }
            }
        }else {
            errorLabel.setText("โปรดกรอกข้อมูลให้ครบทุกช่อง");
        }

    }
}
