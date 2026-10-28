package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaSimple;

// Representa una celda individual de reserva usando ListaSimple (máximo 1 carta)
public class ReservaDeck {
    private ListaSimple<CartaInglesa> carta;

    public ReservaDeck() {
        this.carta = new ListaSimple<>();
    }

    // Guarda una carta en la reserva sólo si está libre
    public boolean agregarCarta(CartaInglesa c) {
        if (estaVacio()) {
            carta.insertarFin(c);
            return true;
        }
        return false;
    }

    // Remueve y entrega la carta almacenada
    public CartaInglesa removerCarta() {
        if (carta.estaVacia()) {
            return null;
        }
        return carta.eliminaFin();
    }

    // Ve la carta de la reserva sin quitarla
    public CartaInglesa getCarta() {
        if (carta.estaVacia()) {
            return null;
        }
        CartaInglesa c = carta.eliminaFin();
        if (c != null) {
            carta.insertarFin(c);
        }
        return c;
    }

    // Verifica si la casilla de reserva está disponible
    public boolean estaVacio() {
        return carta.estaVacia();
    }
}
