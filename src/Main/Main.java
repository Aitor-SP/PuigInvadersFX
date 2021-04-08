package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Menu/menu.fxml")));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.getIcons().add(new Image("/Main/resources/icon.png"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Puig Invaders FX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
