<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="m" items="${mensajes}">
    <div class="d-flex ${m.emisorId == usuarioId ? 'justify-content-end' : 'justify-content-start'}">
        <div class="chat-burbuja ${m.emisorId == usuarioId ? 'burbuja-mia' : 'burbuja-otro'}">
            <div class="small"><c:out value="${m.contenido}"/></div>
            <div style="font-size:0.65rem;opacity:0.7;text-align:right;">${m.fechaEnvio}</div>
        </div>
    </div>
</c:forEach>

