<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Chat</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <style>
        .chat-burbuja { max-width: 75%; border-radius: 18px; padding: 8px 14px; margin-bottom: 6px; }
        .burbuja-mia  { background: #0d6efd; color:#fff; margin-left: auto; }
        .burbuja-otro { background: #e9ecef; color:#222; }
        .chat-box     { overflow-y: auto; height: calc(100vh - 220px); padding: 12px; }
        .chat-lista-item { cursor:pointer; border-bottom:1px solid #f0f0f0; }
        .chat-lista-item:hover { background:#f8f9fa; }
    </style>
</head>
<body>
<div class="pantalla-movil servicios-screen">

    <div class="d-flex align-items-center justify-content-between p-3 border-bottom bg-white">
        <c:choose>
            <c:when test="${not empty chat}">
                <div class="d-flex align-items-center gap-2">
                    <a href="${pageContext.request.contextPath}/chat" class="btn btn-outline-secondary btn-sm">&#8592;</a>
                    <img src="${pageContext.request.contextPath}/assets/img/<c:out value='${not empty otroUsuario.avatar ? otroUsuario.avatar : "usuario.jpg"}'/>
                        " class="rounded-circle" style="width:34px;height:34px;object-fit:cover;" alt="av">
                    <strong class="small"><c:out value="${otroUsuario.nombreCompleto}"/></strong>
                </div>
            </c:when>
            <c:otherwise>
                <strong class="subtitulo-servi-outline">Mensajes</strong>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- VISTA: lista de chats -->
    <c:if test="${empty chat}">
        <div style="padding-bottom:80px;">
            <c:choose>
                <c:when test="${empty chats}">
                    <p class="text-muted text-center mt-5 small">No tienes conversaciones todavía.<br>
                        Contacta con alguien desde un servicio.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="ch" items="${chats}">
                        <a href="${pageContext.request.contextPath}/chat?chatId=${ch.id}"
                           class="text-decoration-none text-dark d-flex align-items-center p-3 chat-lista-item gap-3">
                            <img src="${pageContext.request.contextPath}/assets/img/<c:out value='${not empty ch.otroUsuarioAvatar ? ch.otroUsuarioAvatar : "usuario.jpg"}'/>
                                " class="rounded-circle" style="width:44px;height:44px;object-fit:cover;" alt="av">
                            <div class="flex-grow-1 overflow-hidden">
                                <div class="fw-semibold small"><c:out value="${ch.otroUsuarioNombre}"/></div>
                                <div class="text-muted small text-truncate"><c:out value="${ch.ultimoMensajeTexto}"/></div>
                            </div>
                        </a>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <!-- VISTA: conversación -->
    <c:if test="${not empty chat}">
        <div class="chat-box" id="chatBox">
            <c:forEach var="m" items="${mensajes}">
                <div class="d-flex ${m.emisorId == usuarioId ? 'justify-content-end' : 'justify-content-start'}">
                    <div class="chat-burbuja ${m.emisorId == usuarioId ? 'burbuja-mia' : 'burbuja-otro'}">
                        <div class="small"><c:out value="${m.contenido}"/></div>
                        <div style="font-size:0.65rem;opacity:0.7;text-align:right;">${m.fechaEnvio}</div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Input mensaje -->
        <div class="border-top bg-white p-2 d-flex gap-2" style="position:sticky;bottom:60px;">
            <input type="text" id="inputMensaje" class="form-control form-control-sm"
                   placeholder="Escribe un mensaje..." autocomplete="off">
            <button onclick="enviarMensaje()" class="btn btn-primary btn-sm">&#10148;</button>
        </div>
    </c:if>

    <nav class="servicios-nav-inferior">
        <a href="${pageContext.request.contextPath}/servicios">
            <span>&#128295;</span><br><small>Servicios</small>
        </a>
        <a href="${pageContext.request.contextPath}/trabajos">
            <span>&#128188;</span><br><small>Trabajos</small>
        </a>
        <a href="${pageContext.request.contextPath}/chat" class="activo">
            <span>&#128172;</span><br><small>Chat</small>
        </a>
        <a href="${pageContext.request.contextPath}/wallet">
            <span>&#128181;</span><br><small>Wallet</small>
        </a>
        <a href="${pageContext.request.contextPath}/perfil">
            <span>&#128100;</span><br><small>Perfil</small>
        </a>
    </nav>
</div>

<c:if test="${not empty chat}">
<script>
    const CHAT_ID   = ${chat.id};
    const CTX       = '${pageContext.request.contextPath}';
    const USUARIO_ID = ${usuarioId};

    function scrollAbajo() {
        const box = document.getElementById('chatBox');
        if (box) box.scrollTop = box.scrollHeight;
    }
    scrollAbajo();

    function enviarMensaje() {
        const input    = document.getElementById('inputMensaje');
        const contenido = input.value.trim();
        if (!contenido) return;
        input.value = '';
        fetch(CTX + '/chat', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'chatId=' + CHAT_ID + '&contenido=' + encodeURIComponent(contenido)
        }).then(() => cargarMensajes());
    }

    document.getElementById('inputMensaje').addEventListener('keydown', function(e) {
        if (e.key === 'Enter') { e.preventDefault(); enviarMensaje(); }
    });

    function cargarMensajes() {
        fetch(CTX + '/chat?chatId=' + CHAT_ID + '&mensajes=1')
            .then(r => r.text())
            .then(html => {
                document.getElementById('chatBox').innerHTML = html;
                scrollAbajo();
            });
    }

    // Polling cada 3 segundos
    setInterval(cargarMensajes, 3000);
</script>
</c:if>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>

