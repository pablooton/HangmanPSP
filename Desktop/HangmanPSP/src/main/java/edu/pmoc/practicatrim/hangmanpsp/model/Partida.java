package edu.pmoc.practicatrim.hangmanpsp.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "partidas")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "jugador_id")
    private Jugador jugador;
    @Column(name = "fecha_hora", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date fechaHora;
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

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
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
