package Main.Juego;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class PuigInvaders extends Application {

    private static final Random RANDOM = new Random();
    private static final int ANCHO = 800;
    private static final int LARGO = 600;
    private static final int PL_TAMANO = 60;
    static final Image PL_IMG = new Image("/Main/resources/nave.png");
    static final Image EXP_IMG = new Image("/Main/resources/explosion.png");
    static final int EXP_ANCHO = 128;
    static final int EXP_LARGO = 128;
    static final int EXP_FILAS = 3;
    static final int EXP_COLUMNAS = 3;
    static final int EXP_STEPS = 15;

    static final Image ENEMIGOS_IMG[] = {
            new Image("/Main/resources/enemigo1.png"),
            new Image("/Main/resources/enemigo2.png"),
            new Image("/Main/resources/enemigo3.png"),
            new Image("/Main/resources/enemigo4.png"),
            //DLC
//            new Image("/Main/resources/garriga.png"),
//            new Image("/Main/resources/yisus.png"),
    };

    int MAX_ENEMIGOS = 15;
    final int MAX_DISPAROS = MAX_ENEMIGOS * 3;
    boolean gameOver = false;
    private GraphicsContext graphicsContext;

    Nave nave;
    List<Disparo> disparoList;
    List<Universo> universoList;
    List<Enemigos> enemigosList;

    private double mouseX;
    private int puntuacion;

    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(ANCHO, LARGO);
        graphicsContext = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            try {
                run(graphicsContext);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        canvas.setFocusTraversable(true);

        //Movimiento del cursor
        canvas.setCursor(Cursor.MOVE);
        canvas.setOnMouseMoved(e -> mouseX = e.getX());
        canvas.setOnMouseClicked(e -> {
            if(disparoList.size() < MAX_DISPAROS) disparoList.add(nave.disparo());
        });
        setup();
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.getIcons().add(new Image("/Main/resources/icon.png"));
        stage.setTitle("Puig Invaders FX");
        stage.show();
    }

    //Cargamos el juego
    private void setup() {
        universoList = new ArrayList<>();
        disparoList = new ArrayList<>();
        enemigosList = new ArrayList<>();
        nave = new Nave(ANCHO / 2, LARGO - PL_TAMANO, PL_TAMANO, PL_IMG);
        puntuacion = 0;
        IntStream.range(0, MAX_ENEMIGOS).mapToObj(i -> this.newEnemigo()).forEach(enemigosList::add);
    }

    //Tengo que ver donde lo meto
    private void dialogo() throws IOException {
        if (gameOver = true) {
            Parent root = FXMLLoader.load(getClass().getResource("/Main/Dialogo/dialogo.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.getIcons().add(new Image("/Main/resources/icon.png"));
            stage.setTitle("Puig Invaders FX");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
    }


    private void run(GraphicsContext graphicsContext) throws IOException {
        graphicsContext.setFill(Color.grayRgb(20));
        graphicsContext.fillRect(0, 0, ANCHO, LARGO);
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.setFont(Font.font(20));
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillText("PUNTUACIÓN: <" + puntuacion+">", 100,  20);

        //TODO alert box para guardar la puntuación del jugador y volver al menu principal
        if(gameOver) {
            graphicsContext.setFont(Font.font(35));
            graphicsContext.setFill(Color.YELLOW);
            graphicsContext.fillText("GAME OVER \n El Puig Castellar ha sido invadido \n Tu puntuación ha sido: "+puntuacion, ANCHO / 2, LARGO /2.5);
            return;
        }

        universoList.forEach(Universo::dibujar);

        nave.actualizar();
        nave.dibujar();
        nave.posX = (int) mouseX;

        enemigosList.stream().peek(Nave::actualizar).peek(Nave::dibujar).forEach(e -> {
            if(nave.colision(e) && !nave.explotar) {
                nave.explotado();
            }
        });

        for (int i = disparoList.size() - 1; i >=0 ; i--) {
            Disparo disparo = disparoList.get(i);
            if(disparo.posY < 0 || disparo.eliminar)  {
                disparoList.remove(i);
                continue;
            }

            disparo.actualizar();
            disparo.dibujar();

            for (Enemigos enemigos : this.enemigosList) {
                if(disparo.colision(enemigos) && !enemigos.explotar) {
                    puntuacion++;
                    enemigos.explotado();
                    disparo.eliminar = true;
                }
            }
        }

        for (int i = enemigosList.size() - 1; i >= 0; i--){
            if(enemigosList.get(i).destruido)  {
                enemigosList.set(i, newEnemigo());
            }
        }

        gameOver = nave.destruido;

        if(RANDOM.nextInt(10) > 2) {
            universoList.add(new Universo());
        }

        for (int i = 0; i < universoList.size(); i++) {
            if(universoList.get(i).posY > LARGO)
                universoList.remove(i);
        }
    }

    //Nave del jugador
    public class Nave {

        int posX, posY, tamano;
        boolean explotar, destruido;
        Image img;
        int explosionStep = 0;

        public Nave(int posX, int posY, int tamano, Image image) {
            this.posX = posX;
            this.posY = posY;
            this.tamano = tamano;
            img = image;
        }

        public Disparo disparo() {
            return new Disparo(posX + tamano / 2 - Disparo.tamano / 2, posY - Disparo.tamano);
        }

        public void actualizar() {
            if(explotar) explosionStep++;
            destruido = explosionStep > EXP_STEPS;
        }

        public void dibujar() {
            if(explotar) {
                graphicsContext.drawImage(EXP_IMG, explosionStep % EXP_COLUMNAS * EXP_ANCHO, (explosionStep / EXP_FILAS) * EXP_LARGO + 1,
                        EXP_ANCHO, EXP_LARGO,
                        posX, posY, tamano, tamano);
            }
            else {
                graphicsContext.drawImage(img, posX, posY, tamano, tamano);
            }
        }

        public boolean colision(Nave other) {
            int d = distancia(this.posX + tamano / 2, this.posY + tamano /2,
                    other.posX + other.tamano / 2, other.posY + other.tamano / 2);
            return d < other.tamano / 2 + this.tamano / 2 ;
        }

        public void explotado() {
            explotar = true;
            explosionStep = -1;
        }

    }

    //Enemigos
    public class Enemigos extends Nave {

        // Para modificar la velocidad a medida que avanzamos en la puntuación
        int VELOCIDAD = (puntuacion / 7) + 2;

        public Enemigos(int posX, int posY, int size, Image image) {
            super(posX, posY, size, image);
        }

        public void actualizar() {
            super.actualizar();
            if(!explotar && !destruido) posY += VELOCIDAD;
            if(posY > LARGO) destruido = true;
        }
    }

    //Disparos
    public class Disparo {

        public boolean eliminar;

        int posX, posY, velocidad = 10;
        static final int tamano = 6;

        public Disparo(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }

        public void actualizar() {
            posY-= velocidad;
        }


        public void dibujar() {
            graphicsContext.setFill(Color.DODGERBLUE);
            if (puntuacion >= 100) {
                //Aumentamos la dificultad
                MAX_ENEMIGOS = 25;
                graphicsContext.setFill(Color.RED);
                velocidad = 50;
                graphicsContext.fillRect(posX-5, posY-10, tamano +10, tamano +30);
            } else {
                graphicsContext.fillOval(posX, posY, tamano, tamano);
            }
        }

        public boolean colision(Nave Nave) {
            int distancia = distancia(this.posX + tamano / 2, this.posY + tamano / 2,
                    Nave.posX + Nave.tamano / 2, Nave.posY + Nave.tamano / 2);
            return distancia  < Nave.tamano / 2 + tamano / 2;
        }
    }

    //Universo
    public class Universo {
        int posX, posY;
        private int largo, ancho, r, g, b;
        private double opacidad;

        public Universo() {
            posX = RANDOM.nextInt(ANCHO);
            posY = 0;
            ancho = RANDOM.nextInt(5) + 1;
            largo =  RANDOM.nextInt(5) + 1;
            r = RANDOM.nextInt(100) + 150;
            g = RANDOM.nextInt(100) + 150;
            b = RANDOM.nextInt(100) + 150;
            opacidad = RANDOM.nextFloat();
            if(opacidad < 0) opacidad *=-1;
            if(opacidad > 0.5) opacidad = 0.5;
        }

        public void dibujar() {
            if(opacidad > 0.8) opacidad -=0.01;
            if(opacidad < 0.1) opacidad +=0.01;
            graphicsContext.setFill(Color.rgb(r, g, b, opacidad));
            graphicsContext.fillOval(posX, posY, ancho, largo);
            posY+=20;
        }
    }

    Enemigos newEnemigo() {
        return new Enemigos(50 + RANDOM.nextInt(ANCHO - 100), 0, PL_TAMANO, ENEMIGOS_IMG[RANDOM.nextInt(ENEMIGOS_IMG.length)]);
    }

    int distancia(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}