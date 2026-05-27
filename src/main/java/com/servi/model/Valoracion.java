package com.servi.model;

import java.sql.Timestamp;

public class Valoracion {
    private int id;
    private int reservaId;
    private int valoradorId;
    private int valoradoId;
    private int puntuacion;
    private String comentario;
    private Timestamp fechaCreacion;

    // JOIN
    private String valoradorNombre;
    private String valoradorAvatar;

    public Valoracion() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservaId() { return reservaId; }
    public void setReservaId(int reservaId) { this.reservaId = reservaId; }

    public int getValoradorId() { return valoradorId; }
    public void setValoradorId(int valoradorId) { this.valoradorId = valoradorId; }

    public int getValoradoId() { return valoradoId; }
    public void setValoradoId(int valoradoId) { this.valoradoId = valoradoId; }

    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getValoradorNombre() { return valoradorNombre; }
    public void setValoradorNombre(String valoradorNombre) { this.valoradorNombre = valoradorNombre; }

    public String getValoradorAvatar() { return valoradorAvatar; }
    public void setValoradorAvatar(String valoradorAvatar) { this.valoradorAvatar = valoradorAvatar; }
}
