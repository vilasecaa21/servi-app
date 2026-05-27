package com.servi.servlet;

import com.servi.dao.UsuarioDAO;
import com.servi.model.Usuario;
import com.servi.util.PasswordUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getSession(false) != null && req.getSession(false).getAttribute("usuario") != null) {
            resp.sendRedirect(req.getContextPath() + "/servicios");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Por favor, completa todos los campos.");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        try {
            String emailNorm = email.trim().toLowerCase();
            Usuario usuario = usuarioDAO.findByEmail(emailNorm);
            System.err.println("[LOGIN] email buscado: '" + emailNorm + "'");
            System.err.println("[LOGIN] usuario encontrado: " + (usuario != null));
            if (usuario != null) {
                String storedHash = usuario.getPassword();
                System.err.println("[LOGIN] hash en BD: " + storedHash);
                boolean match = org.mindrot.jbcrypt.BCrypt.checkpw(password, storedHash);
                System.err.println("[LOGIN] BCrypt.checkpw resultado: " + match);
            }
            if (usuario == null || !PasswordUtil.verify(password, usuario.getPassword())) {
                req.setAttribute("error", "Email o contraseña incorrectos.");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            resp.sendRedirect(req.getContextPath() + "/servicios");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERROR BD: " + e.getMessage() + " / " + e.getClass().getName());
            req.setAttribute("error", "Error de conexión: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR INESPERADO: " + e.getMessage() + " / " + e.getClass().getName());
            req.setAttribute("error", "Error inesperado: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}

