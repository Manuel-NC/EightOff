package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaCircularDoble;
import com.example.eightoff.Estructuras.ListaSimple;

import java.util.ArrayList;
import java.util.Collections;

public class EightOffModelo {

    public int tipoColumna = 0;
    public int tipoReserva = 1;
    public int tipoFundacion = 2;

    private ListaCircularDoble<CartaInglesa> baraja;
    private ListaSimple<CartaInglesa>[] columnas;
    private ListaSimple<CartaInglesa>[] reservas;
    private ListaSimple<CartaInglesa>[] fundaciones;

    private HistorialMovimientos historial;

    public EightOffModelo() {
        columnas = new ListaSimple[8];
        reservas = new ListaSimple[8];
        fundaciones = new ListaSimple[4];
        historial = new HistorialMovimientos();
        iniciarPartida();
    }

    public void iniciarPartida() {
        for (int i = 0; i < 8; i++) {
            columnas[i] = new ListaSimple<>();
            reservas[i] = new ListaSimple<>();
        }
        for (int i = 0; i < 4; i++) {
            fundaciones[i] = new ListaSimple<>();
        }
        historial.limpiar();

        baraja = new ListaCircularDoble<>();
        ArrayList<CartaInglesa> tempMazo = new ArrayList<>();

        for (Palo palo : Palo.values()) {
            for (int valor = 2; valor <= 14; valor++) {
                CartaInglesa carta = new CartaInglesa(valor, palo, palo.getColor());
                carta.makeFaceUp();
                tempMazo.add(carta);
            }
        }

        Collections.shuffle(tempMazo);
        for (CartaInglesa c : tempMazo) {
            baraja.insertaFin(c);
        }

        for (int col = 0; col < 8; col++) {
            for (int i = 0; i < 6; i++) {
                CartaInglesa carta = baraja.eliminaInicio();
                columnas[col].insertarFin(carta);
            }
        }

        for (int res = 0; res < 4; res++) {
            CartaInglesa carta = baraja.eliminaInicio();
            reservas[res].insertarFin(carta);
        }
    }

    public boolean moverCarta(int tipoOrigen, int idxOrigen, int tipoDestino, int idxDestino) {
        CartaInglesa cartaOrigen = obtenerCartaSuperior(tipoOrigen, idxOrigen);
        if (cartaOrigen == null) {
            return false;
        }

        boolean movimientoValido = false;

        if (tipoDestino == tipoFundacion) {
            movimientoValido = esValidoMovimientosAFundacion(cartaOrigen, idxDestino);
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

    private boolean esValidoMovimientosAFundacion(CartaInglesa carta, int idxFundacion) {
        CartaInglesa cartaFundacion = obtenerCartaSuperior(tipoFundacion, idxFundacion);

        if (cartaFundacion == null) {
            return carta.getValorBajo() == 1;
        } else {
            if (carta.tieneElMismoPalo(cartaFundacion.getPalo())) {
                int valActual = cartaFundacion.getValorBajo();
                int valNuevo = carta.getValorBajo();
                return valNuevo == valActual + 1;
            }
        }
        return false;
    }

    private boolean esValidoMovimientoAReserva(int idxReserva) {
        return obtenerCartaSuperior(tipoReserva, idxReserva) == null;
    }

    private boolean esValidoMovimientoAColumna(CartaInglesa carta, int idxColumna) {
        CartaInglesa cartaColumna = obtenerCartaSuperior(tipoColumna, idxColumna);

        if (cartaColumna == null) {
            return true;
        } else {
            if (carta.tieneElMismoPalo(cartaColumna.getPalo())) {
                return cartaColumna.getValorBajo() == carta.getValorBajo() + 1;
            }
        }
        return false;
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

    public String darPista() {
        for (int i = 0; i < 8; i++) {
            CartaInglesa cCol = obtenerCartaSuperior(tipoColumna, i);
            if (cCol != null) {
                for (int f = 0; f < 4; f++) {
                    if (esValidoMovimientosAFundacion(cCol, f)) {
                        return "Mueve el " + cCol + " de la Columna " + (i + 1) + " a la Fundación " + (f + 1);
                    }
                }
            }

            CartaInglesa cRes = obtenerCartaSuperior(tipoReserva, i);
            if (cRes != null) {
                for (int f = 0; f < 4; f++) {
                    if (esValidoMovimientosAFundacion(cRes, f)) {
                        return "Mueve el " + cRes + " de la Reserva " + (i + 1) + " a la Fundación " + (f + 1);
                    }
                }
            }
        }

        for (int orig = 0; orig < 8; orig++) {
            CartaInglesa cOrig = obtenerCartaSuperior(tipoColumna, orig);
            if (cOrig == null) continue;

            for (int dest = 0; dest < 8; dest++) {
                if (orig == dest) continue;
                if (esValidoMovimientoAColumna(cOrig, dest)) {
                    return "Mueve el " + cOrig + " de Columna " + (orig + 1) + " a Columna " + (dest + 1);
                }
            }
        }

        for (int res = 0; res < 8; res++) {
            CartaInglesa cRes = obtenerCartaSuperior(tipoReserva, res);
            if (cRes == null) continue;

            for (int col = 0; col < 8; col++) {
                if (esValidoMovimientoAColumna(cRes, col)) {
                    return "Mueve el " + cRes + " de la Reserva " + (res + 1) + " a la Columna " + (col + 1);
                }
            }
        }

        for (int col = 0; col < 8; col++) {
            CartaInglesa cCol = obtenerCartaSuperior(tipoColumna, col);
            if (cCol != null) {
                for (int res = 0; res < 8; res++) {
                    if (esValidoMovimientoAReserva(res)) {
                        return "Mueve el " + cCol + " de Columna " + (col + 1) + " a Reserva " + (res + 1);
                    }
                }
            }
        }

        return "No hay movimientos válidos disponibles.";
    }

    public boolean verificarFinJuego() {
        return darPista().equals("No hay movimientos válidos disponibles.");
    }

    public boolean verificarVictoria() {
        int total = 0;
        for (int i = 0; i < 4; i++) {
            total += obtenerCartasDeLista(fundaciones[i]).size();
        }
        return total == 52;
    }

    public CartaInglesa obtenerCartaSuperior(int tipo, int indice) {
        ListaSimple<CartaInglesa> lista = obtenerLista(tipo, indice);
        if (lista == null || lista.estaVacia()) return null;

        CartaInglesa carta = lista.eliminaFin();
        if (carta != null) {
            lista.insertarFin(carta);
        }
        return carta;
    }

    private CartaInglesa extraerCartaSuperior(int tipo, int indice) {
        ListaSimple<CartaInglesa> lista = obtenerLista(tipo, indice);
        return (lista != null) ? lista.eliminaFin() : null;
    }

    private void insertarEnContenedor(int tipo, int indice, CartaInglesa carta) {
        ListaSimple<CartaInglesa> lista = obtenerLista(tipo, indice);
        if (lista != null && carta != null) {
            lista.insertarFin(carta);
        }
    }

    private ListaSimple<CartaInglesa> obtenerLista(int tipo, int indice) {
        if (tipo == tipoColumna && indice >= 0 && indice < 8) return columnas[indice];
        if (tipo == tipoReserva && indice >= 0 && indice < 8) return reservas[indice];
        if (tipo == tipoFundacion && indice >= 0 && indice < 4) return fundaciones[indice];
        return null;
    }

    public ArrayList<CartaInglesa> obtenerCartasDeLista(ListaSimple<CartaInglesa> lista) {
        ArrayList<CartaInglesa> resultado = new ArrayList<>();
        if (lista == null) return resultado;

        // Se detiene antes de llamar a eliminaInicio() sobre una lista vacia
        while (!lista.estaVacia()) {
            resultado.add(lista.eliminaInicio());
        }
        for (CartaInglesa carta : resultado) {
            lista.insertarFin(carta);
        }
        return resultado;
    }

    public ArrayList<CartaInglesa> getColumna(int index) {
        return obtenerCartasDeLista(columnas[index]);
    }

    public ArrayList<CartaInglesa> getReserva(int index) {
        return obtenerCartasDeLista(reservas[index]);
    }

    public ArrayList<CartaInglesa> getFundacion(int index) {
        return obtenerCartasDeLista(fundaciones[index]);
    }
}