package com.servi.servlet;

import com.servi.dao.TrabajoDAO;
import com.servi.model.Trabajo;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TrabajosServlet extends HttpServlet {

    private final TrabajoDAO trabajoDAO = new TrabajoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession(false) == null || req.getSession(false).getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String busqueda  = req.getParameter("q");
        String poblacion = req.getParameter("poblacion");

        try {
            List<Trabajo> trabajos = trabajoDAO.findAll(busqueda, poblacion);
            req.setAttribute("trabajos", trabajos);
            req.setAttribute("busqueda", busqueda);
            req.setAttribute("poblacion", poblacion);
            req.getRequestDispatcher("/WEB-INF/jsp/trabajos.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Error al cargar los trabajos.");
            req.getRequestDispatcher("/WEB-INF/jsp/trabajos.jsp").forward(req, resp);
        }
    }
}

