package Main.model;

public class Item {
    private String colJugador;
    private String colPuntuacion;

    public Item(String colJugador, String colPuntuacion) {
        this.colJugador = colJugador;
        this.colPuntuacion = colPuntuacion;
    }

    public String getColJugador() {
        return colJugador;
    }

    public String getColPuntuacion() {
        return colPuntuacion;
    }
}
