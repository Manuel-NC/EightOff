package com.example.eightoff;

import com.example.eightoff.Controlador.EightOffControlador;
import com.example.eightoff.Modelo.EightOffModelo;
import com.example.eightoff.Vista.EightOffGUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class EightOffGame extends Application {

    @Override
    public void start(Stage primaryStage) {
        EightOffModelo modelo = new EightOffModelo();
        EightOffGUI vista = new EightOffGUI();
        EightOffControlador controlador = new EightOffControlador(modelo, vista);

        vista.construirInterfaz(primaryStage);
        controlador.inicializar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
