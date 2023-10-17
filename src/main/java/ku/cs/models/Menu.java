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

    public int getMenu_id() {
        return menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }


    public String getMenu_type() {
        return menu_type;
    }

    public void setType(String type) {
        this.menu_type = type;
    }

    public double getMenu_price() {
        return menu_price;
    }

}
