package com.example.eightoff.Vista;

import com.example.eightoff.Modelo.CartaInglesa;
import com.example.eightoff.Modelo.EightOffModelo;
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

public class EightOffGUI {

    private Button btnDeshacer;
    private Button btnPista;
    private Button btnNuevoJuego;

    private StackPane[] casillasReserva;
    private StackPane[] casillasFundacion;
    private Group[] columnasGrupo;

    public EightOffGUI() {
        casillasReserva = new StackPane[8];
        casillasFundacion = new StackPane[4];
        columnasGrupo = new Group[8];
    }

    public void construirInterfaz(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #2b5d34;");

        HBox barraAcciones = new HBox(15);
        barraAcciones.setAlignment(Pos.CENTER);
        barraAcciones.setPadding(new Insets(0, 0, 15, 0));

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
        root.setTop(barraAcciones);

        VBox areaJuego = new VBox(20);
        areaJuego.setAlignment(Pos.TOP_CENTER);

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
            StackPane fondoColumna = crearCasillaVacia();
            columnasGrupo[col].getChildren().add(fondoColumna);
            tableroColumnas.getChildren().add(columnasGrupo[col]);
        }

        areaJuego.getChildren().addAll(zonaSuperior, tableroColumnas);
        root.setCenter(areaJuego);

        Scene scene = new Scene(root, 920, 680);
        stage.setTitle("Eight Off Solitaire");
        stage.setScene(scene);
        stage.show();
    }

    public void actualizarTablero(EightOffModelo modelo, int tipoSeleccion, int idxSeleccion) {
        for (int i = 0; i < 8; i++) {
            casillasReserva[i].getChildren().clear();
            ArrayList<CartaInglesa> cartas = modelo.getReserva(i);
            if (!cartas.isEmpty()) {
                boolean seleccionada = (tipoSeleccion == modelo.tipoReserva && idxSeleccion == i);
                casillasReserva[i].getChildren().add(crearTarjetaCarta(cartas.get(0), seleccionada));
            }
        }

        for (int i = 0; i < 4; i++) {
            casillasFundacion[i].getChildren().clear();
            ArrayList<CartaInglesa> cartas = modelo.getFundacion(i);
            if (!cartas.isEmpty()) {
                CartaInglesa ultima = cartas.get(cartas.size() - 1);
                casillasFundacion[i].getChildren().add(crearTarjetaCarta(ultima, false));
            }
        }

        for (int col = 0; col < 8; col++) {
            columnasGrupo[col].getChildren().clear();

            StackPane fondo = crearCasillaVacia();
            columnasGrupo[col].getChildren().add(fondo);

            ArrayList<CartaInglesa> cartas = modelo.getColumna(col);
            for (int cardIdx = 0; cardIdx < cartas.size(); cardIdx++) {
                CartaInglesa c = cartas.get(cardIdx);
                boolean esUltima = (cardIdx == cartas.size() - 1);
                boolean seleccionada = esUltima && (tipoSeleccion == modelo.tipoColumna && idxSeleccion == col);

                VBox tarjetaVisual = crearTarjetaCarta(c, seleccionada);
                tarjetaVisual.setTranslateY(cardIdx * 32);
                columnasGrupo[col].getChildren().add(tarjetaVisual);
            }
        }
    }

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

    private VBox crearTarjetaCarta(CartaInglesa carta, boolean seleccionada) {
        VBox tarjeta = new VBox();
        tarjeta.setPrefSize(50, 75);
        tarjeta.setMinSize(50, 75);
        tarjeta.setMaxSize(50, 75);
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setPadding(new Insets(4, 0, 0, 0));

        String borderStyle = seleccionada ? "-fx-border-color: #ffcc00; -fx-border-width: 2.5px;"
                : "-fx-border-color: #222222; -fx-border-width: 1px;";

        tarjeta.setStyle(
                "-fx-background-color: white; " + borderStyle +
                        "-fx-border-radius: 5px; -fx-background-radius: 5px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 0, 1);"
        );

        String valorTexto = String.valueOf(carta.getValor());
        if (carta.getValor() == 11) valorTexto = "J";
        else if (carta.getValor() == 12) valorTexto = "Q";
        else if (carta.getValor() == 13) valorTexto = "K";
        else if (carta.getValor() == 14 || carta.getValor() == 1) valorTexto = "A";

        String colorTexto = carta.getColor().equalsIgnoreCase("rojo") ? "#cc0000" : "#000000";

        HBox contenido = new HBox(2);
        contenido.setAlignment(Pos.CENTER);

        Label lblValor = new Label(valorTexto);
        lblValor.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        lblValor.setStyle("-fx-text-fill: " + colorTexto + ";");

        Label lblPalo = new Label(carta.getPalo().getFigura());
        lblPalo.setFont(Font.font("Segoe UI Symbol", 14));
        lblPalo.setStyle("-fx-text-fill: " + colorTexto + ";");

        contenido.getChildren().addAll(lblValor, lblPalo);
        tarjeta.getChildren().add(contenido);

        return tarjeta;
    }

    public Button getBtnDeshacer() { return btnDeshacer; }
    public Button getBtnPista() { return btnPista; }
    public Button getBtnNuevoJuego() { return btnNuevoJuego; }
    public StackPane[] getCasillasReserva() { return casillasReserva; }
    public StackPane[] getCasillasFundacion() { return casillasFundacion; }
    public Group[] getColumnasGrupo() { return columnasGrupo; }
}
