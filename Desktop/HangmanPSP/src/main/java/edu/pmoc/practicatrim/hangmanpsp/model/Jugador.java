package edu.pmoc.practicatrim.hangmanpsp.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "jugadores")
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL)
    private transient List<Partida> partidas;

    public Jugador() {}

    public Jugador(String nombre) {
        this.nombre = nombre;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<Partida> getPartidas() {
        return partidas;
    }
    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }
}