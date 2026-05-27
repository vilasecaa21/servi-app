<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Registro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
</head>
<body>
<div class="pantalla-movil login-screen d-flex flex-column align-items-center justify-content-center py-4">

    <img src="${pageContext.request.contextPath}/assets/img/herramientas.png"
         alt="Logo SERVI" class="logo-servi-outline mb-2" style="width:60px;">
    <h1 class="subtitulo-servi-outline mb-3">Crear cuenta</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger w-100 py-2 small"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/registro" class="formulario-login w-100">
        <div class="mb-2">
            <label class="form-label">Nombre *</label>
            <input type="text" name="nombre" class="form-control" required placeholder="Tu nombre">
        </div>
        <div class="mb-2">
            <label class="form-label">Apellidos</label>
            <input type="text" name="apellidos" class="form-control" placeholder="Apellidos">
        </div>
        <div class="mb-2">
            <label class="form-label">Email *</label>
            <input type="email" name="email" class="form-control" required placeholder="tu@email.com">
        </div>
        <div class="mb-2">
            <label class="form-label">Contraseña * (mín. 6 caracteres)</label>
            <input type="password" name="password" class="form-control" required minlength="6" placeholder="••••••">
        </div>
        <div class="mb-2">
            <label class="form-label">Repite contraseña *</label>
            <input type="password" name="password2" class="form-control" required minlength="6" placeholder="••••••">
        </div>
        <div class="mb-2">
            <label class="form-label">Teléfono</label>
            <input type="tel" name="telefono" class="form-control" placeholder="6XX XXX XXX">
        </div>
        <div class="mb-3">
            <label class="form-label">Población</label>
            <input type="text" name="poblacion" class="form-control" placeholder="Tu ciudad">
        </div>
        <button type="submit" class="btn boton-login w-100">Crear cuenta</button>
    </form>

    <p class="mt-3 small">
        ¿Ya tienes cuenta?
        <a href="${pageContext.request.contextPath}/login">Acceder</a>
    </p>
</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>

