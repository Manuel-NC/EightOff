package com.example.eightoff.Estructuras;

public class ListaSimple<T> {
    private Nodo<T> inicio;

    public void insertarInicio(T dato) {
        Nodo<T> n = new Nodo<>(dato);
        n.setSig(inicio);
        inicio = n;
    }

    public void insertarFin(T dato) {
        Nodo<T> n = new Nodo<>(dato);
        if (inicio == null) {
            n.setSig(inicio);
            inicio = n;
        } else {
            Nodo<T> r = inicio;
            while (r.getSig() != null) {
                r = r.getSig();
            }

            r.setSig(n);
            n.setSig(null);
        }
    }

    public T eliminaInicio() {

        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else {
            T dato = inicio.getDato();
            inicio = inicio.getSig();
            return (T) dato;
        }
    }

    public T eliminaFin() {
        Nodo<T> r;
        Nodo<T> a;
        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        } else {
            if (inicio.getSig() == null) {
                T dato = inicio.getDato();
                inicio = null;
                return dato;
            } else {
                r = inicio;
            }
            a = r;
            while (r.getSig() != null) {
                a = r;
                r = r.getSig();
            }
            a.setSig(null);
        }
        return r.getDato();
    }
}
