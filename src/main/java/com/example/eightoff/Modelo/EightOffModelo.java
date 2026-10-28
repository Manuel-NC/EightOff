package com.example.eightoff.Modelo;

import java.util.ArrayList;

// Modelo principal que coordina el estado del juego Eight Off
public class EightOffModelo {

    public final int tipoColumna = 0;
    public final int tipoReserva = 1;
    public final int tipoFundacion = 2;

    private TableauDeck[] columnas;
    private ReservaDeck[] reservas;
    private FoundationDeck[] fundaciones;

    private HistorialMovimientos historial;

    public EightOffModelo() {
        columnas = new TableauDeck[8];
        reservas = new ReservaDeck[8];
        fundaciones = new FoundationDeck[4];
        historial = new HistorialMovimientos();
        iniciarPartida();
    }

    // Prepara el juego: limpia contenedores, crea mazo nuevo, reparte 6 cartas a las 8 columnas y 1 a las primeras 4 reservas
    public void iniciarPartida() {
        for (int i = 0; i < 8; i++) {
            columnas[i] = new TableauDeck();
            reservas[i] = new ReservaDeck();
        }

        for (int i = 0; i < 4; i++) {
            fundaciones[i] = new FoundationDeck();
        }

        historial.limpiar();
        Mazo mazo = new Mazo();

        for (int col = 0; col < 8; col++) {
            for (int i = 0; i < 6; i++) {
                columnas[col].insertarDirecto(mazo.obtenerUnaCarta());
            }
        }

        for (int res = 0; res < 4; res++) {
            reservas[res].agregarCarta(mazo.obtenerUnaCarta());
        }
    }

    // Revisa si a partir de cierto índice en una columna hay un bloque con orden legal
    public boolean esSecuenciaValida(int idxColumna, int cardIdx) {
        if (idxColumna < 0 || idxColumna >= 8) return false;
        return columnas[idxColumna].esSecuenciaValidaDesde(cardIdx);
    }

    // Obtiene las cartas que se quieren intentar mover desde un contenedor de origen
    public ArrayList<CartaInglesa> obtenerBloqueOrigen(int tipoOrigen, int idxOrigen, int cardIdx) {
        if (tipoOrigen == tipoColumna) {
            return columnas[idxOrigen].obtenerSecuenciaDesde(cardIdx);
        } else if (tipoOrigen == tipoReserva) {
            CartaInglesa c = reservas[idxOrigen].getCarta();
            if (c != null) {
                ArrayList<CartaInglesa> b = new ArrayList<>();
                b.add(c);
                return b;
            }
        }
        return null;
    }

    // Ejecuta el movimiento de una carta o un bloque de cartas entre contenedores si es un movimiento legal
    public boolean moverCarta(int tipoOrigen, int idxOrigen, int cardIdxOrigen, int tipoDestino, int idxDestino) {
        ArrayList<CartaInglesa> bloque = obtenerBloqueOrigen(tipoOrigen, idxOrigen, cardIdxOrigen);
        if (bloque == null || bloque.isEmpty()) {
            return false;
        }

        CartaInglesa primeraCarta = bloque.get(0);
        boolean movimientoValido = false;

        if (tipoDestino == tipoFundacion) {
            if (bloque.size() == 1) {
                movimientoValido = esValidoMovimientoAFundacion(primeraCarta, idxDestino);
            }
        } else if (tipoDestino == tipoReserva) {
            if (bloque.size() == 1) {
                movimientoValido = esValidoMovimientoAReserva(idxDestino);
            }
        } else if (tipoDestino == tipoColumna) {
            movimientoValido = esValidoMovimientoAColumna(primeraCarta, idxDestino);
        }

        if (movimientoValido) {
            // Remueve las cartas del origen
            if (tipoOrigen == tipoColumna) {
                columnas[idxOrigen].removerSecuenciaDesde(cardIdxOrigen);
            } else if (tipoOrigen == tipoReserva) {
                reservas[idxOrigen].removerCarta();
            }

            // Agrega las cartas al destino
            if (tipoDestino == tipoColumna) {
                columnas[idxDestino].agregarBloque(bloque);
            } else if (tipoDestino == tipoReserva) {
                reservas[idxDestino].agregarCarta(bloque.get(0));
            } else if (tipoDestino == tipoFundacion) {
                fundaciones[idxDestino].agregarCarta(bloque.get(0));
            }

            historial.guardarMovimiento(tipoOrigen, idxOrigen, tipoDestino, idxDestino, bloque);
            return true;
        }

        return false;
    }

    // Revisa reglas para depositar en fundación
    public boolean esValidoMovimientoAFundacion(CartaInglesa carta, int idxFundacion) {
        CartaInglesa ultima = fundaciones[idxFundacion].getUltimaCarta();
        if (ultima == null) {
            return carta.getValorBajo() == 1; // Debe ser un As
        }
        return carta.tieneElMismoPalo(ultima.getPalo()) && (carta.getValorBajo() == ultima.getValorBajo() + 1);
    }

    // Revisa reglas para mandar a una casilla de reserva
    public boolean esValidoMovimientoAReserva(int idxReserva) {
        return reservas[idxReserva].estaVacio();
    }

    // Revisa reglas para mandar a una columna del tablero
    public boolean esValidoMovimientoAColumna(CartaInglesa carta, int idxColumna) {
        return columnas[idxColumna].sePuedeAgregarCarta(carta);
    }

    // Revierte la última jugada guardada en el historial
    public boolean deshacer() {
        if (historial.estaVacio()) {
            return false;
        }

        RegistroMovimiento ultimo = historial.obtenerUltimoMovimiento();
        ArrayList<CartaInglesa> bloque = ultimo.getCartas();

        // Extrae el bloque del lugar destino
        if (ultimo.getTipoDestino() == tipoColumna) {
            int startIdx = columnas[ultimo.getIdxDestino()].getCartasComoLista().size() - bloque.size();
            columnas[ultimo.getIdxDestino()].removerSecuenciaDesde(startIdx);
        } else if (ultimo.getTipoDestino() == tipoReserva) {
            reservas[ultimo.getIdxDestino()].removerCarta();
        } else if (ultimo.getTipoDestino() == tipoFundacion) {
            fundaciones[ultimo.getIdxDestino()].removerUltimaCarta();
        }

        // Regresa el bloque a su lugar de origen
        if (ultimo.getTipoOrigen() == tipoColumna) {
            columnas[ultimo.getIdxOrigen()].agregarBloque(bloque);
        } else if (ultimo.getTipoOrigen() == tipoReserva) {
            reservas[ultimo.getIdxOrigen()].agregarCarta(bloque.get(0));
        }

        return true;
    }

    // Obtiene la carta superior de un contenedor según tipo e índice
    public CartaInglesa obtenerCartaSuperior(int tipo, int indice) {
        if (tipo == tipoColumna) return columnas[indice].getUltimaCarta();
        if (tipo == tipoReserva) return reservas[indice].getCarta();
        if (tipo == tipoFundacion) return fundaciones[indice].getUltimaCarta();
        return null;
    }

    // Comprueba si las 52 cartas ya están acomodadas en las fundaciones
    public boolean verificarVictoria() {
        int total = 0;
        for (int i = 0; i < 4; i++) {
            total += fundaciones[i].getCartasComoLista().size();
        }
        return total == 52;
    }

    // Revisa si se puede hacer un deshacer
    public boolean puedeDeshacer() {
        return !historial.estaVacio();
    }

    // Métodos para consultar listas de cartas actualizadas
    public ArrayList<CartaInglesa> getColumna(int index) {
        return columnas[index].getCartasComoLista();
    }

    public ArrayList<CartaInglesa> getReserva(int index) {
        ArrayList<CartaInglesa> res = new ArrayList<>();
        CartaInglesa c = reservas[index].getCarta();
        if (c != null) res.add(c);
        return res;
    }

    public ArrayList<CartaInglesa> getFundacion(int index) {
        return fundaciones[index].getCartasComoLista();
    }
}