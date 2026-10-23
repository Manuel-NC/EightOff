package com.example.eightoff.Estructuras;

public class ListaCircularDoble<T> {
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;

    public ListaCircularDoble() {
        this.inicio = null;
        this.fin = null;
    }

    public void insertaInicio(T dato) {
        NodoDoble<T> n = new NodoDoble<>(dato);

        if (inicio == null) {
            inicio = fin = n;
            n.setSig(inicio);
            n.setAnt(inicio);
        } else {
            n.setSig(inicio);
            inicio.setAnt(n);
            inicio = n;
            fin.setSig(inicio);
            n.setAnt(fin);
        }
    }

    public void insertaFin(T dato) {
        NodoDoble<T> n = new NodoDoble<>(dato);

        if (inicio == null) {
            inicio = fin = n;
            n.setSig(inicio);
            n.setAnt(inicio);
        } else {
            n.setSig(inicio);
            inicio.setAnt(n);
            fin.setSig(n);
            n.setAnt(fin);
            fin = n;
        }
    }

    public T eliminaInicio() {
        T dato = null;

        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else if (inicio == fin) {
            dato = inicio.getDato();
            inicio = fin = null;
        } else {
            dato = inicio.getDato();
            fin.setSig(inicio.getSig());
            inicio = inicio.getSig();
            inicio.setAnt(fin);
        }
        return dato;
    }

    public T eliminaFin() {
        T dato = null;

        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else if (inicio == fin) {
            dato = fin.getDato();
            inicio = fin = null;
        } else {
            NodoDoble<T> r;
            dato = fin.getDato();
            r = fin.getAnt();
            r.setSig(inicio);
            inicio.setAnt(r);
            fin = r;
        }
        return dato;
    }
}
