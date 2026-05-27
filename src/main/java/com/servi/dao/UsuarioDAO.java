package com.servi.dao;

import com.servi.model.Usuario;
import com.servi.util.DBConnection;
import com.servi.util.PasswordUtil;

import java.sql.*;

public class UsuarioDAO {

    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND activo = 1";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public Usuario findById(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public int insert(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellidos, email, password_hash, telefono, poblacion, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 1)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getEmail());
            ps.setString(4, PasswordUtil.hash(u.getPassword()));
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getPoblacion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void update(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, telefono=?, poblacion=?, descripcion=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellidos());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getPoblacion());
            ps.setString(5, u.getDescripcion());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        }
    }

    public void updatePassword(int userId, String newPlainPassword) throws SQLException {
        String sql = "UPDATE usuarios SET password_hash=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hash(newPlainPassword));
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setApellidos(rs.getString("apellidos"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password_hash"));
        u.setTelefono(rs.getString("telefono"));
        u.setPoblacion(rs.getString("poblacion"));
        u.setDescripcion(rs.getString("descripcion"));
        u.setAvatar(rs.getString("foto_perfil"));       // columna real: foto_perfil
        u.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        u.setActivo(rs.getBoolean("activo"));
        return u;
    }
}
