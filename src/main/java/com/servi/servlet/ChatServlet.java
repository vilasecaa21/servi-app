package com.servi.servlet;

import com.servi.dao.ChatDAO;
import com.servi.dao.UsuarioDAO;
import com.servi.model.Chat;
import com.servi.model.Mensaje;
import com.servi.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class ChatServlet extends HttpServlet {

    private final ChatDAO    chatDAO    = new ChatDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;

        int usuarioId = (int) req.getSession().getAttribute("usuarioId");
        String chatIdParam    = req.getParameter("chatId");
        String conUsuarioParam = req.getParameter("con");
        String soloMensajes   = req.getParameter("mensajes"); // polling

        try {
            // Polling AJAX: devuelve solo los mensajes en HTML parcial
            if (soloMensajes != null && chatIdParam != null) {
                int chatId = Integer.parseInt(chatIdParam);
                Chat chat  = chatDAO.findById(chatId);
                if (chat == null
                        || (chat.getUsuario1Id() != usuarioId && chat.getUsuario2Id() != usuarioId)) {
                    resp.sendError(403);
                    return;
                }
                List<Mensaje> mensajes = chatDAO.getMensajes(chatId);
                req.setAttribute("mensajes", mensajes);
                req.setAttribute("usuarioId", usuarioId);
                req.getRequestDispatcher("/WEB-INF/jsp/chat-mensajes.jsp").forward(req, resp);
                return;
            }

            // Abrir/crear chat con otro usuario
            if (conUsuarioParam != null) {
                int otroId = Integer.parseInt(conUsuarioParam);
                Chat chat  = chatDAO.findOrCreate(usuarioId, otroId);
                resp.sendRedirect(req.getContextPath() + "/chat?chatId=" + chat.getId());
                return;
            }

            // Lista de chats
            if (chatIdParam != null) {
                int chatId = Integer.parseInt(chatIdParam);
                Chat chat  = chatDAO.findById(chatId);
                if (chat == null
                        || (chat.getUsuario1Id() != usuarioId && chat.getUsuario2Id() != usuarioId)) {
                    resp.sendRedirect(req.getContextPath() + "/chat");
                    return;
                }
                int otroId   = (chat.getUsuario1Id() == usuarioId) ? chat.getUsuario2Id() : chat.getUsuario1Id();
                Usuario otro = usuarioDAO.findById(otroId);
                req.setAttribute("chat", chat);
                req.setAttribute("otroUsuario", otro);
                req.setAttribute("mensajes", chatDAO.getMensajes(chatId));
                req.setAttribute("chats", chatDAO.findByUsuario(usuarioId));
                req.setAttribute("usuarioId", usuarioId);
                req.getRequestDispatcher("/WEB-INF/jsp/chat.jsp").forward(req, resp);
                return;
            }

            // Solo la lista
            req.setAttribute("chats", chatDAO.findByUsuario(usuarioId));
            req.setAttribute("usuarioId", usuarioId);
            req.getRequestDispatcher("/WEB-INF/jsp/chat.jsp").forward(req, resp);

        } catch (SQLException | NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/chat");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!checkSession(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        int usuarioId  = (int) req.getSession().getAttribute("usuarioId");
        String chatIdStr = req.getParameter("chatId");
        String contenido = req.getParameter("contenido");

        if (chatIdStr == null || contenido == null || contenido.trim().isEmpty()) {
            resp.sendError(400);
            return;
        }

        try {
            int chatId = Integer.parseInt(chatIdStr);
            Chat chat  = chatDAO.findById(chatId);
            if (chat == null
                    || (chat.getUsuario1Id() != usuarioId && chat.getUsuario2Id() != usuarioId)) {
                resp.sendError(403);
                return;
            }
            chatDAO.insertMensaje(chatId, usuarioId, contenido.trim());

            // Respuesta JSON simple para AJAX
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print("{\"ok\":true}");
            out.flush();
        } catch (SQLException | NumberFormatException e) {
            resp.sendError(500);
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

