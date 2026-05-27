package com.servi.servlet;

import com.servi.dao.UsuarioDAO;
import com.servi.dao.WalletDAO;
import com.servi.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class WalletServlet extends HttpServlet {

    private final WalletDAO  walletDAO  = new WalletDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;
        int usuarioId = (int) req.getSession().getAttribute("usuarioId");
        try {
            Usuario u = usuarioDAO.findById(usuarioId);
            req.setAttribute("usuario", u);
            req.setAttribute("balance", walletDAO.getBalance(usuarioId));
            req.setAttribute("movimientos", walletDAO.findByUsuario(usuarioId));
            req.getRequestDispatcher("/WEB-INF/jsp/wallet.jsp").forward(req, resp);
        } catch (SQLException e) {
            resp.sendRedirect(req.getContextPath() + "/servicios");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        int usuarioId  = (int) req.getSession().getAttribute("usuarioId");
        String accion  = req.getParameter("accion");
        String impStr  = req.getParameter("importe");

        try {
            BigDecimal importe = new BigDecimal(impStr);
            if (importe.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();

            if ("recargar".equals(accion)) {
                walletDAO.addMovimiento(usuarioId, "ingreso", importe, "Recarga de saldo");
                req.setAttribute("ok", "Saldo recargado correctamente.");
            } else if ("retirar".equals(accion)) {
                BigDecimal balance = walletDAO.getBalance(usuarioId);
                if (balance.compareTo(importe) < 0) {
                    req.setAttribute("error", "Saldo insuficiente.");
                } else {
                    walletDAO.addMovimiento(usuarioId, "retiro", importe, "Retirada de saldo");
                    req.setAttribute("ok", "Retirada realizada correctamente.");
                }
            }

            req.getSession().setAttribute("usuario", usuarioDAO.findById(usuarioId));

            Usuario u2 = usuarioDAO.findById(usuarioId);
            req.setAttribute("usuario", u2);
            req.setAttribute("balance", walletDAO.getBalance(usuarioId));
            req.setAttribute("movimientos", walletDAO.findByUsuario(usuarioId));
            req.getRequestDispatcher("/WEB-INF/jsp/wallet.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Importe no válido.");
            try {
                req.setAttribute("usuario", usuarioDAO.findById(usuarioId));
                req.setAttribute("movimientos", walletDAO.findByUsuario(usuarioId));
            } catch (SQLException ignored) {}
            req.getRequestDispatcher("/WEB-INF/jsp/wallet.jsp").forward(req, resp);
        } catch (SQLException e) {
            resp.sendRedirect(req.getContextPath() + "/wallet");
        }
    }

    private boolean checkSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getSession(false) == null || req.getSession(false).getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}

