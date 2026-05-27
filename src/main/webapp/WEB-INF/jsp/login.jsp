<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Acceder</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
</head>
<body>
<div class="pantalla-movil login-screen d-flex flex-column align-items-center justify-content-center">

    <img src="${pageContext.request.contextPath}/assets/img/herramientas.png"
         alt="Logo SERVI" class="logo-servi-outline mb-2" style="width:80px;">
    <h1 class="subtitulo-servi-outline">SERVI</h1>
    <p class="text-muted mb-4 small">El marketplace de servicios locales</p>

    <c:if test="${not empty error}">
        <div class="alert alert-danger w-100 py-2 small"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login" class="formulario-login w-100">
        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" required
                   placeholder="tu@email.com" autocomplete="email">
        </div>
        <div class="mb-3">
            <label class="form-label">Contraseña</label>
            <input type="password" name="password" class="form-control" required
                   placeholder="••••••" autocomplete="current-password">
        </div>
        <button type="submit" class="btn boton-login w-100">Entrar</button>
    </form>

    <p class="mt-3 small">
        ¿No tienes cuenta?
        <a href="${pageContext.request.contextPath}/registro">Regístrate</a>
    </p>
</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>

