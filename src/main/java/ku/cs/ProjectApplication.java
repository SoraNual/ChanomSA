package ku.cs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.github.saacsos.FXRouter;
import java.io.IOException;

public class ProjectApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        FXRouter.bind(this, stage, "Drink Store Management", 1280, 720);
        configRoute();
        FXRouter.goTo("main");
    }
    private static void configRoute() {
        String packageStr = "ku/cs/";

        FXRouter.when("main", packageStr+"main-page.fxml");

        FXRouter.when("menu", packageStr+"menu-page.fxml");
        FXRouter.when("menu-detail", packageStr+"menu-detail-page.fxml");
        FXRouter.when("add-menu", packageStr+"add-menu-page.fxml");

        FXRouter.when("topping", packageStr+"topping-page.fxml");
        FXRouter.when("topping-detail", packageStr+"topping-detail-page.fxml");
        FXRouter.when("add-topping", packageStr+"add-topping-page.fxml");

        FXRouter.when("create-order", packageStr+"create-order-page.fxml");


    }

    public static void main(String[] args) {
        launch();
    }
}
