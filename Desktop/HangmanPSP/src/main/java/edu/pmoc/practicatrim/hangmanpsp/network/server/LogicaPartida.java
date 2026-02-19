package edu.pmoc.practicatrim.hangmanpsp.network.server;

import java.util.HashMap;
import java.util.Map;

public class LogicaPartida {
    private String palabraSecreta;
    private String progreso;
    private int turnoActual = 0;
    private int numJugadores;
    private boolean activa = true;
    private Map<Integer, Integer> vidasJugadores = new HashMap<>();

    public LogicaPartida(String palabra, int numJugadores) {
        this.numJugadores = numJugadores;
        iniciarNuevaRonda(palabra);
    }

    public synchronized void iniciarNuevaRonda(String nuevaPalabra) {
        this.palabraSecreta = nuevaPalabra.toUpperCase();
        this.progreso = "_".repeat(palabraSecreta.length());
        for (int i = 0; i < numJugadores; i++) {
            vidasJugadores.put(i, 6);
        }
        this.activa = true;
    }

    public synchronized void cancelarPartida() {
        this.activa = false;
    }

    public synchronized void procesarJugada(int id, char letra) {
        if (id != turnoActual || !activa) return;

        letra = Character.toUpperCase(letra);
        boolean acierto = false;
        StringBuilder nuevoProgreso = new StringBuilder(progreso);

        for (int i = 0; i < palabraSecreta.length(); i++) {
            if (palabraSecreta.charAt(i) == letra) {
                nuevoProgreso.setCharAt(i, letra);
                acierto = true;
            }
        }

        if (acierto) {
            progreso = nuevoProgreso.toString();
        } else {
            int v = vidasJugadores.get(id) - 1;
            vidasJugadores.put(id, v);

            if (v <= 0) {
                activa = false;
            } else if (numJugadores == 2) {
                this.turnoActual = (this.turnoActual == 0) ? 1 : 0;
            }
        }
    }
    public String getPalabraSecreta() {
        return palabraSecreta;
    }
    public String getProgreso() { return progreso; }
    public int getVidas(int id) { return vidasJugadores.getOrDefault(id, 0); }
    public int getTurnoActual() { return turnoActual; }
    public boolean isActiva() { return activa; }
}