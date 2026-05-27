package com.servi.dao;

import com.servi.model.Categoria;
import com.servi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> findAll() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categorias ORDER BY nombre";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        }
        return lista;
    }

    public void ensureDefaultCategories() throws SQLException {
        if (count() > 0) return;
        String sql = "INSERT IGNORE INTO categorias (nombre, icono) VALUES " +
                "('Fontanería','🔧'),('Electricidad','⚡'),('Informática','💻')," +
                "('Clases','📚'),('Limpieza','🧹'),('Reformas','🏠')," +
                "('Jardinería','🌱'),('Transporte','🚛'),('Pintura','🎨')," +
                "('Cocina','🍳'),('Belleza','💇'),('Otros','📌')";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    private int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM categorias";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public Categoria findById(int id) throws SQLException {
        String sql = "SELECT id, nombre, descripcion FROM categorias WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    private Categoria mapRow(ResultSet rs) throws SQLException {
        Categoria cat = new Categoria();
        cat.setId(rs.getInt("id"));
        cat.setNombre(rs.getString("nombre"));
        cat.setDescripcion(rs.getString("descripcion"));
        // columna 'icono' no existe en la BD
        return cat;
    }
}
