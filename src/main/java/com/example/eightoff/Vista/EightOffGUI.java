package com.example.eightoff.Vista;

import com.example.eightoff.Controlador.EightOffControlador;
import com.example.eightoff.Modelo.CartaInglesa;
import com.example.eightoff.Modelo.EightOffModelo;
import com.example.eightoff.Modelo.Pista;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

// Construye la ventana y renderiza visualmente el estado del tablero
public class EightOffGUI {

    private Button btnDeshacer;
    private Button btnPista;
    private Button btnNuevoJuego;
    private Label lblMensajePista;

    private StackPane[] casillasReserva;
    private StackPane[] casillasFundacion;
    private Group[] columnasGrupo;
    private StackPane[] fondosColumna;

    public EightOffGUI() {
        casillasReserva = new StackPane[8];
        casillasFundacion = new StackPane[4];
        columnasGrupo = new Group[8];
        fondosColumna = new StackPane[8];
    }

    // Crea el panel principal, botones superiores, casillas vacías de reserva/fundación y área de juego
    public void construirInterfaz(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #2b5d34;");

        VBox panelSuperior = new VBox(10);
        panelSuperior.setAlignment(Pos.CENTER);

        HBox barraAcciones = new HBox(15);
        barraAcciones.setAlignment(Pos.CENTER);

        String estiloBoton = "-fx-background-color: #ffffff; -fx-text-fill: #000000; " +
                "-fx-font-weight: bold; -fx-cursor: hand; " +
                "-fx-border-radius: 4px; -fx-background-radius: 4px;";

        btnDeshacer = new Button("Deshacer");
        btnDeshacer.setStyle(estiloBoton);

        btnPista = new Button("Pista");
        btnPista.setStyle(estiloBoton);

        btnNuevoJuego = new Button("Nuevo Juego");
        btnNuevoJuego.setStyle(estiloBoton);

        barraAcciones.getChildren().addAll(btnDeshacer, btnPista, btnNuevoJuego);

        lblMensajePista = new Label("");
        lblMensajePista.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblMensajePista.setStyle("-fx-text-fill: #ffffff;");

        panelSuperior.getChildren().addAll(barraAcciones, lblMensajePista);
        root.setTop(panelSuperior);

        VBox areaJuego = new VBox(20);
        areaJuego.setAlignment(Pos.TOP_CENTER);
        areaJuego.setPadding(new Insets(15, 0, 0, 0));

        HBox zonaSuperior = new HBox(20);
        zonaSuperior.setAlignment(Pos.CENTER);

        HBox reservasListas = new HBox(5);
        reservasListas.setAlignment(Pos.CENTER);
        for (int i = 0; i < 8; i++) {
            casillasReserva[i] = crearCasillaVacia();
            reservasListas.getChildren().add(casillasReserva[i]);
        }

        HBox fundacionesListas = new HBox(5);
        fundacionesListas.setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            casillasFundacion[i] = crearCasillaVacia();
            fundacionesListas.getChildren().add(casillasFundacion[i]);
        }

        zonaSuperior.getChildren().addAll(reservasListas, fundacionesListas);

        HBox tableroColumnas = new HBox(12);
        tableroColumnas.setAlignment(Pos.TOP_CENTER);

        for (int col = 0; col < 8; col++) {
            columnasGrupo[col] = new Group();
            fondosColumna[col] = crearCasillaVacia();
            columnasGrupo[col].getChildren().add(fondosColumna[col]);
            tableroColumnas.getChildren().add(columnasGrupo[col]);
        }

        areaJuego.getChildren().addAll(zonaSuperior, tableroColumnas);
        root.setCenter(areaJuego);

        Scene scene = new Scene(root, 940, 700);
        stage.setTitle("Eight Off");
        stage.setScene(scene);
        stage.show();
    }

    // Redibuja las cartas en sus contenedores actualizados y asigna los eventos de clic
    public void actualizarTablero(EightOffModelo modelo, int tipoSeleccion, int idxSeleccion, int cardIdxSeleccion, Pista.PistaResultado pista, EightOffControlador controlador) {

        btnDeshacer.setDisable(!modelo.puedeDeshacer()); // Deshabilita el botón Undo si no hay movimientos

        // Renderizar reservas
        for (int i = 0; i < 8; i++) {
            int index = i;
            casillasReserva[i].getChildren().clear();
            casillasReserva[i].setOnMouseClicked(e -> controlador.gestionarSeleccionCasilla(modelo.tipoReserva, index, 0));
            ArrayList<CartaInglesa> cartas = modelo.getReserva(i);
            if (!cartas.isEmpty()) {
                boolean seleccionada = (tipoSeleccion == modelo.tipoReserva && idxSeleccion == i);
                boolean esMovible = (pista != null && pista.esReservaMovible(i));
                casillasReserva[i].getChildren().add(new CartaView(cartas.get(0), seleccionada, esMovible));
            }
        }

        // Renderizar fundaciones
        for (int i = 0; i < 4; i++) {
            int index = i;
            casillasFundacion[i].getChildren().clear();
            casillasFundacion[i].setOnMouseClicked(e -> controlador.gestionarSeleccionCasilla(modelo.tipoFundacion, index, 0));
            ArrayList<CartaInglesa> cartas = modelo.getFundacion(i);
            if (!cartas.isEmpty()) {
                CartaInglesa ultima = cartas.get(cartas.size() - 1);
                casillasFundacion[i].getChildren().add(new CartaView(ultima, false, false));
            }
        }

        // Renderizar columnas de cartas apiladas en cascada
        for (int col = 0; col < 8; col++) {
            int colIdx = col;
            columnasGrupo[col].getChildren().clear();

            fondosColumna[col] = crearCasillaVacia();
            fondosColumna[col].setOnMouseClicked(e -> controlador.gestionarSeleccionCasilla(modelo.tipoColumna, colIdx, -1));
            columnasGrupo[col].getChildren().add(fondosColumna[col]);

            ArrayList<CartaInglesa> cartas = modelo.getColumna(col);
            for (int cardIdx = 0; cardIdx < cartas.size(); cardIdx++) {
                int currentCardIdx = cardIdx;
                CartaInglesa c = cartas.get(cardIdx);

                boolean seleccionada = (tipoSeleccion == modelo.tipoColumna && idxSeleccion == col && cardIdx >= cardIdxSeleccion);
                boolean esMovible = (pista != null && pista.esCartaColumnaMovible(col, cardIdx));

                CartaView tarjetaVisual = new CartaView(c, seleccionada, esMovible);
                tarjetaVisual.setTranslateY(cardIdx * 32); // Desplazamiento vertical en cascada
                tarjetaVisual.setOnMouseClicked(e -> {
                    e.consume();
                    controlador.gestionarSeleccionCasilla(modelo.tipoColumna, colIdx, currentCardIdx);
                });

                columnasGrupo[col].getChildren().add(tarjetaVisual);
            }
        }
    }

    // Genera el recuadro gris transparente para las zonas sin cartas
    private StackPane crearCasillaVacia() {
        StackPane casilla = new StackPane();
        casilla.setPrefSize(50, 75);
        casilla.setMinSize(50, 75);
        casilla.setMaxSize(50, 75);
        casilla.setStyle(
                "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-color: rgba(0, 0, 0, 0.15); " +
                        "-fx-background-radius: 5px;"
        );
        return casilla;
    }

    // Actualiza la barra superior de texto
    public void setMensajePista(String texto) {
        lblMensajePista.setText(texto);
    }

    // Getters de los botones principales
    public Button getBtnDeshacer() { return btnDeshacer; }
    public Button getBtnPista() { return btnPista; }
    public Button getBtnNuevoJuego() { return btnNuevoJuego; }
}