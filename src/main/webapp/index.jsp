<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("usuario") != null) {
        response.sendRedirect(request.getContextPath() + "/servicios");
    } else {
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>
