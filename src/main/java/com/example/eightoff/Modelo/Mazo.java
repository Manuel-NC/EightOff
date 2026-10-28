package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaCircularDoble;
import java.util.ArrayList;
import java.util.Collections;

// Representa el mazo completo usando nuestra estructura de ListaCircularDoble
public class Mazo {
    private ListaCircularDoble<CartaInglesa> cartas;
    private int cantidad;

    // Crea el mazo y lo llena mezclando las cartas
    public Mazo() {
        cartas = new ListaCircularDoble<>();
        cantidad = 0;
        llenarYMezclar();
    }

    // Genera las 52 cartas inglesas, las revuelve con Collections.shuffle y las mete a la ListaCircularDoble
    private void llenarYMezclar() {
        ArrayList<CartaInglesa> temp = new ArrayList<>();
        for (Palo palo : Palo.values()) {
            for (int valor = 2; valor <= 14; valor++) {
                CartaInglesa c = new CartaInglesa(valor, palo, palo.getColor());
                c.makeFaceUp();
                temp.add(c);
            }
        }
        Collections.shuffle(temp); // Mezclar mazo
        for (CartaInglesa c : temp) {
            cartas.insertaFin(c);
            cantidad++;
        }
    }

    // Saca una carta del inicio del mazo
    public CartaInglesa obtenerUnaCarta() {
        if (estaVacio()) return null;
        cantidad--;
        return cartas.eliminaInicio();
    }

    // Revisa si ya no quedan cartas en el mazo
    public boolean estaVacio() {
        return cantidad == 0;
    }
}