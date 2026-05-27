package com.servi.servlet;

import com.servi.dao.ServicioDAO;
import com.servi.model.Servicio;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ServiciosServlet extends HttpServlet {

    private final ServicioDAO servicioDAO = new ServicioDAO();

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
            List<Servicio> servicios = servicioDAO.findAll(busqueda, poblacion);
            req.setAttribute("servicios", servicios);
            req.setAttribute("busqueda", busqueda);
            req.setAttribute("poblacion", poblacion);
            req.getRequestDispatcher("/WEB-INF/jsp/servicios.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Error al cargar los servicios.");
            req.getRequestDispatcher("/WEB-INF/jsp/servicios.jsp").forward(req, resp);
        }
    }
}

