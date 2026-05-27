<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Valorar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
    <style>
        .star-rating input { display:none; }
        .star-rating label { font-size:2rem; cursor:pointer; color:#ccc; }
        .star-rating input:checked ~ label,
        .star-rating label:hover,
        .star-rating label:hover ~ label { color:#ffc107; }
        .star-rating { display:flex; flex-direction:row-reverse; justify-content:center; }
    </style>
</head>
<body>
<div class="pantalla-movil login-screen d-flex flex-column align-items-center justify-content-center p-4">

    <h4 class="mb-1">&#11088; Valorar servicio</h4>
    <p class="text-muted small mb-3"><c:out value="${reserva.servicioTitulo}"/></p>

    <c:if test="${not empty error}">
        <div class="alert alert-warning w-100 py-2 small"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/valoracion" class="w-100">
        <input type="hidden" name="reservaId" value="${reserva.id}">

        <!-- Estrellas interactivas -->
        <div class="star-rating mb-3" id="stars">
            <input type="radio" name="puntuacion" id="s5" value="5" required>
            <label for="s5">&#11088;</label>
            <input type="radio" name="puntuacion" id="s4" value="4">
            <label for="s4">&#11088;</label>
            <input type="radio" name="puntuacion" id="s3" value="3">
            <label for="s3">&#11088;</label>
            <input type="radio" name="puntuacion" id="s2" value="2">
            <label for="s2">&#11088;</label>
            <input type="radio" name="puntuacion" id="s1" value="1">
            <label for="s1">&#11088;</label>
        </div>

        <div class="mb-3">
            <label class="form-label">Comentario</label>
            <textarea name="comentario" class="form-control" rows="4"
                      placeholder="Describe tu experiencia..."></textarea>
        </div>

        <button type="submit" class="btn btn-warning w-100 fw-bold">Enviar valoración</button>
        <a href="${pageContext.request.contextPath}/perfil" class="btn btn-outline-secondary w-100 mt-2">Cancelar</a>
    </form>
</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>

