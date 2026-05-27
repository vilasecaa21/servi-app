package com.servi.model;

import java.sql.Timestamp;

public class Mensaje {
    private int id;
    private int chatId;
    private int emisorId;
    private String contenido;
    private Timestamp fechaEnvio;
    private boolean leido;

    // JOIN
    private String emisorNombre;
    private String emisorAvatar;

    public Mensaje() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getChatId() { return chatId; }
    public void setChatId(int chatId) { this.chatId = chatId; }

    public int getEmisorId() { return emisorId; }
    public void setEmisorId(int emisorId) { this.emisorId = emisorId; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Timestamp getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Timestamp fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }

    public String getEmisorNombre() { return emisorNombre; }
    public void setEmisorNombre(String emisorNombre) { this.emisorNombre = emisorNombre; }

    public String getEmisorAvatar() { return emisorAvatar; }
    public void setEmisorAvatar(String emisorAvatar) { this.emisorAvatar = emisorAvatar; }
}
