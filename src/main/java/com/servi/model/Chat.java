package com.servi.model;

import java.sql.Timestamp;

public class Chat {
    private int id;
    private int usuario1Id;
    private int usuario2Id;
    private Timestamp fechaCreacion;
    private Timestamp ultimoMensaje;

    // Campos JOIN
    private String otroUsuarioNombre;
    private String otroUsuarioAvatar;
    private String ultimoMensajeTexto;

    public Chat() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuario1Id() { return usuario1Id; }
    public void setUsuario1Id(int usuario1Id) { this.usuario1Id = usuario1Id; }

    public int getUsuario2Id() { return usuario2Id; }
    public void setUsuario2Id(int usuario2Id) { this.usuario2Id = usuario2Id; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getUltimoMensaje() { return ultimoMensaje; }
    public void setUltimoMensaje(Timestamp ultimoMensaje) { this.ultimoMensaje = ultimoMensaje; }

    public String getOtroUsuarioNombre() { return otroUsuarioNombre; }
    public void setOtroUsuarioNombre(String otroUsuarioNombre) { this.otroUsuarioNombre = otroUsuarioNombre; }

    public String getOtroUsuarioAvatar() { return otroUsuarioAvatar; }
    public void setOtroUsuarioAvatar(String otroUsuarioAvatar) { this.otroUsuarioAvatar = otroUsuarioAvatar; }

    public String getUltimoMensajeTexto() { return ultimoMensajeTexto; }
    public void setUltimoMensajeTexto(String ultimoMensajeTexto) { this.ultimoMensajeTexto = ultimoMensajeTexto; }
}
