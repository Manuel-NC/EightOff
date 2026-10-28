package com.example.eightoff.Modelo;

import java.util.ArrayList;

// Guarda los datos de un movimiento ejecutado para poder deshacerlo después
public class RegistroMovimiento {

    private int tipoOrigen;
    private int idxOrigen;
    private int tipoDestino;
    private int idxDestino;
    private ArrayList<CartaInglesa> cartas;

    // Constructor para cuando se mueve 1 sola carta
    public RegistroMovimiento(int tipoOrigen, int idxOrigen, int tipoDestino, int idxDestino, CartaInglesa carta) {
        this.tipoOrigen = tipoOrigen;
        this.idxOrigen = idxOrigen;
        this.tipoDestino = tipoDestino;
        this.idxDestino = idxDestino;
        this.cartas = new ArrayList<>();
        this.cartas.add(carta);
    }

    // Constructor para cuando se mueve un bloque/secuencia de varias cartas
    public RegistroMovimiento(int tipoOrigen, int idxOrigen, int tipoDestino, int idxDestino, ArrayList<CartaInglesa> cartas) {
        this.tipoOrigen = tipoOrigen;
        this.idxOrigen = idxOrigen;
        this.tipoDestino = tipoDestino;
        this.idxDestino = idxDestino;
        this.cartas = new ArrayList<>(cartas);
    }

    // Getters para consultar los datos del movimiento a revertir
    public int getTipoOrigen() { return tipoOrigen; }
    public int getIdxOrigen() { return idxOrigen; }
    public int getTipoDestino() { return tipoDestino; }
    public int getIdxDestino() { return idxDestino; }
    public ArrayList<CartaInglesa> getCartas() { return cartas; }
}
