<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Servicios</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
</head>
<body>
<div class="pantalla-movil servicios-screen">

    <!-- Cabecera -->
    <div class="d-flex align-items-center justify-content-between p-3 border-bottom bg-white">
        <div class="d-flex align-items-center gap-2">
            <img src="${pageContext.request.contextPath}/assets/img/herramientas.png" style="width:28px;" alt="logo">
            <strong class="subtitulo-servi-outline">SERVI</strong>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-secondary btn-sm">Salir</a>
    </div>

    <!-- Buscador -->
    <form method="get" action="${pageContext.request.contextPath}/servicios"
          class="p-3 d-flex gap-2 bg-light border-bottom">
        <input type="text" name="q" class="form-control form-control-sm"
               placeholder="Buscar servicio..." value="<c:out value='${busqueda}'/>">
        <input type="text" name="poblacion" class="form-control form-control-sm"
               placeholder="Población" value="<c:out value='${poblacion}'/>">
        <button class="btn btn-primary btn-sm">&#128269;</button>
    </form>

    <!-- Botón publicar -->
    <div class="p-3 pb-1 d-flex justify-content-between align-items-center">
        <span class="fw-semibold">Servicios disponibles</span>
        <a href="${pageContext.request.contextPath}/publicar-servicio"
           class="btn btn-success btn-sm">+ Publicar</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger mx-3 py-2 small"><c:out value="${error}"/></div>
    </c:if>

    <!-- Lista de servicios -->
    <div class="p-3 pt-1" style="padding-bottom: 80px !important;">
        <c:choose>
            <c:when test="${empty servicios}">
                <p class="text-muted text-center mt-4">No se encontraron servicios.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="s" items="${servicios}">
                    <a href="${pageContext.request.contextPath}/servicio?id=${s.id}"
                       class="text-decoration-none text-dark">
                        <div class="servicio-card mb-3">
                            <div class="d-flex align-items-center gap-2 mb-1">
                                <img src="${pageContext.request.contextPath}/assets/img/<c:out value='${not empty s.usuarioAvatar ? s.usuarioAvatar : "usuario.jpg"}'/>
                                    " class="rounded-circle" style="width:36px;height:36px;object-fit:cover;" alt="avatar">
                                <div>
                                    <div class="fw-semibold small"><c:out value="${s.usuarioNombre}"/></div>
                                    <div class="text-muted" style="font-size:0.75rem;"><c:out value="${s.poblacion}"/></div>
                                </div>
                                <span class="badge bg-secondary ms-auto"><c:out value="${s.categoriaNombre}"/></span>
                            </div>
                            <h6 class="mb-1"><c:out value="${s.titulo}"/></h6>
                            <p class="small text-muted mb-1"><c:out value="${s.descripcion}"/></p>
                            <div class="d-flex justify-content-between align-items-center">
                                <strong class="text-primary">${s.precio} €</strong>
                                <span class="badge bg-success"><c:out value="${s.estado}"/></span>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Nav inferior -->
    <nav class="servicios-nav-inferior">
        <a href="${pageContext.request.contextPath}/servicios" class="activo">
            <span>&#128295;</span><br><small>Servicios</small>
        </a>
        <a href="${pageContext.request.contextPath}/trabajos">
            <span>&#128188;</span><br><small>Trabajos</small>
        </a>
        <a href="${pageContext.request.contextPath}/chat">
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
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>

