package com.example.eightoff.Modelo;

import java.util.ArrayList;

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

    public void iniciarPartida() {
        for (int i = 0; i < 8; i++) {
            columnas[i] = new TableauDeck();
            reservas[i] = new ReservaDeck();
        }

        Palo[] palos = Palo.values();
        for (int i = 0; i < 4; i++) {
            fundaciones[i] = new FoundationDeck(palos[i]);
        }

        historial.limpiar();
        Mazo mazo = new Mazo();

        // Repartir 6 cartas a cada una de las 8 columnas (48 cartas)
        for (int col = 0; col < 8; col++) {
            for (int i = 0; i < 6; i++) {
                columnas[col].insertarDirecto(mazo.obtenerUnaCarta());
            }
        }

        // Repartir las 4 cartas restantes a las primeras 4 reservas
        for (int res = 0; res < 4; res++) {
            reservas[res].agregarCarta(mazo.obtenerUnaCarta());
        }
    }

    public boolean moverCarta(int tipoOrigen, int idxOrigen, int tipoDestino, int idxDestino) {
        CartaInglesa cartaOrigen = obtenerCartaSuperior(tipoOrigen, idxOrigen);
        if (cartaOrigen == null) {
            return false;
        }

        boolean movimientoValido = false;

        if (tipoDestino == tipoFundacion) {
            movimientoValido = esValidoMovimientoAFundacion(cartaOrigen, idxDestino);
        } else if (tipoDestino == tipoReserva) {
            movimientoValido = esValidoMovimientoAReserva(idxDestino);
        } else if (tipoDestino == tipoColumna) {
            movimientoValido = esValidoMovimientoAColumna(cartaOrigen, idxDestino);
        }

        if (movimientoValido) {
            CartaInglesa removida = extraerCartaSuperior(tipoOrigen, idxOrigen);
            insertarEnContenedor(tipoDestino, idxDestino, removida);
            historial.guardarMovimiento(tipoOrigen, idxOrigen, tipoDestino, idxDestino, removida);
            return true;
        }

        return false;
    }

    public boolean esValidoMovimientoAFundacion(CartaInglesa carta, int idxFundacion) {
        CartaInglesa ultima = fundaciones[idxFundacion].getUltimaCarta();
        if (ultima == null) {
            return carta.getValorBajo() == 1 && carta.tieneElMismoPalo(Palo.values()[idxFundacion]);
        }
        return carta.tieneElMismoPalo(ultima.getPalo()) && (carta.getValorBajo() == ultima.getValorBajo() + 1);
    }

    public boolean esValidoMovimientoAReserva(int idxReserva) {
        return reservas[idxReserva].estaVacio();
    }

    public boolean esValidoMovimientoAColumna(CartaInglesa carta, int idxColumna) {
        return columnas[idxColumna].sePuedeAgregarCarta(carta);
    }

    public boolean deshacer() {
        if (historial.estaVacio()) {
            return false;
        }

        RegistroMovimiento ultimo = historial.obtenerUltimoMovimiento();
        CartaInglesa carta = extraerCartaSuperior(ultimo.getTipoDestino(), ultimo.getIdxDestino());
        insertarEnContenedor(ultimo.getTipoOrigen(), ultimo.getIdxOrigen(), carta);
        return true;
    }

    public CartaInglesa obtenerCartaSuperior(int tipo, int indice) {
        if (tipo == tipoColumna) return columnas[indice].getUltimaCarta();
        if (tipo == tipoReserva) return reservas[indice].getCarta();
        if (tipo == tipoFundacion) return fundaciones[indice].getUltimaCarta();
        return null;
    }

    private CartaInglesa extraerCartaSuperior(int tipo, int indice) {
        if (tipo == tipoColumna) return columnas[indice].removerUltimaCarta();
        if (tipo == tipoReserva) return reservas[indice].removerCarta();
        if (tipo == tipoFundacion) return fundaciones[indice].removerUltimaCarta();
        return null;
    }

    private void insertarEnContenedor(int tipo, int indice, CartaInglesa carta) {
        if (tipo == tipoColumna) columnas[indice].insertarDirecto(carta);
        else if (tipo == tipoReserva) reservas[indice].agregarCarta(carta);
        else if (tipo == tipoFundacion) fundaciones[indice].agregarCarta(carta);
    }

    public boolean verificarVictoria() {
        int total = 0;
        for (int i = 0; i < 4; i++) {
            total += fundaciones[i].getCartasComoLista().size();
        }
        return total == 52;
    }

    public boolean puedeDeshacer() {
        return !historial.estaVacio();
    }

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