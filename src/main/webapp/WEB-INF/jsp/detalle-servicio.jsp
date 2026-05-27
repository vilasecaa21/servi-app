<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – <c:out value="${servicio.titulo}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
</head>
<body>
<div class="pantalla-movil servicios-screen">

    <div class="d-flex align-items-center p-3 border-bottom bg-white gap-2">
        <a href="${pageContext.request.contextPath}/servicios" class="btn btn-outline-secondary btn-sm">&#8592;</a>
        <strong>Detalle del Servicio</strong>
    </div>

    <div class="p-3" style="padding-bottom:80px !important;">

        <!-- Info del proveedor -->
        <div class="d-flex align-items-center gap-3 mb-3">
            <img src="${pageContext.request.contextPath}/assets/img/<c:out value='${not empty proveedor.avatar ? proveedor.avatar : "usuario.jpg"}'/>
                " class="rounded-circle border" style="width:56px;height:56px;object-fit:cover;" alt="av">
            <div>
                <div class="fw-bold"><c:out value="${proveedor.nombreCompleto}"/></div>
                <div class="text-muted small"><c:out value="${proveedor.poblacion}"/></div>
                <div class="small">
                    <c:forEach begin="1" end="5" var="i">
                        <c:choose>
                            <c:when test="${i <= proveedor.valoracionMedia}">&#11088;</c:when>
                            <c:otherwise><span style="opacity:0.3">&#11088;</span></c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <span class="text-muted">(${proveedor.totalValoraciones})</span>
                </div>
            </div>
        </div>

        <!-- Datos del servicio -->
        <div class="servicio-card mb-3">
            <span class="badge bg-secondary mb-2"><c:out value="${servicio.categoriaNombre}"/></span>
            <h5><c:out value="${servicio.titulo}"/></h5>
            <p class="text-muted small"><c:out value="${servicio.descripcion}"/></p>
            <div class="d-flex justify-content-between align-items-center">
                <h4 class="text-primary mb-0">${servicio.precio} €</h4>
                <span class="badge bg-success"><c:out value="${servicio.estado}"/></span>
            </div>
            <div class="text-muted small mt-1">&#128205; <c:out value="${servicio.poblacion}"/></div>
        </div>

        <!-- Botón contactar -->
        <a href="${pageContext.request.contextPath}/chat?con=${proveedor.id}"
           class="btn btn-primary w-100 mb-3">&#128172; Contactar por chat</a>
        <a href="${pageContext.request.contextPath}/perfil?id=${proveedor.id}"
           class="btn btn-outline-secondary w-100 mb-3">&#128100; Ver perfil del profesional</a>

        <!-- Valoraciones -->
        <h6 class="fw-bold mb-2">&#11088; Valoraciones del profesional</h6>
        <c:choose>
            <c:when test="${empty valoraciones}">
                <p class="text-muted small">Sin valoraciones todavía.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="v" items="${valoraciones}">
                    <div class="servicio-card mb-2 p-2">
                        <div class="d-flex align-items-center gap-2 mb-1">
                            <img src="${pageContext.request.contextPath}/assets/img/<c:out value='${not empty v.valoradorAvatar ? v.valoradorAvatar : "usuario.jpg"}'/>
                                " class="rounded-circle" style="width:28px;height:28px;object-fit:cover;" alt="av">
                            <span class="small fw-semibold"><c:out value="${v.valoradorNombre}"/></span>
                            <span class="ms-auto small">
                                <c:forEach begin="1" end="${v.puntuacion}" var="i">&#11088;</c:forEach>
                            </span>
                        </div>
                        <p class="small text-muted mb-0"><c:out value="${v.comentario}"/></p>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

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

