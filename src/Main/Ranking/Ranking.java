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

        colJugador.setCellValueFactory(new PropertyValueFactory<>("colJugador"));
        colPuntuacion.setCellValueFactory(new PropertyValueFactory<>("colPuntuacion"));

        tablaRanking.getColumns().addAll(colJugador,colPuntuacion);
        tablaRanking.setItems(dataTable);

        //Añadir los datos
        dataTable.add(new Item("Aitor","999"));
        dataTable.add(new Item("Cybertin","107"));
        dataTable.add(new Item("Nanonixx","112"));
        dataTable.add(new Item("Cocotel","111"));
        dataTable.add(new Item("Aaron","56"));
        dataTable.add(new Item("davidihermana","0"));
        dataTable.add(new Item("123","123"));
        dataTable.add(new Item("Heterocurioso","69"));
        dataTable.add(new Item("Hulolo","89"));
        dataTable.add(new Item("SnakeMax","74"));
        dataTable.add(new Item("Joloso","120"));
        dataTable.add(new Item("Tipex69","100"));
        dataTable.add(new Item("NAJI","RIP"));
        dataTable.add(new Item("Cick","64"));
        dataTable.add(new Item("Joy","85"));
        dataTable.add(new Item("Nil","75"));
        dataTable.add(new Item("Toni","50"));
        dataTable.add(new Item("WikiRusben","0"));

    }
}
