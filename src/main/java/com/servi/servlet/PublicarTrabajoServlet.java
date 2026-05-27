package com.servi.servlet;

import com.servi.dao.CategoriaDAO;
import com.servi.dao.TrabajoDAO;
import com.servi.model.Categoria;
import com.servi.model.Trabajo;
import com.servi.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PublicarTrabajoServlet extends HttpServlet {

    private final TrabajoDAO trabajoDAO     = new TrabajoDAO();
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
        req.getRequestDispatcher("/WEB-INF/jsp/publicar-trabajo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        Usuario u = (Usuario) req.getSession().getAttribute("usuario");

        String titulo      = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        String presuStr    = req.getParameter("presupuesto");
        String poblacion   = req.getParameter("poblacion");
        String catStr      = req.getParameter("categoria_id");
        if (catStr == null || catStr.trim().isEmpty()) catStr = "1";

        if (titulo == null || titulo.trim().isEmpty()
                || descripcion == null || descripcion.trim().isEmpty()) {
            req.setAttribute("error", "Completa todos los campos obligatorios.");
            try { req.setAttribute("categorias", categoriaDAO.findAll()); } catch (SQLException ignored) {}
            req.getRequestDispatcher("/WEB-INF/jsp/publicar-trabajo.jsp").forward(req, resp);
            return;
        }

        try {
            Trabajo t = new Trabajo();
            t.setUsuarioId(u.getId());
            t.setCategoriaId(Integer.parseInt(catStr));
            t.setTitulo(titulo.trim());
            t.setDescripcion(descripcion.trim());
            t.setPresupuesto(presuStr != null && !presuStr.isEmpty() ? new BigDecimal(presuStr) : null);
            t.setPoblacion(poblacion != null ? poblacion.trim() : "");
            trabajoDAO.insert(t);
            resp.sendRedirect(req.getContextPath() + "/trabajos");
        } catch (SQLException | NumberFormatException e) {
            req.setAttribute("error", "Error al publicar el trabajo. Comprueba los datos.");
            try { req.setAttribute("categorias", categoriaDAO.findAll()); } catch (SQLException ignored) {}
            req.getRequestDispatcher("/WEB-INF/jsp/publicar-trabajo.jsp").forward(req, resp);
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

