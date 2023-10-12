package ku.cs.models;

public class Menu {

    private int menu_id;
    private String menu_name;
    private String type;
    private double menu_Price;

    public Menu() {
    }

    public Menu(int id, String name, String type, double price) {
        this.menu_id = id;
        this.menu_name = name;
        this.type = type;
        this.menu_Price = price;
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
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMenuPrice() {
        return menu_Price;
    }

    public void setMenuPrice(double price) {
        this.menu_Price = price;
    }

}
