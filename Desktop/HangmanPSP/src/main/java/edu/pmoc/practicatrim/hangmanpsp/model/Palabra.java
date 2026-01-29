package edu.pmoc.practicatrim.hangmanpsp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "palabras")
public class Palabra {
    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "palabra", length = 15, nullable = false)
    public String palabra;

    public Palabra() {}

    public Palabra(int id , String palabra) {
        this.id = id;
        this.palabra = palabra;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPalabra() {
        return palabra;
    }
    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }
    @Override
    public String toString() {
        return "Palabra [id=" + id + ", texto=" + palabra + "]";
    }

}
