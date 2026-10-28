package com.example.eightoff.Controlador;

import com.example.eightoff.Modelo.EightOffModelo;
import com.example.eightoff.Modelo.Pista;
import com.example.eightoff.Vista.EightOffGUI;
import javafx.scene.control.Alert;

// Controlador encargados de coordinar los eventos de usuario entre Modelo y Vista
public class EightOffControlador {

    private EightOffModelo modelo;
    private EightOffGUI vista;

    private int tipoSeleccionado = -1;
    private int indiceSeleccionado = -1;
    private int cardIdxSeleccionado = -1;
    private Pista.PistaResultado pistaActual = null;

    public EightOffControlador(EightOffModelo modelo, EightOffGUI vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    // Registra las acciones de los botones superiores
    public void inicializar() {
        vista.getBtnNuevoJuego().setOnAction(e -> gestionarBotonNuevoJuego());
        vista.getBtnDeshacer().setOnAction(e -> gestionarBotonDeshacer());
        vista.getBtnPista().setOnAction(e -> gestionarBotonPista());

        refrescarVista();
    }

    // Maneja la interacción de clic en cualquier carta/casilla del tablero (origen y destino)
    public void gestionarSeleccionCasilla(int tipo, int indice, int cardIdx) {
        pistaActual = null; // Limpia iluminaciones de pista previa
        vista.setMensajePista("");

        // Primer clic: seleccionar origen
        if (tipoSeleccionado == -1) {
            if (tipo == modelo.tipoColumna && cardIdx == -1) {
                return;
            }

            if (tipo == modelo.tipoColumna) {
                if (modelo.esSecuenciaValida(indice, cardIdx)) {
                    tipoSeleccionado = tipo;
                    indiceSeleccionado = indice;
                    cardIdxSeleccionado = cardIdx;
                }
            } else if (tipo == modelo.tipoReserva) {
                if (modelo.obtenerCartaSuperior(tipo, indice) != null) {
                    tipoSeleccionado = tipo;
                    indiceSeleccionado = indice;
                    cardIdxSeleccionado = 0;
                }
            }
        } else {
            // Segundo clic: cancelar selección si se hace sobre la misma carta
            if (tipoSeleccionado == tipo && indiceSeleccionado == indice && cardIdxSeleccionado == cardIdx) {
                deseleccionar();
            } else {
                // Ejecutar intento de movimiento al destino elegido
                boolean exito = modelo.moverCarta(tipoSeleccionado, indiceSeleccionado, cardIdxSeleccionado, tipo, indice);
                deseleccionar();

                if (exito) {
                    if (modelo.verificarVictoria()) {
                        mostrarAlerta("GG", "Ganaste!");
                    }
                }
            }
        }
        refrescarVista();
    }

    // Acción del botón Deshacer
    public void gestionarBotonDeshacer() {
        deseleccionar();
        pistaActual = null;
        vista.setMensajePista("");
        modelo.deshacer();
        refrescarVista();
    }

    // Acción del botón Pista
    public void gestionarBotonPista() {
        deseleccionar();
        pistaActual = Pista.calcularPista(modelo);
        vista.setMensajePista(pistaActual.getMensaje());
        refrescarVista();
    }

    // Acción del botón Nuevo Juego
    public void gestionarBotonNuevoJuego() {
        deseleccionar();
        pistaActual = null;
        vista.setMensajePista("¡Nuevo juego iniciado!");
        modelo.iniciarPartida();
        refrescarVista();
    }

    // Restablece las variables de selección activa
    private void deseleccionar() {
        tipoSeleccionado = -1;
        indiceSeleccionado = -1;
        cardIdxSeleccionado = -1;
    }

    // Llama al método de repintado de la GUI
    private void refrescarVista() {
        vista.actualizarTablero(modelo, tipoSeleccionado, indiceSeleccionado, cardIdxSeleccionado, pistaActual, this);
    }

    // Despliega ventanas de alerta informativas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
