package com.example.eightoff.Modelo;

import com.example.eightoff.Estructuras.ListaSimple;
import java.util.ArrayList;

// Modela una columna del tablero usando ListaSimple
public class TableauDeck {
    private ListaSimple<CartaInglesa> cartas;

    public TableauDeck() {
        this.cartas = new ListaSimple<>();
    }

    // Intenta agregar una carta validando las reglas
    public boolean agregarCarta(CartaInglesa carta) {
        if (sePuedeAgregarCarta(carta)) {
            cartas.insertarFin(carta);
            return true;
        }
        return false;
    }

    // Valida si la carta puede ir en la columna (si está vacía sólo entra un Rey (13), si no, debe ser del mismo palo y valor descendente)
    public boolean sePuedeAgregarCarta(CartaInglesa carta) {
        CartaInglesa ultima = getUltimaCarta();
        if (ultima == null) {
            return carta.getValor() == 13; // Regla de Eight Off: espacio vacío sólo para Rey
        } else {
            return carta.tieneElMismoPalo(ultima.getPalo()) && (ultima.getValorBajo() == carta.getValorBajo() + 1);
        }
    }

    // Revisa si las cartas desde un índice específico forman un bloque/secuencia ordenada válida
    public boolean esSecuenciaValidaDesde(int index) {
        ArrayList<CartaInglesa> lista = getCartasComoLista();
        if (index < 0 || index >= lista.size()) {
            return false;
        }
        for (int i = index; i < lista.size() - 1; i++) {
            CartaInglesa actual = lista.get(i);
            CartaInglesa siguiente = lista.get(i + 1);
            if (!siguiente.tieneElMismoPalo(actual.getPalo()) || actual.getValorBajo() != siguiente.getValorBajo() + 1) {
                return false;
            }
        }
        return true;
    }

    // Retorna la sublista de cartas desde un índice si la secuencia es válida
    public ArrayList<CartaInglesa> obtenerSecuenciaDesde(int index) {
        if (!esSecuenciaValidaDesde(index)) {
            return null;
        }
        ArrayList<CartaInglesa> lista = getCartasComoLista();
        ArrayList<CartaInglesa> sublista = new ArrayList<>();
        for (int i = index; i < lista.size(); i++) {
            sublista.add(lista.get(i));
        }
        return sublista;
    }

    // Quita de la columna y regresa un bloque de cartas desde cierto índice en adelante
    public ArrayList<CartaInglesa> removerSecuenciaDesde(int index) {
        if (!esSecuenciaValidaDesde(index)) {
            return null;
        }
        ArrayList<CartaInglesa> lista = getCartasComoLista();
        ArrayList<CartaInglesa> removidas = new ArrayList<>();

        this.cartas = new ListaSimple<>();
        for (int i = 0; i < index; i++) {
            this.cartas.insertarFin(lista.get(i));
        }
        for (int i = index; i < lista.size(); i++) {
            removidas.add(lista.get(i));
        }
        return removidas;
    }

    // Agrega un bloque completo de cartas a la columna
    public void agregarBloque(ArrayList<CartaInglesa> bloque) {
        if (bloque == null) return;
        for (CartaInglesa c : bloque) {
            cartas.insertarFin(c);
        }
    }

    // Quita y devuelve la carta que esté al final
    public CartaInglesa removerUltimaCarta() {
        if (cartas.estaVacia()) {
            return null;
        }
        return cartas.eliminaFin();
    }

    // Ve la carta del final sin eliminarla
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

    // Checa si la columna no tiene cartas
    public boolean estaVacio() {
        return cartas.estaVacia();
    }

    // Pasa el contenido de la ListaSimple a un ArrayList para refrescar la interfaz
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

    // Inserta directo una carta a la columna sin aplicar validación (usado al repartir)
    public void insertarDirecto(CartaInglesa c) {
        cartas.insertarFin(c);
    }
}
