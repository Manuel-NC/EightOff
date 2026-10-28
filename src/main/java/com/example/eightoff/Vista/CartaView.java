package com.example.eightoff.Vista;

import com.example.eightoff.Modelo.CartaInglesa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CartaView extends VBox {

    public CartaView(CartaInglesa carta, boolean seleccionada, boolean destacadaPista) {
        setPrefSize(50, 75);
        setMinSize(50, 75);
        setMaxSize(50, 75);
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(4, 0, 0, 0));

        String borderAndBackground;
        if (destacadaPista) {
            borderAndBackground = "-fx-background-color: #90caf9; -fx-border-color: #1565c0; -fx-border-width: 2.5px;";
        } else if (seleccionada) {
            borderAndBackground = "-fx-background-color: #fff9c4; -fx-border-color: #ffcc00; -fx-border-width: 2.5px;";
        } else {
            borderAndBackground = "-fx-background-color: #ffffff; -fx-border-color: #222222; -fx-border-width: 1px;";
        }

        setStyle(
                borderAndBackground +
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

        String figuraLimpia = carta.getPalo().getFigura().replace("\uFE0F", "").replace("\uFE0E", "");

        Label lblPalo = new Label(figuraLimpia);
        lblPalo.setFont(Font.font("Segoe UI Symbol", 14));
        lblPalo.setStyle("-fx-text-fill: " + colorTexto + ";");

        contenido.getChildren().addAll(lblValor, lblPalo);
        getChildren().add(contenido);
    }
}
