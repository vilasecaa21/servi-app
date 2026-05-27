package com.servi.servlet;

import com.servi.dao.TrabajoDAO;
import com.servi.dao.UsuarioDAO;
import com.servi.dao.ValoracionDAO;
import com.servi.model.Trabajo;
import com.servi.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

public class DetalleTrabajoServlet extends HttpServlet {

    private final TrabajoDAO    trabajoDAO    = new TrabajoDAO();
    private final UsuarioDAO    usuarioDAO    = new UsuarioDAO();
    private final ValoracionDAO valoracionDAO = new ValoracionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession(false) == null || req.getSession(false).getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null) { resp.sendRedirect(req.getContextPath() + "/trabajos"); return; }

        try {
            Trabajo trabajo = trabajoDAO.findById(Integer.parseInt(idStr));
            if (trabajo == null) { resp.sendRedirect(req.getContextPath() + "/trabajos"); return; }

            Usuario cliente = usuarioDAO.findById(trabajo.getUsuarioId());
            req.setAttribute("trabajo", trabajo);
            req.setAttribute("cliente", cliente);
            req.setAttribute("valoraciones", valoracionDAO.findByValorado(trabajo.getUsuarioId()));
            req.getRequestDispatcher("/WEB-INF/jsp/detalle-trabajo.jsp").forward(req, resp);
        } catch (SQLException | NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/trabajos");
        }
    }
}

