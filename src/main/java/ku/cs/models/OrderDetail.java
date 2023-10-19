package ku.cs.models;

public class OrderDetail {

    private int orderDetail_id;
    private int order_id;
    private int menu_id;
    private int topping_id;
    private int quantity;
    private double total_price;

    public OrderDetail(int orderDetail_id, int order_id, int menu_id, int topping_id, int quantity,double total_price) {
        this.orderDetail_id = orderDetail_id;
        this.order_id = order_id;
        this.menu_id = menu_id;
        this.topping_id = topping_id;
        this.quantity = quantity;
        this.total_price = total_price;
    }

    public int getOrderDetail_id() {
        return orderDetail_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public int getTopping_id() {
        return topping_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal_price() {
        return total_price;
    }
}