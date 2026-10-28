package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaSimple;
import java.util.ArrayList;

// Representa cada una de las 4 fundaciones donde se van acumulando las cartas del As al Rey por palo
public class FoundationDeck {
    private Palo palo; // Inicia en null y se fija dinámicamente al poner el primer As
    private ListaSimple<CartaInglesa> cartas;

    // Constructor sin palo predefinido (dinámico)
    public FoundationDeck() {
        this.palo = null;
        this.cartas = new ListaSimple<>();
    }

    // Intenta agregar una carta respetando la regla del As y la secuencia ascendente del mismo palo
    public boolean agregarCarta(CartaInglesa carta) {
        CartaInglesa ultima = getUltimaCarta();

        // Si está vacía, acepta cualquier As y fija el palo de la fundación
        if (ultima == null) {
            if (carta.getValorBajo() == 1) {
                this.palo = carta.getPalo();
                cartas.insertarFin(carta);
                return true;
            }
            return false;
        }

        // Si ya hay cartas, debe coincidir en palo y ser el número siguiente
        if (carta.tieneElMismoPalo(this.palo) && (carta.getValorBajo() == ultima.getValorBajo() + 1)) {
            cartas.insertarFin(carta);
            return true;
        }
        return false;
    }

    // Elimina la última carta guardada y si queda vacía, libera el palo asignado
    public CartaInglesa removerUltimaCarta() {
        if (cartas.estaVacia()) {
            return null;
        }
        CartaInglesa removida = cartas.eliminaFin();
        if (cartas.estaVacia()) {
            this.palo = null; // Reinicia el palo para que acepte otro As si se vuelve a vaciar
        }
        return removida;
    }

    // Consulta la carta que está hasta arriba sin sacarla de la lista
    public CartaInglesa getUltimaCarta() {
        if (cartas.estaVacia()) {
            return null;
        }
        CartaInglesa c = cartas.eliminaFin();
        if (c != null) {
            cartas.insertarFin(c);
        }
        return c;
    }

    // Devuelve el palo actual de esta fundación
    public Palo getPalo() {
        return palo;
    }

    // Checa si la fundación no tiene cartas
    public boolean estaVacio() {
        return cartas.estaVacia();
    }

    // Convierte el contenido de la ListaSimple a un ArrayList auxiliar para enviarlo a la vista
    public ArrayList<CartaInglesa> getCartasComoLista() {
        ArrayList<CartaInglesa> resultado = new ArrayList<>();
        while (!cartas.estaVacia()) {
            resultado.add(cartas.eliminaInicio());
        }
        for (CartaInglesa c : resultado) {
            cartas.insertarFin(c);
        }
        return resultado;
    }
}