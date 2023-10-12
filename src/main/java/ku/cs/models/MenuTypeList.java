package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class MenuTypeList {

    private List<MenuType> menuTypes;

    public MenuTypeList() {
        menuTypes = new ArrayList<>();
    }

    public void addMenuType(MenuType menuType) {
        menuTypes.add(menuType);
    }

    public List<MenuType> getMenuTypes() {
        return menuTypes;
    }

}
