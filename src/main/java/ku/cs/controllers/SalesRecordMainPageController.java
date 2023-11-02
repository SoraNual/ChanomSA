package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import ku.cs.models.SalesRecord;

public class SalesRecordMainPageController {
    @FXML TableView<SalesRecord> salesRecordTable;
    @FXML DatePicker recordDatePicker;

    String query = "SELECT m.menu_id, m.menu_name, SUM(od.quantity)  as total_quantity " +
            "FROM orderdetails as od " +
            "JOIN menus as m ON m.menu_id = od.menu_id " +
            "GROUP BY m.menu_name " +
            "ORDER BY total_quantity DESC";


}
