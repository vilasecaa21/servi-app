package com.servi.servlet;

import com.servi.dao.CategoriaDAO;
import com.servi.dao.ServicioDAO;
import com.servi.model.Categoria;
import com.servi.model.Servicio;
import com.servi.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PublicarServicioServlet extends HttpServlet {

    private final ServicioDAO servicioDAO   = new ServicioDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    public void init() throws ServletException {
        try { categoriaDAO.ensureDefaultCategories(); } catch (SQLException ignored) {}
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;
        List<Categoria> cats = Collections.emptyList();
        try { cats = categoriaDAO.findAll(); } catch (SQLException ignored) {}
        req.setAttribute("categorias", cats);
        req.getRequestDispatcher("/WEB-INF/jsp/publicar-servicio.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        Usuario u = (Usuario) req.getSession().getAttribute("usuario");

        String titulo      = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        String precioStr   = req.getParameter("precio");
        String tipoPrecio  = req.getParameter("tipo_precio");
        String poblacion   = req.getParameter("poblacion");
        String catStr      = req.getParameter("categoria_id");
        if (catStr == null || catStr.trim().isEmpty()) catStr = "1";

        if (titulo == null || titulo.trim().isEmpty()
                || descripcion == null || descripcion.trim().isEmpty()
                || precioStr == null) {
            req.setAttribute("error", "Completa todos los campos obligatorios.");
            try {
                req.setAttribute("categorias", categoriaDAO.findAll());
            } catch (SQLException ignored) {}
            req.getRequestDispatcher("/WEB-INF/jsp/publicar-servicio.jsp").forward(req, resp);
            return;
        }

        try {
            Servicio s = new Servicio();
            s.setUsuarioId(u.getId());
            s.setCategoriaId(Integer.parseInt(catStr));
            s.setTitulo(titulo.trim());
            s.setDescripcion(descripcion.trim());
            s.setPrecio(new BigDecimal(precioStr));
            s.setTipoPrecio(tipoPrecio != null ? tipoPrecio : "hora");
            s.setPoblacion(poblacion != null ? poblacion.trim() : "");
            servicioDAO.insert(s);
            resp.sendRedirect(req.getContextPath() + "/servicios");
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error al publicar el servicio. Comprueba los datos.");
            try { req.setAttribute("categorias", categoriaDAO.findAll()); } catch (SQLException ignored) {}
            req.getRequestDispatcher("/WEB-INF/jsp/publicar-servicio.jsp").forward(req, resp);
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

