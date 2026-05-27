<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Trabajos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
</head>
<body>
<div class="pantalla-movil servicios-screen">

    <div class="d-flex align-items-center justify-content-between p-3 border-bottom bg-white">
        <div class="d-flex align-items-center gap-2">
            <img src="${pageContext.request.contextPath}/assets/img/herramientas.png" style="width:28px;" alt="logo">
            <strong class="subtitulo-servi-outline">SERVI</strong>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-secondary btn-sm">Salir</a>
    </div>

    <form method="get" action="${pageContext.request.contextPath}/trabajos"
          class="p-3 d-flex gap-2 bg-light border-bottom">
        <input type="text" name="q" class="form-control form-control-sm"
               placeholder="Buscar trabajo..." value="<c:out value='${busqueda}'/>">
        <input type="text" name="poblacion" class="form-control form-control-sm"
               placeholder="Población" value="<c:out value='${poblacion}'/>">
        <button class="btn btn-primary btn-sm">&#128269;</button>
    </form>

    <div class="p-3 pb-1 d-flex justify-content-between align-items-center">
        <span class="fw-semibold">Trabajos publicados</span>
        <a href="${pageContext.request.contextPath}/publicar-trabajo"
           class="btn btn-success btn-sm">+ Publicar</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger mx-3 py-2 small"><c:out value="${error}"/></div>
    </c:if>

    <div class="p-3 pt-1" style="padding-bottom:80px !important;">
        <c:choose>
            <c:when test="${empty trabajos}">
                <p class="text-muted text-center mt-4">No se encontraron trabajos.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="t" items="${trabajos}">
                    <a href="${pageContext.request.contextPath}/trabajo?id=${t.id}"
                       class="text-decoration-none text-dark">
                        <div class="servicio-card mb-3">
                            <div class="d-flex align-items-center gap-2 mb-1">
                                <img src="${pageContext.request.contextPath}/assets/img/<c:out value='${not empty t.usuarioAvatar ? t.usuarioAvatar : "usuario.jpg"}'/>
                                    " class="rounded-circle" style="width:36px;height:36px;object-fit:cover;" alt="avatar">
                                <div>
                                    <div class="fw-semibold small"><c:out value="${t.usuarioNombre}"/></div>
                                    <div class="text-muted" style="font-size:0.75rem;"><c:out value="${t.poblacion}"/></div>
                                </div>
                                <span class="badge bg-secondary ms-auto"><c:out value="${t.categoriaNombre}"/></span>
                            </div>
                            <h6 class="mb-1"><c:out value="${t.titulo}"/></h6>
                            <p class="small text-muted mb-1"><c:out value="${t.descripcion}"/></p>
                            <div class="d-flex justify-content-between align-items-center">
                                <c:if test="${not empty t.presupuesto}">
                                    <strong class="text-success">Presupuesto: ${t.presupuesto} €</strong>
                                </c:if>
                                <c:if test="${empty t.presupuesto}">
                                    <strong class="text-success">Presupuesto: a convenir</strong>
                                </c:if>
                                <span class="badge bg-warning text-dark"><c:out value="${t.estado}"/></span>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <nav class="servicios-nav-inferior">
        <a href="${pageContext.request.contextPath}/servicios">
            <span>&#128295;</span><br><small>Servicios</small>
        </a>
        <a href="${pageContext.request.contextPath}/trabajos" class="activo">
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

