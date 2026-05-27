package com.servi.servlet;

import com.servi.dao.ServicioDAO;
import com.servi.dao.UsuarioDAO;
import com.servi.dao.ValoracionDAO;
import com.servi.model.Servicio;
import com.servi.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

public class DetalleServicioServlet extends HttpServlet {

    private final ServicioDAO   servicioDAO   = new ServicioDAO();
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
        if (idStr == null) { resp.sendRedirect(req.getContextPath() + "/servicios"); return; }

        try {
            Servicio servicio = servicioDAO.findById(Integer.parseInt(idStr));
            if (servicio == null) { resp.sendRedirect(req.getContextPath() + "/servicios"); return; }

            Usuario proveedor = usuarioDAO.findById(servicio.getUsuarioId());
            double[] stats = valoracionDAO.getEstadisticas(servicio.getUsuarioId());
            proveedor.setValoracionMedia(stats[0]);
            proveedor.setTotalValoraciones((int) stats[1]);
            req.setAttribute("servicio", servicio);
            req.setAttribute("proveedor", proveedor);
            req.setAttribute("valoraciones", valoracionDAO.findByValorado(servicio.getUsuarioId()));
            req.getRequestDispatcher("/WEB-INF/jsp/detalle-servicio.jsp").forward(req, resp);
        } catch (SQLException | NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/servicios");
        }
    }
}

