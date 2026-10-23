package com.example.eightoff.Modelo;

import java.util.Stack;

public class HistorialMovimientos {

    private Stack<RegistroMovimiento> pila;

    public HistorialMovimientos() {
        pila = new Stack<>();
    }

    public void guardarMovimiento(int tipoOrigen, int idxOrigen, int tipoDestino, int idxDestino, CartaInglesa carta) {
        RegistroMovimiento movimiento = new RegistroMovimiento(tipoOrigen, idxOrigen, tipoDestino, idxDestino, carta);
        pila.push(movimiento);
    }

    public RegistroMovimiento obtenerUltimoMovimiento() {
        if (pila.isEmpty()) {
            return null;
        }
        return pila.pop();
    }

    public void limpiar() {
        pila.clear();
    }

    public boolean estaVacio() {
        return pila.isEmpty();
    }
}