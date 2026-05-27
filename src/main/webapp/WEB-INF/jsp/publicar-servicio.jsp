<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Publicar Servicio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
</head>
<body>
<div class="pantalla-movil servicios-screen">

    <div class="d-flex align-items-center p-3 border-bottom bg-white gap-2">
        <a href="${pageContext.request.contextPath}/servicios" class="btn btn-outline-secondary btn-sm">&#8592;</a>
        <strong>Publicar Servicio</strong>
    </div>

    <div class="p-3" style="padding-bottom:80px !important;">

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 small"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/publicar-servicio">
            <div class="mb-3">
                <label class="form-label fw-semibold">Título *</label>
                <input type="text" name="titulo" class="form-control" required
                       placeholder="ej: Clases de guitarra">
            </div>
            <div class="mb-3">
                <label class="form-label fw-semibold">Categoría *</label>
                <select name="categoria_id" class="form-select" required>
                    <option value="">-- Selecciona --</option>
                    <c:forEach var="cat" items="${categorias}">
                        <option value="${cat.id}"><c:out value="${cat.nombre}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label fw-semibold">Descripción *</label>
                <textarea name="descripcion" class="form-control" rows="4" required
                          placeholder="Describe tu servicio con detalle..."></textarea>
            </div>
            <div class="row mb-3">
                <div class="col-7">
                    <label class="form-label fw-semibold">Precio (€) *</label>
                    <input type="number" name="precio" class="form-control" required
                           min="0" step="0.01" placeholder="25.00">
                </div>
                <div class="col-5">
                    <label class="form-label fw-semibold">Tipo</label>
                    <select name="tipo_precio" class="form-select">
                        <option value="hora">/ hora</option>
                        <option value="servicio">/ servicio</option>
                        <option value="dia">/ día</option>
                        <option value="mes">/ mes</option>
                    </select>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label fw-semibold">Población</label>
                <input type="text" name="poblacion" class="form-control" placeholder="Tu ciudad">
            </div>
            <button type="submit" class="btn btn-primary w-100">Publicar Servicio</button>
        </form>
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

