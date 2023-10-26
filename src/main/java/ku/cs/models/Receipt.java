package ku.cs.models;

import java.sql.Timestamp;

public class Receipt {
    private int receipt_id;
    private int order_id;
    private int queue_num;
    private Timestamp receipt_date;

    public Receipt(int receipt_id, int order_id, int queue_num) {
        this.receipt_id = receipt_id;
        this.order_id = order_id;
        this.queue_num = queue_num;
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

    public Timestamp getReceipt_date() {
        return receipt_date;
    }

    public void setReceipt_date(Timestamp receipt_date) {
        this.receipt_date = receipt_date;
    }
}
