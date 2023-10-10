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

    }

    public static void main(String[] args) {
        launch();
    }
}
