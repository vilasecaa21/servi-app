package com.servi.dao;

import com.servi.model.WalletMovimiento;
import com.servi.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WalletDAO {

    public List<WalletMovimiento> findByUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM wallet_movimientos WHERE usuario_id = ? ORDER BY fecha_movimiento DESC";
        List<WalletMovimiento> lista = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WalletMovimiento m = new WalletMovimiento();
                    m.setId(rs.getInt("id"));
                    m.setUsuarioId(rs.getInt("usuario_id"));
                    m.setTipo(rs.getString("tipo"));
                    m.setImporte(rs.getBigDecimal("importe"));
                    m.setConcepto(rs.getString("concepto"));
                    m.setFechaMovimiento(rs.getTimestamp("fecha_movimiento"));
                    lista.add(m);
                }
            }
        }
        return lista;
    }

    /** Calcula el saldo sumando ingresos y restando retiros/comisiones. */
    public BigDecimal getBalance(int usuarioId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(CASE WHEN tipo='ingreso' THEN importe ELSE -importe END), 0) " +
                     "FROM wallet_movimientos WHERE usuario_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    public void addMovimiento(int usuarioId, String tipo, BigDecimal importe, String concepto) throws SQLException {
        // tipos válidos en BD: 'ingreso', 'retiro', 'comision', 'ajuste'
        String sql = "INSERT INTO wallet_movimientos (usuario_id, tipo, importe, concepto) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, tipo);
            ps.setBigDecimal(3, importe);
            ps.setString(4, concepto);
            ps.executeUpdate();
        }
    }
}
