package com.servi.dao;

import com.servi.model.Valoracion;
import com.servi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ValoracionDAO {

    // autor_id (no valorador_id), destinatario_id (no valorado_id), fecha_valoracion (no fecha_creacion)
    public List<Valoracion> findByValorado(int valoradoId) throws SQLException {
        String sql =
            "SELECT v.id, v.reserva_id, v.autor_id, v.destinatario_id, v.puntuacion, " +
            "       v.comentario, v.fecha_valoracion, " +
            "       u.nombre AS valorador_nombre, u.foto_perfil AS valorador_avatar " +
            "FROM valoraciones v JOIN usuarios u ON v.autor_id = u.id " +
            "WHERE v.destinatario_id = ? ORDER BY v.fecha_valoracion DESC";
        List<Valoracion> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, valoradoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        }
        return lista;
    }

    public boolean yaValoro(int reservaId, int autorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM valoraciones WHERE reserva_id = ? AND autor_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, reservaId);
            ps.setInt(2, autorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void insert(Valoracion v) throws SQLException {
        // autor_id/destinatario_id; no UPDATE a usuarios (columnas no existen)
        String sql = "INSERT INTO valoraciones (reserva_id, autor_id, destinatario_id, puntuacion, comentario) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, v.getReservaId());
            ps.setInt(2, v.getValoradorId());
            ps.setInt(3, v.getValoradoId());
            ps.setInt(4, v.getPuntuacion());
            ps.setString(5, v.getComentario());
            ps.executeUpdate();
        }
    }

    /** Devuelve [valoracion_media, total_valoraciones] calculado desde la tabla. */
    public double[] getEstadisticas(int usuarioId) throws SQLException {
        String sql = "SELECT COALESCE(AVG(puntuacion), 0), COUNT(*) FROM valoraciones WHERE destinatario_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new double[]{rs.getDouble(1), rs.getDouble(2)};
            }
        }
        return new double[]{0, 0};
    }

    private Valoracion mapRow(ResultSet rs) throws SQLException {
        Valoracion v = new Valoracion();
        v.setId(rs.getInt("id"));
        v.setReservaId(rs.getInt("reserva_id"));
        v.setValoradorId(rs.getInt("autor_id"));
        v.setValoradoId(rs.getInt("destinatario_id"));
        v.setPuntuacion(rs.getInt("puntuacion"));
        v.setComentario(rs.getString("comentario"));
        v.setFechaCreacion(rs.getTimestamp("fecha_valoracion"));
        v.setValoradorNombre(rs.getString("valorador_nombre"));
        v.setValoradorAvatar(rs.getString("valorador_avatar"));
        return v;
    }
}
