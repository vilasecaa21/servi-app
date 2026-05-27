package com.servi.servlet;

import com.servi.dao.UsuarioDAO;
import com.servi.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String nombre    = req.getParameter("nombre");
        String apellidos = req.getParameter("apellidos");
        String email     = req.getParameter("email");
        String password  = req.getParameter("password");
        String password2 = req.getParameter("password2");
        String telefono  = req.getParameter("telefono");
        String poblacion = req.getParameter("poblacion");

        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.length() < 6) {
            req.setAttribute("error", "Rellena todos los campos obligatorios. La contraseña debe tener al menos 6 caracteres.");
            req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
            return;
        }
        if (!password.equals(password2)) {
            req.setAttribute("error", "Las contraseñas no coinciden.");
            req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
            return;
        }

        try {
            if (usuarioDAO.emailExists(email.trim().toLowerCase())) {
                req.setAttribute("error", "Ya existe una cuenta con ese email.");
                req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
                return;
            }

            Usuario u = new Usuario();
            u.setNombre(nombre.trim());
            u.setApellidos(apellidos != null ? apellidos.trim() : "");
            u.setEmail(email.trim().toLowerCase());
            u.setPassword(password);
            u.setTelefono(telefono != null ? telefono.trim() : "");
            u.setPoblacion(poblacion != null ? poblacion.trim() : "");

            int newId = usuarioDAO.insert(u);
            if (newId < 0) throw new SQLException("No se pudo insertar el usuario");

            u.setId(newId);
            HttpSession session = req.getSession(true);
            session.setAttribute("usuario", u);
            session.setAttribute("usuarioId", newId);
            resp.sendRedirect(req.getContextPath() + "/servicios");

        } catch (SQLException e) {
            req.setAttribute("error", "Error al registrar. Inténtalo de nuevo.");
            req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
        }
    }
}

