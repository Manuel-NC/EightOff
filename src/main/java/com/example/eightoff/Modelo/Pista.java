package com.example.eightoff.Modelo;

public class Pista {

    public static class PistaResultado {
        private boolean[] columnasMovibles;
        private boolean[] reservasMovibles;
        private boolean hayMovimientos;
        private String mensaje;

        public PistaResultado(boolean[] columnasMovibles, boolean[] reservasMovibles, boolean hayMovimientos, String mensaje) {
            this.columnasMovibles = columnasMovibles;
            this.reservasMovibles = reservasMovibles;
            this.hayMovimientos = hayMovimientos;
            this.mensaje = mensaje;
        }

        public boolean esColumnaMovible(int idx) {
            return columnasMovibles != null && idx >= 0 && idx < columnasMovibles.length && columnasMovibles[idx];
        }

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

    public static PistaResultado calcularPista(EightOffModelo modelo) {
        boolean[] columnasMovibles = new boolean[8];
        boolean[] reservasMovibles = new boolean[8];
        boolean hayMovimientos = false;

        // 1. Evaluar cartas superiores de las Columnas
        for (int col = 0; col < 8; col++) {
            CartaInglesa cCol = modelo.obtenerCartaSuperior(modelo.tipoColumna, col);
            if (cCol == null) continue;

            boolean sePuedeMover = false;

            // Se puede mover a alguna fundacion?
            for (int f = 0; f < 4; f++) {
                if (modelo.esValidoMovimientoAFundacion(cCol, f)) {
                    sePuedeMover = true;
                    break;
                }
            }

            // Se puede mover a otra columna?
            if (!sePuedeMover) {
                for (int destCol = 0; destCol < 8; destCol++) {
                    if (col == destCol) continue;
                    if (modelo.esValidoMovimientoAColumna(cCol, destCol)) {
                        sePuedeMover = true;
                        break;
                    }
                }
            }

            // Se puede mover a alguna reserva libre?
            if (!sePuedeMover) {
                for (int res = 0; res < 8; res++) {
                    if (modelo.esValidoMovimientoAReserva(res)) {
                        sePuedeMover = true;
                        break;
                    }
                }
            }

            if (sePuedeMover) {
                columnasMovibles[col] = true;
                hayMovimientos = true;
            }
        }

        // 2. Evaluar cartas en las Reservas
        for (int res = 0; res < 8; res++) {
            CartaInglesa cRes = modelo.obtenerCartaSuperior(modelo.tipoReserva, res);
            if (cRes == null) continue;

            boolean sePuedeMover = false;

            // Se puede mover a alguna fundacion?
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
                : "No hay movimientos validos disponibles.";

        return new PistaResultado(columnasMovibles, reservasMovibles, hayMovimientos, mensaje);
    }
}
