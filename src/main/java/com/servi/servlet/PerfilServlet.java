package com.servi.servlet;

import com.servi.dao.ServicioDAO;
import com.servi.dao.TrabajoDAO;
import com.servi.dao.UsuarioDAO;
import com.servi.dao.ValoracionDAO;
import com.servi.model.Usuario;
import com.servi.util.PasswordUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

public class PerfilServlet extends HttpServlet {

    private final UsuarioDAO    usuarioDAO    = new UsuarioDAO();
    private final ServicioDAO   servicioDAO   = new ServicioDAO();
    private final TrabajoDAO    trabajoDAO    = new TrabajoDAO();
    private final ValoracionDAO valoracionDAO = new ValoracionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;

        int usuarioId = (int) req.getSession().getAttribute("usuarioId");

        // Ver perfil propio o ajeno
        String idParam = req.getParameter("id");
        int perfilId = (idParam != null) ? Integer.parseInt(idParam) : usuarioId;

        try {
            Usuario perfil = usuarioDAO.findById(perfilId);
            if (perfil == null) { resp.sendRedirect(req.getContextPath() + "/servicios"); return; }

            double[] stats = valoracionDAO.getEstadisticas(perfilId);
            perfil.setValoracionMedia(stats[0]);
            perfil.setTotalValoraciones((int) stats[1]);
            req.setAttribute("perfil", perfil);
            req.setAttribute("esPropioPeril", perfilId == usuarioId);
            req.setAttribute("servicios", servicioDAO.findByUsuario(perfilId));
            req.setAttribute("trabajos", trabajoDAO.findByUsuario(perfilId));
            req.setAttribute("valoraciones", valoracionDAO.findByValorado(perfilId));
            req.getRequestDispatcher("/WEB-INF/jsp/perfil.jsp").forward(req, resp);
        } catch (SQLException e) {
            resp.sendRedirect(req.getContextPath() + "/servicios");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        Usuario sesion = (Usuario) req.getSession().getAttribute("usuario");
        String accion  = req.getParameter("accion");

        try {
            if ("editar".equals(accion)) {
                sesion.setNombre(req.getParameter("nombre").trim());
                sesion.setApellidos(req.getParameter("apellidos") != null ? req.getParameter("apellidos").trim() : "");
                sesion.setTelefono(req.getParameter("telefono") != null ? req.getParameter("telefono").trim() : "");
                sesion.setPoblacion(req.getParameter("poblacion") != null ? req.getParameter("poblacion").trim() : "");
                sesion.setDescripcion(req.getParameter("descripcion") != null ? req.getParameter("descripcion").trim() : "");
                usuarioDAO.update(sesion);
                req.getSession().setAttribute("usuario", usuarioDAO.findById(sesion.getId()));
            } else if ("cambiar-password".equals(accion)) {
                String actual  = req.getParameter("password_actual");
                String nueva   = req.getParameter("password_nueva");
                String nueva2  = req.getParameter("password_nueva2");
                if (!PasswordUtil.verify(actual, sesion.getPassword())) {
                    req.setAttribute("errorPass", "La contraseña actual es incorrecta.");
                } else if (!nueva.equals(nueva2) || nueva.length() < 6) {
                    req.setAttribute("errorPass", "La nueva contraseña no coincide o es demasiado corta.");
                } else {
                    usuarioDAO.updatePassword(sesion.getId(), nueva);
                    req.getSession().setAttribute("usuario", usuarioDAO.findById(sesion.getId()));
                    req.setAttribute("okPass", "Contraseña actualizada correctamente.");
                }
            }

            // Recargar perfil completo
            int uid = sesion.getId();
            Usuario perfil = usuarioDAO.findById(uid);
            double[] stats2 = valoracionDAO.getEstadisticas(uid);
            perfil.setValoracionMedia(stats2[0]);
            perfil.setTotalValoraciones((int) stats2[1]);
            req.setAttribute("perfil", perfil);
            req.setAttribute("esPropioPeril", true);
            req.setAttribute("servicios", servicioDAO.findByUsuario(uid));
            req.setAttribute("trabajos", trabajoDAO.findByUsuario(uid));
            req.setAttribute("valoraciones", valoracionDAO.findByValorado(uid));
            req.getRequestDispatcher("/WEB-INF/jsp/perfil.jsp").forward(req, resp);
        } catch (SQLException e) {
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

