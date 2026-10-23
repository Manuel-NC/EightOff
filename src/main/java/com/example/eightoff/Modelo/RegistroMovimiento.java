package com.example.eightoff.Modelo;

public class RegistroMovimiento {

    private int tipoOrigen;
    private int idxOrigen;
    private int tipoDestino;
    private int idxDestino;
    private CartaInglesa carta;

    public RegistroMovimiento(int tipoOrigen, int idxOrigen, int tipoDestino, int idxDestino, CartaInglesa carta) {
        this.tipoOrigen = tipoOrigen;
        this.idxOrigen = idxOrigen;
        this.tipoDestino = tipoDestino;
        this.idxDestino = idxDestino;
        this.carta = carta;
    }

    public int getTipoOrigen() {
        return tipoOrigen;
    }

    public int getIdxOrigen() {
        return idxOrigen;
    }

    public int getTipoDestino() {
        return tipoDestino;
    }

    public int getIdxDestino() {
        return idxDestino;
    }

    public CartaInglesa getCarta() {
        return carta;
    }
}
