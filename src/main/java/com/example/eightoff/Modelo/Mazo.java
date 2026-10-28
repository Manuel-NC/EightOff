package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaCircularDoble;
import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    private ListaCircularDoble<CartaInglesa> cartas;

    public Mazo() {
        cartas = new ListaCircularDoble<>();
        llenarYMezclar();
    }

    private void llenarYMezclar() {
        ArrayList<CartaInglesa> temp = new ArrayList<>();
        for (Palo palo : Palo.values()) {
            for (int valor = 2; valor <= 14; valor++) {
                CartaInglesa c = new CartaInglesa(valor, palo, palo.getColor());
                c.makeFaceUp();
                temp.add(c);
            }
        }
        Collections.shuffle(temp);
        for (CartaInglesa c : temp) {
            cartas.insertaFin(c);
        }
    }

    public CartaInglesa obtenerUnaCarta() {
        if (estaVacio()) return null;
        return cartas.eliminaInicio();
    }

    public boolean estaVacio() {
        CartaInglesa c = cartas.eliminaInicio();
        if (c == null) {
            return true;
        }
        cartas.insertaInicio(c);
        return false;
    }
}
