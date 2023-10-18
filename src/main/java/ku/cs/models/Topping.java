package ku.cs.models;

public class Topping {

    private int topping_id;
    private String topping_name;
    private double topping_price;

    public Topping( String topping_name, double topping_price) {
        this.topping_name = topping_name;
        this.topping_price = topping_price;
    }

    public int getTopping_id() {
        return topping_id;
    }

    public String getTopping_name() {
        return topping_name;
    }

    public double getTopping_price() {
        return topping_price;
    }
}

