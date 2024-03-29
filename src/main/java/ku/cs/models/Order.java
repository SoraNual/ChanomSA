package ku.cs.models;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Order {
    private int order_id;
    private double order_price;
    private int order_total_quantity;
    private LocalDateTime order_dateTime;
    private LocalDate order_date;
    private LocalTime order_time;
    private String status;
    private String use_phone_number;
    private int queue_number;
    public Order() {
    }
    public Order(int order_id, int order_total_quantity, double order_price, String status, String use_phone_number) {
        this.order_id = order_id;
        this.order_total_quantity = order_total_quantity;
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

    public int getOrder_total_quantity() {
        return order_total_quantity;
    }

    public void setOrder_total_quantity(int order_total_quantity) {
        this.order_total_quantity = order_total_quantity;
    }

    public int getQueue_number() {
        return queue_number;
    }

    public void setQueue_number(int queue_number) {
        this.queue_number = queue_number;
    }

    public LocalDateTime getOrder_dateTime() {
        return order_dateTime;
    }

    public void setOrder_dateTime(LocalDateTime order_dateTime) {
        this.order_dateTime = order_dateTime;
        setOrder_date(order_dateTime.toLocalDate());
        setOrder_time(order_dateTime.toLocalTime());
    }

    public LocalDate getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDate order_date) {
        this.order_date = order_date;
    }

    public LocalTime getOrder_time() {
        return order_time;
    }

    public void setOrder_time(LocalTime order_time) {
        this.order_time = order_time;
    }
}
