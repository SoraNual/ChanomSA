package ku.cs.models;

public class OrderAllDetail {

    private int orderDetail_id;
    private int order_id;
    private int menu_id;
    private String  menu_name;
    private int topping_id;
    private String  topping_name;
    private int quantity;
    private double total_price;

    public OrderAllDetail(int orderDetail_id, int order_id, int menu_id, String menu_name, int topping_id, String topping_name, int quantity, double total_price) {
        this.orderDetail_id = orderDetail_id;
        this.order_id = order_id;
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.topping_id = topping_id;
        this.topping_name = topping_name;
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

    public String getMenu_name() {
        return menu_name;
    }

    public int getTopping_id() {
        return topping_id;
    }

    public String getTopping_name() {
        return topping_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setOrderDetail_id(int orderDetail_id) {
        this.orderDetail_id = orderDetail_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setTopping_id(int topping_id) {
        this.topping_id = topping_id;
    }

    public void setTopping_name(String topping_name) {
        this.topping_name = topping_name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}