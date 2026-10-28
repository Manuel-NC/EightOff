package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaSimple;
import java.util.ArrayList;

public class TableauDeck {
    private ListaSimple<CartaInglesa> cartas;

    public TableauDeck() {
        this.cartas = new ListaSimple<>();
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (sePuedeAgregarCarta(carta)) {
            cartas.insertarFin(carta);
            return true;
        }
        return false;
    }

    public boolean sePuedeAgregarCarta(CartaInglesa carta) {
        CartaInglesa ultima = getUltimaCarta();
        if (ultima == null) {
            // Regla Eight Off: Columna vacia solo acepta Rey (13)
            return carta.getValor() == 13;
        } else {
            // Mismo palo y valor consecutivo descendente
            return carta.tieneElMismoPalo(ultima.getPalo()) && (ultima.getValorBajo() == carta.getValorBajo() + 1);
        }
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

    public void insertarDirecto(CartaInglesa c) {
        cartas.insertarFin(c);
    }
}
