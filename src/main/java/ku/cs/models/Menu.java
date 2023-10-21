package ku.cs.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.util.ProjectUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Menu {

    private int menu_id;
    private String menu_type;
    private String menu_name;
    private double menu_price;

    public Menu() {
    }
    public Menu(int id) {
        this.menu_id = id;
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

    public String getPicturePath(){
        // set default picture -- Khaokrapow
        String picturePath =   "data/menus-pictures/" + getMenu_id() + ".png";
        if (new File(picturePath).exists()) return picturePath;
            //        User user = new User();
            //        user.load(getComplainantName());
            //        if (user.isPictureSet()) return user.getPicturePath();
        return "data/menus-pictures/default.png";
    }
    public String getEditPicturePath(){
        // set default picture -- Khaokrapow
        String picturePath =   "data/menus-pictures/" + getMenu_id() + ".png";
        //        User user = new User();
        //        user.load(getComplainantName());
        //        if (user.isPictureSet()) return user.getPicturePath();
        return "data/menus-pictures/default.png";
    }
    public ImageView getImageView(){
        ImageView imageView = new ImageView(getPicture());
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        return imageView;
    }

    public Image getPicture(){
        File file = new File(getPicturePath());
        return new Image(String.valueOf(file.toURI()));
    }

    public void setPicture(File file){
        //if (file.getName().equals("default.png")) return;
        ProjectUtility.copyFile(file.getPath(), "data/menus-pictures/"+getMenu_id()+".png");
    }

}
