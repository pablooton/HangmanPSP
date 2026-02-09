package edu.pmoc.practicatrim.hangmanpsp.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "jugador_id")
    private transient Jugador jugador;

    @Column(name = "fecha_partida")
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(name = "acertado")
    private boolean acertado;

    @Column(name = "puntuacion_obtenida")
    private int puntuacionObtenida;

    public Partida() {}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Jugador getJugador() {
        return jugador;
    }
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    public boolean isAcertado() {
        return acertado;
    }
    public void setAcertado(boolean acertado) {
        this.acertado = acertado;
    }
    public int getPuntuacionObtenida() {
        return puntuacionObtenida;
    }
    public void setPuntuacionObtenida(int puntuacionObtenida) {
        this.puntuacionObtenida = puntuacionObtenida;
    }
}