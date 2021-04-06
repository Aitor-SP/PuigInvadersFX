package Main.Ranking;

import Main.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Ranking implements Initializable {

    @FXML
    TableView<Item> tablaRanking;

    private ObservableList<Item> dataTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataTable = FXCollections.observableArrayList();
        setTablaRanking();
    }

    private void setTablaRanking() {
        TableColumn<Item, String> colJugador = new TableColumn<>("JUGADOR");
        TableColumn<Item, String> colPuntuacion = new TableColumn<>("PUNTUACIÓN");

        colJugador.setCellValueFactory(new PropertyValueFactory<Item,String>("colJugador"));
        colPuntuacion.setCellValueFactory(new PropertyValueFactory<Item,String>("colPuntuacion"));

        tablaRanking.getColumns().addAll(colJugador,colPuntuacion);
        tablaRanking.setItems(dataTable);

        //Añadir los datos
        dataTable.add(new Item("Aitor","999"));

    }
}
