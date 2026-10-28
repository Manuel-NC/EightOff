package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaSimple;

public class ReservaDeck {
    private ListaSimple<CartaInglesa> carta;

    public ReservaDeck() {
        this.carta = new ListaSimple<>();
    }

    public boolean agregarCarta(CartaInglesa c) {
        if (estaVacio()) {
            carta.insertarFin(c);
            return true;
        }
        return false;
    }

    public CartaInglesa removerCarta() {
        return carta.eliminaFin();
    }

    public CartaInglesa getCarta() {
        CartaInglesa c = carta.eliminaFin();
        if (c != null) {
            carta.insertarFin(c);
        }
        return c;
    }

    public boolean estaVacio() {
        return carta.estaVacia();
    }
}
