package com.example.eightoff.Modelo;

import java.util.ArrayList;

// Evaluador de movimientos válidos para generar la sugerencia e iluminar las cartas movibles
public class Pista {

    // Estructura contenedora del resultado del análisis de pistas
    public static class PistaResultado {
        private boolean[][] columnasMovibles;
        private boolean[] reservasMovibles;
        private boolean hayMovimientos;
        private String mensaje;

        public PistaResultado(boolean[][] columnasMovibles, boolean[] reservasMovibles, boolean hayMovimientos, String mensaje) {
            this.columnasMovibles = columnasMovibles;
            this.reservasMovibles = reservasMovibles;
            this.hayMovimientos = hayMovimientos;
            this.mensaje = mensaje;
        }

        // Revisa si una carta en una posición de columna específica se puede mover
        public boolean esCartaColumnaMovible(int colIdx, int cardIdx) {
            return columnasMovibles != null && colIdx >= 0 && colIdx < columnasMovibles.length
                    && columnasMovibles[colIdx] != null && cardIdx >= 0
                    && cardIdx < columnasMovibles[colIdx].length && columnasMovibles[colIdx][cardIdx];
        }

        // Revisa si la carta de una reserva dada se puede mover
        public boolean esReservaMovible(int idx) {
            return reservasMovibles != null && idx >= 0 && idx < reservasMovibles.length && reservasMovibles[idx];
        }

        public boolean isHayMovimientos() {
            return hayMovimientos;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    // Analiza el tablero e ilumina ÚNICAMENTE cartas que van a Fundación o a otra Columna
    public static PistaResultado calcularPista(EightOffModelo modelo) {
        boolean[][] columnasMovibles = new boolean[8][];
        boolean[] reservasMovibles = new boolean[8];
        boolean hayMovimientos = false;

        // 1. Evaluar cartas en las Columnas
        for (int col = 0; col < 8; col++) {
            ArrayList<CartaInglesa> cartasCol = modelo.getColumna(col);
            columnasMovibles[col] = new boolean[cartasCol.size()];

            for (int cardIdx = 0; cardIdx < cartasCol.size(); cardIdx++) {
                if (!modelo.esSecuenciaValida(col, cardIdx)) continue;

                ArrayList<CartaInglesa> bloque = modelo.obtenerBloqueOrigen(modelo.tipoColumna, col, cardIdx);
                if (bloque == null || bloque.isEmpty()) continue;

                CartaInglesa primera = bloque.get(0);
                boolean sePuedeMover = false;

                // ¿e puede mover a alguna fundación? (Solo cartas individuales)
                if (bloque.size() == 1) {
                    for (int f = 0; f < 4; f++) {
                        if (modelo.esValidoMovimientoAFundacion(primera, f)) {
                            sePuedeMover = true;
                            break;
                        }
                    }
                }

                // Se puede mover a otra columna?
                if (!sePuedeMover) {
                    for (int destCol = 0; destCol < 8; destCol++) {
                        if (col == destCol) continue;
                        if (modelo.esValidoMovimientoAColumna(primera, destCol)) {
                            sePuedeMover = true;
                            break;
                        }
                    }
                }

                // Marca las cartas de la columna si pueden ir a Fundación o Columna
                if (sePuedeMover) {
                    for (int k = cardIdx; k < cartasCol.size(); k++) {
                        columnasMovibles[col][k] = true;
                    }
                    hayMovimientos = true;
                }
            }
        }

        // 2. Evaluar cartas en las Reservas (Solo si pueden ir a Fundación o Columna)
        for (int res = 0; res < 8; res++) {
            CartaInglesa cRes = modelo.obtenerCartaSuperior(modelo.tipoReserva, res);
            if (cRes == null) continue;

            boolean sePuedeMover = false;

            // Se puede mover a alguna fundación?
            for (int f = 0; f < 4; f++) {
                if (modelo.esValidoMovimientoAFundacion(cRes, f)) {
                    sePuedeMover = true;
                    break;
                }
            }

            // Se puede mover a alguna columna?
            if (!sePuedeMover) {
                for (int col = 0; col < 8; col++) {
                    if (modelo.esValidoMovimientoAColumna(cRes, col)) {
                        sePuedeMover = true;
                        break;
                    }
                }
            }

            if (sePuedeMover) {
                reservasMovibles[res] = true;
                hayMovimientos = true;
            }
        }

        String mensaje = hayMovimientos
                ? "Iluminando todas las cartas que se pueden mover."
                : "No hay movimientos válidos disponibles.";

        return new PistaResultado(columnasMovibles, reservasMovibles, hayMovimientos, mensaje);
    }
}
