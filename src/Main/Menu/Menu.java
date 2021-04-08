package Main.Menu;

import Main.Juego.PuigInvaders;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Menu implements Initializable {

    @FXML private Button btnJugar, btnRanking, btnInstrucciones, btnSalir;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void jugar(ActionEvent actionEvent) {
        switchWindow((Stage)btnJugar.getScene().getWindow(), new PuigInvaders());
    }

    //Hace posible el cambio de ventanas
    public static void switchWindow(Stage window, Application app) {
        try {
            app.start(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ranking(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Main/Ranking/ranking.fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("/Main/resources/icon.png"));
        stage.setTitle("Ranking - Puig Invaders FX");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void instrucciones(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Main/Instrucciones/instrucciones.fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("/Main/resources/icon.png"));
        stage.setTitle("Instrucciones - Puig Invaders FX");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void salir(ActionEvent actionEvent) {
        Platform.exit();
    }
}
