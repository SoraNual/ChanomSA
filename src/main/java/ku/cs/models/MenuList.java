package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class MenuList {


    private List<Menu> menu;

    public MenuList() {
        menu = new ArrayList<>();
    }

    //public void addMenu(Menu menu) {
      //  menu.add(menu);
    //}

    public List<Menu> getMenus() {
        return menu;
    }
}
