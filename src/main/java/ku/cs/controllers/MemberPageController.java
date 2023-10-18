package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Member;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MemberPageController {
    @FXML
    private TableView<Member> memberTable;
    @FXML private TableColumn<Member, Integer> IDColumn;
    @FXML private TableColumn<Member, String> nameColumn;
    @FXML private TableColumn<Member, String> phoneColumn;
    ArrayList<Member> members = new ArrayList<>();
    public void initialize() {
        System.out.println("-------MemberPage------");
        // Set cell value factories
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("member_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("member_name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("member_phone_number"));


        // Retrieve menu items from the database and display in the table


        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT member_id, member_name, member_phone_number FROM members";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int memberId = resultSet.getInt("member_id");
                String memberName = resultSet.getString("member_name");
                String memberPhoneNum = resultSet.getString("member_phone_number");

                members.add(new Member(memberId,memberName, memberPhoneNum));
                System.out.println(memberId+" "+memberName+" "+memberPhoneNum);
            }

            memberTable.setItems(FXCollections.observableList(members));

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddMemberButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("add-member",members);
        } catch (Exception err){
            System.out.println("Can't go to add-member");
        }
    }

}
