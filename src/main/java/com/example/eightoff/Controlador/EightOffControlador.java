package com.example.eightoff.Controlador;

import com.example.eightoff.Modelo.CartaInglesa;
import com.example.eightoff.Modelo.EightOffModelo;
import com.example.eightoff.Vista.EightOffGUI;
import javafx.scene.control.Alert;

public class EightOffControlador {

    private EightOffModelo modelo;
    private EightOffGUI vista;

    private int tipoSeleccionado = -1;
    private int indiceSeleccionado = -1;

    public EightOffControlador(EightOffModelo modelo, EightOffGUI vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void inicializar() {
        vista.getBtnNuevoJuego().setOnAction(e -> gestionarBotonNuevoJuego());
        vista.getBtnDeshacer().setOnAction(e -> gestionarBotonDeshacer());
        vista.getBtnPista().setOnAction(e -> gestionarBotonPista());

        for (int i = 0; i < 8; i++) {
            int index = i;
            vista.getCasillasReserva()[i].setOnMouseClicked(e -> gestionarSeleccionCasilla(modelo.tipoReserva, index));
        }

        for (int i = 0; i < 4; i++) {
            int index = i;
            vista.getCasillasFundacion()[i].setOnMouseClicked(e -> gestionarSeleccionCasilla(modelo.tipoFundacion, index));
        }

        for (int i = 0; i < 8; i++) {
            int index = i;
            vista.getColumnasGrupo()[i].setOnMouseClicked(e -> gestionarSeleccionCasilla(modelo.tipoColumna, index));
        }

        refrescarVista();
    }

    public void gestionarSeleccionCasilla(int tipo, int indice) {
        if (tipoSeleccionado == -1) {
            CartaInglesa carta = modelo.obtenerCartaSuperior(tipo, indice);
            if (carta != null && tipo != modelo.tipoFundacion) {
                tipoSeleccionado = tipo;
                indiceSeleccionado = indice;
            }
        } else {
            if (tipoSeleccionado == tipo && indiceSeleccionado == indice) {
                deseleccionar();
            } else {
                boolean exito = modelo.moverCarta(tipoSeleccionado, indiceSeleccionado, tipo, indice);
                deseleccionar();

                if (exito) {
                    if (modelo.verificarVictoria()) {
                        mostrarAlerta("Felicidades!", "¡Has ganado el juego!");
                    } else if (modelo.verificarFinJuego()) {
                        mostrarAlerta("Fin del juego", "No quedan más movimientos posibles.");
                    }
                }
            }
        }
        refrescarVista();
    }

    public void gestionarBotonDeshacer() {
        deseleccionar();
        if (!modelo.deshacer()) {
            mostrarAlerta("Deshacer", "No hay movimientos previos para revertir.");
        }
        refrescarVista();
    }

    public void gestionarBotonPista() {
        deseleccionar();
        String pista = modelo.darPista();
        mostrarAlerta("Pista de Movimiento", pista);
        refrescarVista();
    }

    public void gestionarBotonNuevoJuego() {
        deseleccionar();
        modelo.iniciarPartida();
        refrescarVista();
    }

    private void deseleccionar() {
        tipoSeleccionado = -1;
        indiceSeleccionado = -1;
    }

    private void refrescarVista() {
        vista.actualizarTablero(modelo, tipoSeleccionado, indiceSeleccionado);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
