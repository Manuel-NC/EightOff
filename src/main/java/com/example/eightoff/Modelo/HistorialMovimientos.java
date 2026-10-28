package com.example.eightoff.Modelo;

import java.util.ArrayList;
import java.util.Stack;

// Maneja la pila de deshacer (Undo) del juego
public class HistorialMovimientos {

    private Stack<RegistroMovimiento> pila;

    public HistorialMovimientos() {
        pila = new Stack<>();
    }

    // Agrega un movimiento individual a la pila
    public void guardarMovimiento(int tipoOrigen, int idxOrigen, int tipoDestino, int idxDestino, CartaInglesa carta) {
        RegistroMovimiento movimiento = new RegistroMovimiento(tipoOrigen, idxOrigen, tipoDestino, idxDestino, carta);
        pila.push(movimiento);
    }

    // Agrega un movimiento de bloque completo a la pila
    public void guardarMovimiento(int tipoOrigen, int idxOrigen, int tipoDestino, int idxDestino, ArrayList<CartaInglesa> bloque) {
        RegistroMovimiento movimiento = new RegistroMovimiento(tipoOrigen, idxOrigen, tipoDestino, idxDestino, bloque);
        pila.push(movimiento);
    }

    // Saca y devuelve el último movimiento registrado
    public RegistroMovimiento obtenerUltimoMovimiento() {
        if (pila.isEmpty()) {
            return null;
        }
        return pila.pop();
    }

    // Vacia el historial (usado al iniciar una nueva partida)
    public void limpiar() {
        pila.clear();
    }

    // Indica si no hay movimientos por deshacer
    public boolean estaVacio() {
        return pila.isEmpty();
    }
}