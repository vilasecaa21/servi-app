<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Wallet</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
</head>
<body>
<div class="pantalla-movil servicios-screen">

    <div class="d-flex align-items-center justify-content-between p-3 border-bottom bg-white">
        <strong class="subtitulo-servi-outline">&#128181; Mi Wallet</strong>
    </div>

    <div class="p-3" style="padding-bottom:80px !important;">

        <!-- Saldo actual -->
        <div class="card text-center mb-4 border-0 shadow-sm">
            <div class="card-body py-4">
                <div class="text-muted small mb-1">Saldo disponible</div>
                <h2 class="text-primary fw-bold">${balance} €</h2>
            </div>
        </div>

        <!-- Alertas -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 small"><c:out value="${error}"/></div>
        </c:if>
        <c:if test="${not empty ok}">
            <div class="alert alert-success py-2 small"><c:out value="${ok}"/></div>
        </c:if>

        <!-- Recargar / Retirar -->
        <div class="row g-2 mb-4">
            <div class="col-6">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body p-2">
                        <div class="small fw-semibold mb-2 text-success">&#11014; Recargar</div>
                        <form method="post" action="${pageContext.request.contextPath}/wallet">
                            <input type="hidden" name="accion" value="recargar">
                            <input type="number" name="importe" class="form-control form-control-sm mb-2"
                                   placeholder="Importe €" min="1" step="0.01" required>
                            <button type="submit" class="btn btn-success btn-sm w-100">Recargar</button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-6">
                <div class="card border-0 shadow-sm h-100">
                    <div class="card-body p-2">
                        <div class="small fw-semibold mb-2 text-danger">&#11015; Retirar</div>
                        <form method="post" action="${pageContext.request.contextPath}/wallet">
                            <input type="hidden" name="accion" value="retirar">
                            <input type="number" name="importe" class="form-control form-control-sm mb-2"
                                   placeholder="Importe €" min="1" step="0.01" required>
                            <button type="submit" class="btn btn-danger btn-sm w-100">Retirar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Historial de movimientos -->
        <h6 class="fw-bold mb-2">Historial de movimientos</h6>
        <c:choose>
            <c:when test="${empty movimientos}">
                <p class="text-muted small text-center mt-3">Sin movimientos todavía.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="m" items="${movimientos}">
                    <div class="d-flex align-items-center justify-content-between py-2 border-bottom">
                        <div>
                            <div class="small fw-semibold">
                                <c:choose>
                                    <c:when test="${m.tipo == 'ingreso'}">
                                        <span class="text-success">&#11014;</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-danger">&#11015;</span>
                                    </c:otherwise>
                                </c:choose>
                                <c:out value="${m.concepto}"/>
                            </div>
                            <div class="text-muted" style="font-size:0.72rem;">${m.fechaMovimiento}</div>
                        </div>
                        <div class="fw-bold ${m.tipo == 'ingreso' ? 'text-success' : 'text-danger'}">
                            ${m.tipo == 'ingreso' ? '+' : '-'}${m.importe} €
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <nav class="servicios-nav-inferior">
        <a href="${pageContext.request.contextPath}/servicios">
            <span>&#128295;</span><br><small>Servicios</small>
        </a>
        <a href="${pageContext.request.contextPath}/trabajos">
            <span>&#128188;</span><br><small>Trabajos</small>
        </a>
        <a href="${pageContext.request.contextPath}/chat">
            <span>&#128172;</span><br><small>Chat</small>
        </a>
        <a href="${pageContext.request.contextPath}/wallet" class="activo">
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

