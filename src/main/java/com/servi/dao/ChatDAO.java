package com.servi.dao;

import com.servi.model.Chat;
import com.servi.model.Mensaje;
import com.servi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    public List<Chat> findByUsuario(int usuarioId) throws SQLException {
        // foto_perfil (no avatar), sin ultimo_mensaje (no existe), mensaje (no contenido)
        String sql =
            "SELECT ch.id, ch.usuario1_id, ch.usuario2_id, ch.fecha_creacion, " +
            "  CASE WHEN ch.usuario1_id = ? THEN u2.nombre ELSE u1.nombre END AS otro_nombre, " +
            "  CASE WHEN ch.usuario1_id = ? THEN u2.foto_perfil ELSE u1.foto_perfil END AS otro_avatar, " +
            "  (SELECT m.mensaje FROM mensajes m WHERE m.chat_id = ch.id ORDER BY m.fecha_envio DESC LIMIT 1) AS ultimo_texto " +
            "FROM chats ch " +
            "JOIN usuarios u1 ON ch.usuario1_id = u1.id " +
            "JOIN usuarios u2 ON ch.usuario2_id = u2.id " +
            "WHERE (ch.usuario1_id = ? OR ch.usuario2_id = ?) AND ch.activo = 1 " +
            "ORDER BY ch.fecha_creacion DESC";

        List<Chat> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, usuarioId);
            ps.setInt(3, usuarioId);
            ps.setInt(4, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Chat ch = new Chat();
                    ch.setId(rs.getInt("id"));
                    ch.setUsuario1Id(rs.getInt("usuario1_id"));
                    ch.setUsuario2Id(rs.getInt("usuario2_id"));
                    ch.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    ch.setOtroUsuarioNombre(rs.getString("otro_nombre"));
                    ch.setOtroUsuarioAvatar(rs.getString("otro_avatar"));
                    ch.setUltimoMensajeTexto(rs.getString("ultimo_texto"));
                    lista.add(ch);
                }
            }
        }
        return lista;
    }

    public Chat findOrCreate(int usuario1Id, int usuario2Id) throws SQLException {
        int menor = Math.min(usuario1Id, usuario2Id);
        int mayor = Math.max(usuario1Id, usuario2Id);

        String sqlFind = "SELECT id, usuario1_id, usuario2_id FROM chats WHERE usuario1_id = ? AND usuario2_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sqlFind)) {
            ps.setInt(1, menor);
            ps.setInt(2, mayor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Chat ch = new Chat();
                    ch.setId(rs.getInt("id"));
                    ch.setUsuario1Id(rs.getInt("usuario1_id"));
                    ch.setUsuario2Id(rs.getInt("usuario2_id"));
                    return ch;
                }
            }
        }

        String sqlInsert = "INSERT INTO chats (usuario1_id, usuario2_id) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, menor);
            ps.setInt(2, mayor);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Chat ch = new Chat();
                    ch.setId(rs.getInt(1));
                    ch.setUsuario1Id(menor);
                    ch.setUsuario2Id(mayor);
                    return ch;
                }
            }
        }
        return null;
    }

    public Chat findById(int chatId) throws SQLException {
        String sql = "SELECT id, usuario1_id, usuario2_id FROM chats WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, chatId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Chat ch = new Chat();
                    ch.setId(rs.getInt("id"));
                    ch.setUsuario1Id(rs.getInt("usuario1_id"));
                    ch.setUsuario2Id(rs.getInt("usuario2_id"));
                    return ch;
                }
            }
        }
        return null;
    }

    public List<Mensaje> getMensajes(int chatId) throws SQLException {
        // columna 'mensaje' (no 'contenido'), foto_perfil (no avatar)
        String sql =
            "SELECT m.id, m.chat_id, m.emisor_id, m.mensaje AS contenido, m.leido, m.fecha_envio, " +
            "       u.nombre AS emisor_nombre, u.foto_perfil AS emisor_avatar " +
            "FROM mensajes m JOIN usuarios u ON m.emisor_id = u.id " +
            "WHERE m.chat_id = ? ORDER BY m.fecha_envio ASC";
        List<Mensaje> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, chatId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mensaje m = new Mensaje();
                    m.setId(rs.getInt("id"));
                    m.setChatId(rs.getInt("chat_id"));
                    m.setEmisorId(rs.getInt("emisor_id"));
                    m.setContenido(rs.getString("contenido"));  // alias de 'mensaje'
                    m.setFechaEnvio(rs.getTimestamp("fecha_envio"));
                    m.setLeido(rs.getBoolean("leido"));
                    m.setEmisorNombre(rs.getString("emisor_nombre"));
                    m.setEmisorAvatar(rs.getString("emisor_avatar"));
                    lista.add(m);
                }
            }
        }
        return lista;
    }

    public void insertMensaje(int chatId, int emisorId, String contenido) throws SQLException {
        // columna 'mensaje' (no 'contenido'), sin UPDATE ultimo_mensaje (no existe)
        String sql = "INSERT INTO mensajes (chat_id, emisor_id, mensaje) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, chatId);
            ps.setInt(2, emisorId);
            ps.setString(3, contenido);
            ps.executeUpdate();
        }
    }
}
