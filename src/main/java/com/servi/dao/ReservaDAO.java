package com.servi.dao;

import com.servi.model.Reserva;
import com.servi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    // precio_acordado (no precio), descripcion_acuerdo (no mensaje),
    // fecha_reserva (no fecha_creacion), autor_id (no valorador_id)
    private static final String BASE_SELECT =
        "SELECT r.id, r.servicio_id, r.cliente_id, r.proveedor_id, " +
        "       r.fecha_servicio, r.estado, r.precio_acordado, r.descripcion_acuerdo, r.fecha_reserva, " +
        "       COALESCE(s.titulo, 'Sin título') AS servicio_titulo, " +
        "       uc.nombre AS cliente_nombre, up.nombre AS proveedor_nombre, " +
        "  (SELECT COUNT(*) FROM valoraciones v WHERE v.reserva_id = r.id AND v.autor_id = r.cliente_id) > 0 AS valorado_cliente, " +
        "  (SELECT COUNT(*) FROM valoraciones v WHERE v.reserva_id = r.id AND v.autor_id = r.proveedor_id) > 0 AS valorado_proveedor " +
        "FROM reservas r " +
        "LEFT JOIN servicios s ON r.servicio_id = s.id " +
        "JOIN usuarios uc ON r.cliente_id = uc.id " +
        "JOIN usuarios up ON r.proveedor_id = up.id ";

    public List<Reserva> findByUsuario(int usuarioId) throws SQLException {
        String sql = BASE_SELECT + "WHERE r.cliente_id = ? OR r.proveedor_id = ? ORDER BY r.fecha_reserva DESC";
        List<Reserva> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public Reserva findById(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE r.id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public int insert(Reserva r) throws SQLException {
        String sql = "INSERT INTO reservas (servicio_id, cliente_id, proveedor_id, fecha_servicio, precio_acordado, descripcion_acuerdo, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'pendiente')";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getServicioId());
            ps.setInt(2, r.getClienteId());
            ps.setInt(3, r.getProveedorId());
            ps.setDate(4, r.getFechaServicio());
            ps.setBigDecimal(5, r.getPrecio());
            ps.setString(6, r.getMensaje());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    private Reserva mapRow(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setId(rs.getInt("id"));
        r.setServicioId(rs.getInt("servicio_id"));
        r.setClienteId(rs.getInt("cliente_id"));
        r.setProveedorId(rs.getInt("proveedor_id"));
        r.setFechaServicio(rs.getDate("fecha_servicio"));
        r.setEstado(rs.getString("estado"));
        r.setPrecio(rs.getBigDecimal("precio_acordado"));
        r.setMensaje(rs.getString("descripcion_acuerdo"));
        r.setFechaCreacion(rs.getTimestamp("fecha_reserva"));
        r.setServicioTitulo(rs.getString("servicio_titulo"));
        r.setClienteNombre(rs.getString("cliente_nombre"));
        r.setProveedorNombre(rs.getString("proveedor_nombre"));
        r.setValoradoPorCliente(rs.getBoolean("valorado_cliente"));
        r.setValoradoPorProveedor(rs.getBoolean("valorado_proveedor"));
        return r;
    }
}
