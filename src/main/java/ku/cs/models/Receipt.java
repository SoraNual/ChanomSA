package ku.cs.models;

import java.time.LocalDateTime;

public class Receipt {
    private int receipt_id;
    private int order_id;
    private int queue_num;
    private int total_quantity;
    private double net_price;
    private LocalDateTime receipt_dateTime;

    public Receipt(int receipt_id, int order_id, int queue_num, double net_price) {
        this.receipt_id = receipt_id;
        this.order_id = order_id;
        this.queue_num = queue_num;
        this.net_price = net_price;
    }

    public int getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(int receipt_id) {
        this.receipt_id = receipt_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getQueue_num() {
        return queue_num;
    }

    public void setQueue_num(int queue_num) {
        this.queue_num = queue_num;
    }

    public LocalDateTime getReceipt_dateTime() {
        return receipt_dateTime;
    }

    public void setReceipt_dateTime(LocalDateTime receipt_dateTime) {
        this.receipt_dateTime = receipt_dateTime;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

    public double getNet_price() {
        return net_price;
    }

    public void setNet_price(double net_price) {
        this.net_price = net_price;
    }
}
