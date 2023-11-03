package ku.cs.models;

public class SalesRecord {
    private int rank;
    private int menu_id;
    private int total_quantity;
    private String menu_name;

    public SalesRecord(int rank, int menu_id, int total_quantity, String menu_name) {
        this.menu_id = menu_id;
        this.total_quantity = total_quantity;
        this.menu_name = menu_name;
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }
}
