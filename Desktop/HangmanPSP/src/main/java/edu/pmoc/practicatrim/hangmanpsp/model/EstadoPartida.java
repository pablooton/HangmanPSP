package edu.pmoc.practicatrim.hangmanpsp.model;

import java.io.Serializable;

public class EstadoPartida implements Serializable {
    private static final long serialVersionUID = 1L;

    private String progreso;
    private int vidas;
    private boolean esTuTurno;
    private boolean juegoTerminado;
    private String mensaje;

    public EstadoPartida(String progreso, int vidas, boolean esTuTurno, boolean juegoTerminado, String mensaje) {
        this.progreso = progreso;
        this.vidas = vidas;
        this.esTuTurno = esTuTurno;
        this.juegoTerminado = juegoTerminado;
        this.mensaje = mensaje;
    }
    public String getProgreso() { return progreso; }
    public int getVidas() { return vidas; }
    public boolean isEsTuTurno() { return esTuTurno; }
    public boolean isJuegoTerminado() { return juegoTerminado; }
    public String getMensaje() { return mensaje; }
}