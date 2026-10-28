package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaSimple;
import java.util.ArrayList;

public class FoundationDeck {
    private Palo palo;
    private ListaSimple<CartaInglesa> cartas;

    public FoundationDeck(Palo palo) {
        this.palo = palo;
        this.cartas = new ListaSimple<>();
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (!carta.tieneElMismoPalo(palo)) {
            return false;
        }

        CartaInglesa ultima = getUltimaCarta();
        if (ultima == null) {
            if (carta.getValorBajo() == 1) { // As
                cartas.insertarFin(carta);
                return true;
            }
        } else {
            if (carta.getValorBajo() == ultima.getValorBajo() + 1) {
                cartas.insertarFin(carta);
                return true;
            }
        }
        return false;
    }

    public CartaInglesa removerUltimaCarta() {
        return cartas.eliminaFin();
    }

    public CartaInglesa getUltimaCarta() {
        CartaInglesa c = cartas.eliminaFin();
        if (c != null) {
            cartas.insertarFin(c);
        }
        return c;
    }

    public boolean estaVacio() {
        return cartas.estaVacia();
    }

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
