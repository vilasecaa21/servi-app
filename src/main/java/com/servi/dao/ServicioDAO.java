package com.servi.dao;

import com.servi.model.Servicio;
import com.servi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    // foto_perfil (no avatar), sin tipo_precio, fecha_publicacion (no fecha_creacion)
    private static final String BASE_SELECT =
        "SELECT s.id, s.usuario_id, s.categoria_id, s.titulo, s.descripcion, " +
        "       s.precio, s.poblacion, s.estado, s.fecha_publicacion, " +
        "       u.nombre AS usuario_nombre, u.foto_perfil AS usuario_avatar, " +
        "       c.nombre AS categoria_nombre " +
        "FROM servicios s " +
        "JOIN usuarios u ON s.usuario_id = u.id " +
        "JOIN categorias c ON s.categoria_id = c.id ";

    public List<Servicio> findAll(String busqueda, String poblacion) throws SQLException {
        StringBuilder sql = new StringBuilder(BASE_SELECT).append("WHERE s.estado = 'activo' ");
        List<Object> params = new ArrayList<>();

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append("AND (s.titulo LIKE ? OR s.descripcion LIKE ?) ");
            params.add("%" + busqueda.trim() + "%");
            params.add("%" + busqueda.trim() + "%");
        }
        if (poblacion != null && !poblacion.trim().isEmpty()) {
            sql.append("AND s.poblacion LIKE ? ");
            params.add("%" + poblacion.trim() + "%");
        }
        sql.append("ORDER BY s.fecha_publicacion DESC");

        List<Servicio> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public List<Servicio> findByUsuario(int usuarioId) throws SQLException {
        String sql = BASE_SELECT + "WHERE s.usuario_id = ? ORDER BY s.fecha_publicacion DESC";
        List<Servicio> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public Servicio findById(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE s.id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public int insert(Servicio s) throws SQLException {
        // tipo_precio no existe en la BD
        String sql = "INSERT INTO servicios (usuario_id, categoria_id, titulo, descripcion, precio, poblacion, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'activo')";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getUsuarioId());
            ps.setInt(2, s.getCategoriaId());
            ps.setString(3, s.getTitulo());
            ps.setString(4, s.getDescripcion());
            ps.setBigDecimal(5, s.getPrecio());
            ps.setString(6, s.getPoblacion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    private Servicio mapRow(ResultSet rs) throws SQLException {
        Servicio s = new Servicio();
        s.setId(rs.getInt("id"));
        s.setUsuarioId(rs.getInt("usuario_id"));
        s.setCategoriaId(rs.getInt("categoria_id"));
        s.setTitulo(rs.getString("titulo"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setPrecio(rs.getBigDecimal("precio"));
        s.setPoblacion(rs.getString("poblacion"));
        s.setEstado(rs.getString("estado"));
        s.setFechaCreacion(rs.getTimestamp("fecha_publicacion"));  // columna real: fecha_publicacion
        s.setUsuarioNombre(rs.getString("usuario_nombre"));
        s.setUsuarioAvatar(rs.getString("usuario_avatar"));
        s.setCategoriaNombre(rs.getString("categoria_nombre"));
        return s;
    }
}
