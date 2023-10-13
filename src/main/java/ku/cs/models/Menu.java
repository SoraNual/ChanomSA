package ku.cs.models;

public class Menu {

    private int menu_id;
    private String menu_type;
    private String menu_name;
    private double menu_price;

    public Menu() {
    }

    public Menu(int id, String type, String name, double price) {
        this.menu_id = id;
        this.menu_type = type;
        this.menu_name = name;
        this.menu_price = price;
    }

    public int getId() {
        return menu_id;
    }


    public String getName() {
        return menu_name;
    }

    public void setName(String name) {
        this.menu_name = name;
    }

    public String getType() {
        return menu_type;
    }

    public void setType(String type) {
        this.menu_type = type;
    }

    public double getMenuPrice() {
        return menu_price;
    }

    public void setMenuPrice(double price) {
        this.menu_price = price;
    }

}
