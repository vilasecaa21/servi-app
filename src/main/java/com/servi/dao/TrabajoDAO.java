package com.servi.dao;

import com.servi.model.Trabajo;
import com.servi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrabajoDAO {

    // foto_perfil (no avatar), fecha_publicacion (no fecha_creacion)
    private static final String BASE_SELECT =
        "SELECT t.id, t.usuario_id, t.categoria_id, t.titulo, t.descripcion, " +
        "       t.presupuesto, t.poblacion, t.estado, t.fecha_publicacion, " +
        "       u.nombre AS usuario_nombre, u.foto_perfil AS usuario_avatar, " +
        "       c.nombre AS categoria_nombre " +
        "FROM trabajos t " +
        "JOIN usuarios u ON t.usuario_id = u.id " +
        "JOIN categorias c ON t.categoria_id = c.id ";

    public List<Trabajo> findAll(String busqueda, String poblacion) throws SQLException {
        StringBuilder sql = new StringBuilder(BASE_SELECT).append("WHERE t.estado = 'activo' ");
        List<Object> params = new ArrayList<>();

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append("AND (t.titulo LIKE ? OR t.descripcion LIKE ?) ");
            params.add("%" + busqueda.trim() + "%");
            params.add("%" + busqueda.trim() + "%");
        }
        if (poblacion != null && !poblacion.trim().isEmpty()) {
            sql.append("AND t.poblacion LIKE ? ");
            params.add("%" + poblacion.trim() + "%");
        }
        sql.append("ORDER BY t.fecha_publicacion DESC");

        List<Trabajo> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public List<Trabajo> findByUsuario(int usuarioId) throws SQLException {
        String sql = BASE_SELECT + "WHERE t.usuario_id = ? ORDER BY t.fecha_publicacion DESC";
        List<Trabajo> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public Trabajo findById(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE t.id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public int insert(Trabajo t) throws SQLException {
        String sql = "INSERT INTO trabajos (usuario_id, categoria_id, titulo, descripcion, presupuesto, poblacion, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'activo')";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getUsuarioId());
            ps.setInt(2, t.getCategoriaId());
            ps.setString(3, t.getTitulo());
            ps.setString(4, t.getDescripcion());
            ps.setBigDecimal(5, t.getPresupuesto());
            ps.setString(6, t.getPoblacion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    private Trabajo mapRow(ResultSet rs) throws SQLException {
        Trabajo t = new Trabajo();
        t.setId(rs.getInt("id"));
        t.setUsuarioId(rs.getInt("usuario_id"));
        t.setCategoriaId(rs.getInt("categoria_id"));
        t.setTitulo(rs.getString("titulo"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setPresupuesto(rs.getBigDecimal("presupuesto"));
        t.setPoblacion(rs.getString("poblacion"));
        t.setEstado(rs.getString("estado"));
        t.setFechaCreacion(rs.getTimestamp("fecha_publicacion"));  // columna real: fecha_publicacion
        t.setUsuarioNombre(rs.getString("usuario_nombre"));
        t.setUsuarioAvatar(rs.getString("usuario_avatar"));
        t.setCategoriaNombre(rs.getString("categoria_nombre"));
        return t;
    }
}
