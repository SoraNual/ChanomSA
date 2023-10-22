package ku.cs.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Order {
    private int order_id;
    private double order_price;
    private Timestamp order_dateTime;
    private String status;
    private String use_phone_number;

    public Order(double order_price, String status, String use_phone_number) {
        this.order_price = order_price;
        this.status = status;
        this.use_phone_number = use_phone_number;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUse_phone_number() {
        return use_phone_number;
    }

    public void setUse_phone_number(String use_phone_number) {
        this.use_phone_number = use_phone_number;
    }
}
