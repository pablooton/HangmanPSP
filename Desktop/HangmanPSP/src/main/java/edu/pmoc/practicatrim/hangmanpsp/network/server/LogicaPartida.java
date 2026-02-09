package edu.pmoc.practicatrim.hangmanpsp.network.server;

import java.util.Random;

public class LogicaPartida {
    private String palabraSecreta;
    private char[] progreso;
    private int numJugadores;
    private int turnoActual;
    private int[] vidas = {6, 6};
    private boolean activa = true;
    private boolean cancelada = false;

    public LogicaPartida(String palabra, int numJugadores) {
        this.palabraSecreta = palabra.toUpperCase();
        this.numJugadores = numJugadores;
        this.progreso = new char[palabra.length()];
        for (int i = 0; i < progreso.length; i++) progreso[i] = '_';

        this.turnoActual = (numJugadores == 2) ? new Random().nextInt(2) : 0;

        System.out.println("[SERVIDOR] Partida creada. Palabra: " + palabraSecreta + " | Jugadores: " + numJugadores);
    }

    public synchronized void procesarJugada(int idJugador, char letra) {
        if (idJugador != turnoActual || !activa) return;

        boolean acierto = false;
        char letraMayus = Character.toUpperCase(letra);

        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letraMayus) {
                progreso[i] = letraMayus;
                acierto = true;
            }
        }

        if (!acierto) {
            vidas[idJugador]--;
            System.out.println("[SERVIDOR] Jugador " + idJugador + " falló. Letra: " + letraMayus + " | Vidas: " + vidas[idJugador]);
            if (numJugadores == 2) {
                cambiarTurno();
            }
        } else {
            System.out.println("[SERVIDOR] Jugador " + idJugador + " acertó. Letra: " + letraMayus);
        }

        verificarFinal();
        notifyAll();
    }

    private void cambiarTurno() {
        int siguiente = (turnoActual == 0) ? 1 : 0;
        if (vidas[siguiente] > 0) {
            turnoActual = siguiente;
            System.out.println("[SERVIDOR] Cambio de turno. Ahora le toca al Jugador " + turnoActual);
        }
    }

    private void verificarFinal() {
        boolean palabraCompleta = !String.valueOf(progreso).contains("_");

        boolean todosSinVidas = true;
        for (int i = 0; i < numJugadores; i++) {
            if (vidas[i] > 0) {
                todosSinVidas = false;
                break;
            }
        }

        if (palabraCompleta || todosSinVidas || cancelada) {
            this.activa = false;
            if (palabraCompleta) System.out.println("[SERVIDOR] Fin: ¡Palabra adivinada!");
            if (todosSinVidas) System.out.println("[SERVIDOR] Fin: Jugadores sin vidas.");
        }
    }

    public synchronized void cancelarPartida() {
        this.cancelada = true;
        this.activa = false;
        System.out.println("[SERVIDOR] Un jugador ha pulsado CANCELAR. Partida anulada.");
        notifyAll();
    }

    public int calcularPuntos() {
        return (palabraSecreta.length() >= 10) ? 2 : 1;
    }

    public synchronized boolean isActiva() { return activa; }

    public synchronized boolean isCancelada() { return cancelada; }

    public synchronized String getProgreso() { return String.valueOf(progreso); }

    public synchronized int getTurnoActual() { return turnoActual; }

    public synchronized int getVidas(int id) { return vidas[id]; }

    public String getPalabraSecreta() { return palabraSecreta; }
}