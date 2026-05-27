package com.servi.servlet;

import com.servi.dao.ReservaDAO;
import com.servi.dao.ValoracionDAO;
import com.servi.model.Reserva;
import com.servi.model.Usuario;
import com.servi.model.Valoracion;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

public class ValoracionServlet extends HttpServlet {

    private final ValoracionDAO valoracionDAO = new ValoracionDAO();
    private final ReservaDAO    reservaDAO    = new ReservaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;

        String reservaIdStr = req.getParameter("reservaId");
        if (reservaIdStr == null) { resp.sendRedirect(req.getContextPath() + "/perfil"); return; }

        int usuarioId = (int) req.getSession().getAttribute("usuarioId");

        try {
            Reserva reserva = reservaDAO.findById(Integer.parseInt(reservaIdStr));
            if (reserva == null
                    || (reserva.getClienteId() != usuarioId && reserva.getProveedorId() != usuarioId)
                    || !"finalizado".equalsIgnoreCase(reserva.getEstado())) {
                resp.sendRedirect(req.getContextPath() + "/perfil");
                return;
            }
            if (valoracionDAO.yaValoro(reserva.getId(), usuarioId)) {
                req.setAttribute("error", "Ya has valorado esta reserva.");
            }
            req.setAttribute("reserva", reserva);
            req.getRequestDispatcher("/WEB-INF/jsp/valoracion.jsp").forward(req, resp);
        } catch (SQLException | NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/perfil");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        int usuarioId     = (int) req.getSession().getAttribute("usuarioId");
        String reservaStr  = req.getParameter("reservaId");
        String puntStr     = req.getParameter("puntuacion");
        String comentario  = req.getParameter("comentario");

        try {
            int reservaId = Integer.parseInt(reservaStr);
            Reserva reserva = reservaDAO.findById(reservaId);

            if (reserva == null
                    || (reserva.getClienteId() != usuarioId && reserva.getProveedorId() != usuarioId)
                    || !"finalizado".equalsIgnoreCase(reserva.getEstado())
                    || valoracionDAO.yaValoro(reservaId, usuarioId)) {
                resp.sendRedirect(req.getContextPath() + "/perfil");
                return;
            }

            int valoradoId = (reserva.getClienteId() == usuarioId)
                ? reserva.getProveedorId()
                : reserva.getClienteId();

            Valoracion v = new Valoracion();
            v.setReservaId(reservaId);
            v.setValoradorId(usuarioId);
            v.setValoradoId(valoradoId);
            v.setPuntuacion(Integer.parseInt(puntStr));
            v.setComentario(comentario != null ? comentario.trim() : "");
            valoracionDAO.insert(v);

            resp.sendRedirect(req.getContextPath() + "/perfil?id=" + valoradoId);
        } catch (SQLException | NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/perfil");
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

