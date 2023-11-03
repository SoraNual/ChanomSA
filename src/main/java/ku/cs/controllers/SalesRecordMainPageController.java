package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.SalesRecord;
import ku.cs.services.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SalesRecordMainPageController {
    @FXML TableView<SalesRecord> salesRecordTable;
    @FXML TableColumn<SalesRecord, Integer> rankColumn;
    @FXML TableColumn<SalesRecord, String> menuNameColumn;
    @FXML
    TableColumn<SalesRecord, Integer> totalQuantityColumn;
    @FXML DatePicker recordDatePicker;

    @FXML
    ToggleGroup filterGroup;
    @FXML RadioButton dateFilterRadioButton;
    @FXML RadioButton monthFilterRadioButton;
    @FXML RadioButton yearFilterRadioButton;

    private LocalDate currentDatePicked;
    private ArrayList<SalesRecord> salesRecords = new ArrayList<>();


    public void initialize() {


        recordDatePicker.setValue(LocalDate.now());
        handleRecordDatePicker();
    }

    private void readDB(String readPrompt) {

        String query = "SELECT m.menu_id, m.menu_name, SUM(od.quantity) as total_quantity FROM orderdetails as od JOIN menus as m ON m.menu_id = od.menu_id " +
                "JOIN orders as o ON o.order_id = od.order_id WHERE o.status != \"ยังไม่ชำระเงิน\" AND o.status != \"ถูกปฎิเสธ\" AND";

        if(readPrompt.equals("date")){
            query+=" Date(o.order_dateTime) = \"" + currentDatePicked.toString() + "\"";
        } else if (readPrompt.equals("month")) {
            query+=" MONTH(o.order_dateTime) = "+ currentDatePicked.getMonthValue() +  " AND YEAR(o.order_dateTime) = "+ currentDatePicked.getYear();
        } else if (readPrompt.equals("year")) {
            query+=" YEAR(o.order_dateTime) = "+ currentDatePicked.getYear();
        }

        query+=" GROUP BY m.menu_name ORDER BY total_quantity DESC;";

        System.out.println(query);
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("--Update Sales Record Table, Filtered by '" + readPrompt+ "'--");

            int rank = 1;
            while (resultSet.next()) {
                int menuID = resultSet.getInt("menu_id");
                String menuName = resultSet.getString("menu_name");
                int totalQuantity = resultSet.getInt("total_quantity");


                SalesRecord newRecord = new SalesRecord(rank, menuID, totalQuantity, menuName);
                salesRecords.add(newRecord);

                System.out.println("#"+ rank + " " + menuName + " ขายได้ทั้งหมด " + totalQuantity + " แก้ว");
                rank++;
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateSalesRecordsTable();


    }
    private void updateSalesRecordsTable(){
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rankColumn.setCellFactory(column -> {
            return new TableCell<SalesRecord, Integer>() {
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

        menuNameColumn.setCellValueFactory(new PropertyValueFactory<>("menu_name"));
        menuNameColumn.setCellFactory(column -> {
            return new TableCell<SalesRecord, String>() {
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

        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("total_quantity"));
        totalQuantityColumn.setCellFactory(column -> {
            return new TableCell<SalesRecord, Integer>() {
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
        salesRecordTable.setItems(FXCollections.observableList(salesRecords));
    }

    @FXML private void handleRecordDatePicker(){
        clearSalesRecordsTable();

        currentDatePicked = recordDatePicker.getValue();

        System.out.println(currentDatePicked);

        if(dateFilterRadioButton.isSelected())
            readDB("date");
        else if (monthFilterRadioButton.isSelected()) {
            readDB("month");
        } else if (yearFilterRadioButton.isSelected()) {
            readDB("year");
        }
    }

    private void clearSalesRecordsTable(){
        salesRecordTable.getItems().clear();
    }

    @FXML void handleDateFilterRadioButton(ActionEvent actionEvent){
        clearSalesRecordsTable();

        currentDatePicked = recordDatePicker.getValue();

        System.out.println("filter: Date " + currentDatePicked);

        readDB("date");
    }
    @FXML void handleMonthFilterRadioButton(ActionEvent actionEvent){
        clearSalesRecordsTable();

        currentDatePicked = recordDatePicker.getValue();

        System.out.println("filter: Month " + currentDatePicked);

        readDB("month");
    }
    @FXML void handleYearFilterRadioButton(ActionEvent actionEvent){
        clearSalesRecordsTable();

        currentDatePicked = recordDatePicker.getValue();

        System.out.println("filter: year " + currentDatePicked);

        readDB("year");}
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("main");
        } catch (Exception err){
            System.out.println("Can't go to main");
        }
    }


}
