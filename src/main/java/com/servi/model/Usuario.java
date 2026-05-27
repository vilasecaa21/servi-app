package com.servi.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Usuario {
    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String telefono;
    private String poblacion;
    private String descripcion;
    private String avatar;
    private BigDecimal saldoWallet;
    private double valoracionMedia;
    private int totalValoraciones;
    private Timestamp fechaRegistro;
    private boolean activo;

    public Usuario() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPoblacion() { return poblacion; }
    public void setPoblacion(String poblacion) { this.poblacion = poblacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public BigDecimal getSaldoWallet() { return saldoWallet; }
    public void setSaldoWallet(BigDecimal saldoWallet) { this.saldoWallet = saldoWallet; }

    public double getValoracionMedia() { return valoracionMedia; }
    public void setValoracionMedia(double valoracionMedia) { this.valoracionMedia = valoracionMedia; }

    public int getTotalValoraciones() { return totalValoraciones; }
    public void setTotalValoraciones(int totalValoraciones) { this.totalValoraciones = totalValoraciones; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNombreCompleto() {
        return nombre + (apellidos != null && !apellidos.isEmpty() ? " " + apellidos : "");
    }
}
