package edu.pmoc.practicatrim.hangmanpsp.model;

import java.io.Serializable;
import java.util.List;

public class EstadoPartida implements Serializable {
    private static final long serialVersionUID = 1L;
    private String progreso;
    private int vidas;
    private boolean esTuTurno;
    private boolean juegoTerminado;
    private String mensaje;
    private List<Character> letrasAcertadas;

    public EstadoPartida(String progreso, int vidas, boolean esTuTurno, boolean juegoTerminado, String mensaje, List<Character> letrasAcertadas) {
        this.progreso = progreso;
        this.vidas = vidas;
        this.esTuTurno = esTuTurno;
        this.juegoTerminado = juegoTerminado;
        this.mensaje = mensaje;
        this.letrasAcertadas = letrasAcertadas;
    }

    public String getProgreso() { return progreso; }
    public int getVidas() { return vidas; }
    public boolean isEsTuTurno() { return esTuTurno; }
    public boolean isJuegoTerminado() { return juegoTerminado; }
    public String getMensaje() { return mensaje; }
    public List<Character> getLetrasAcertadas() { return letrasAcertadas; }
}