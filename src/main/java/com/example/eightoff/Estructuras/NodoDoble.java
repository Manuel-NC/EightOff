package com.example.eightoff.Estructuras;

public class NodoDoble<T> {

    private T dato;
    private NodoDoble<T> sig;
    private NodoDoble<T> ant;

    public NodoDoble() {
        dato = (T) new Object();
        sig = null;
        ant = null;
    }

    public NodoDoble(T dato) {
        this.dato = dato;
        sig = null;
        ant = null;
    }

    public NodoDoble(T dato, NodoDoble<T> sig, NodoDoble<T> ant) {
        this.dato = dato;
        this.sig = sig;
        this.ant = ant;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public void setSig(NodoDoble<T> sig) {
        this.sig = sig;
    }

    public void setAnt(NodoDoble<T> ant) { this.ant = ant;}

    public T getDato() {
        return dato;
    }

    public NodoDoble<T> getSig() {
        return this.sig;
    }

    public NodoDoble<T> getAnt() {
        return this.ant;
    }
}

