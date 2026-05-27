package com.servi.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Reserva {
    private int id;
    private int servicioId;
    private int clienteId;
    private int proveedorId;
    private Date fechaServicio;
    private String estado;
    private BigDecimal precio;
    private String mensaje;
    private Timestamp fechaCreacion;

    // Campos JOIN
    private String servicioTitulo;
    private String clienteNombre;
    private String proveedorNombre;
    private boolean valoradoPorCliente;
    private boolean valoradoPorProveedor;

    public Reserva() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getServicioId() { return servicioId; }
    public void setServicioId(int servicioId) { this.servicioId = servicioId; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public int getProveedorId() { return proveedorId; }
    public void setProveedorId(int proveedorId) { this.proveedorId = proveedorId; }

    public Date getFechaServicio() { return fechaServicio; }
    public void setFechaServicio(Date fechaServicio) { this.fechaServicio = fechaServicio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getServicioTitulo() { return servicioTitulo; }
    public void setServicioTitulo(String servicioTitulo) { this.servicioTitulo = servicioTitulo; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getProveedorNombre() { return proveedorNombre; }
    public void setProveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; }

    public boolean isValoradoPorCliente() { return valoradoPorCliente; }
    public void setValoradoPorCliente(boolean valoradoPorCliente) { this.valoradoPorCliente = valoradoPorCliente; }

    public boolean isValoradoPorProveedor() { return valoradoPorProveedor; }
    public void setValoradoPorProveedor(boolean valoradoPorProveedor) { this.valoradoPorProveedor = valoradoPorProveedor; }
}
