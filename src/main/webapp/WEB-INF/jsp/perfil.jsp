<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SERVI – Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/estilos.css">
</head>
<body>
<div class="pantalla-movil servicios-screen">

    <div class="d-flex align-items-center justify-content-between p-3 border-bottom bg-white">
        <div class="d-flex align-items-center gap-2">
            <img src="${pageContext.request.contextPath}/assets/img/herramientas.png" style="width:28px;" alt="logo">
            <strong class="subtitulo-servi-outline">Perfil</strong>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-secondary btn-sm">Salir</a>
    </div>

    <div class="p-3" style="padding-bottom:80px !important;">

        <!-- Cabecera del perfil -->
        <div class="text-center mb-3">
            <img src="${pageContext.request.contextPath}/assets/img/<c:out value='${not empty perfil.avatar ? perfil.avatar : "usuario.jpg"}'/>
                " class="rounded-circle border" style="width:80px;height:80px;object-fit:cover;" alt="avatar">
            <h5 class="mt-2 mb-0"><c:out value="${perfil.nombreCompleto}"/></h5>
            <div class="text-muted small"><c:out value="${perfil.poblacion}"/></div>
            <div class="mt-1">
                <c:forEach begin="1" end="5" var="i">
                    <c:choose>
                        <c:when test="${i <= perfil.valoracionMedia}">&#11088;</c:when>
                        <c:otherwise><span style="opacity:0.3">&#11088;</span></c:otherwise>
                    </c:choose>
                </c:forEach>
                <small class="text-muted">(${perfil.totalValoraciones})</small>
            </div>
            <c:if test="${not empty perfil.descripcion}">
                <p class="small text-muted mt-2"><c:out value="${perfil.descripcion}"/></p>
            </c:if>
        </div>

        <!-- Alertas -->
        <c:if test="${not empty errorPass}">
            <div class="alert alert-danger py-2 small"><c:out value="${errorPass}"/></div>
        </c:if>
        <c:if test="${not empty okPass}">
            <div class="alert alert-success py-2 small"><c:out value="${okPass}"/></div>
        </c:if>

        <!-- Formulario editar (solo propio perfil) -->
        <c:if test="${esPropioPeril}">
            <div class="accordion mb-3" id="accordionPerfil">
                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed py-2" type="button"
                                data-bs-toggle="collapse" data-bs-target="#editarDatos">
                            &#9998; Editar mis datos
                        </button>
                    </h2>
                    <div id="editarDatos" class="accordion-collapse collapse" data-bs-parent="#accordionPerfil">
                        <div class="accordion-body p-3">
                            <form method="post" action="${pageContext.request.contextPath}/perfil">
                                <input type="hidden" name="accion" value="editar">
                                <div class="mb-2">
                                    <label class="form-label small">Nombre</label>
                                    <input type="text" name="nombre" class="form-control form-control-sm"
                                           value="<c:out value='${perfil.nombre}'/>" required>
                                </div>
                                <div class="mb-2">
                                    <label class="form-label small">Apellidos</label>
                                    <input type="text" name="apellidos" class="form-control form-control-sm"
                                           value="<c:out value='${perfil.apellidos}'/>">
                                </div>
                                <div class="mb-2">
                                    <label class="form-label small">Teléfono</label>
                                    <input type="tel" name="telefono" class="form-control form-control-sm"
                                           value="<c:out value='${perfil.telefono}'/>">
                                </div>
                                <div class="mb-2">
                                    <label class="form-label small">Población</label>
                                    <input type="text" name="poblacion" class="form-control form-control-sm"
                                           value="<c:out value='${perfil.poblacion}'/>">
                                </div>
                                <div class="mb-2">
                                    <label class="form-label small">Descripción</label>
                                    <textarea name="descripcion" class="form-control form-control-sm" rows="3"><c:out value="${perfil.descripcion}"/></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary btn-sm w-100">Guardar cambios</button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="accordion-item">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed py-2" type="button"
                                data-bs-toggle="collapse" data-bs-target="#cambiarPass">
                            &#128274; Cambiar contraseña
                        </button>
                    </h2>
                    <div id="cambiarPass" class="accordion-collapse collapse" data-bs-parent="#accordionPerfil">
                        <div class="accordion-body p-3">
                            <form method="post" action="${pageContext.request.contextPath}/perfil">
                                <input type="hidden" name="accion" value="cambiar-password">
                                <div class="mb-2">
                                    <label class="form-label small">Contraseña actual</label>
                                    <input type="password" name="password_actual" class="form-control form-control-sm" required>
                                </div>
                                <div class="mb-2">
                                    <label class="form-label small">Nueva contraseña</label>
                                    <input type="password" name="password_nueva" class="form-control form-control-sm" required minlength="6">
                                </div>
                                <div class="mb-2">
                                    <label class="form-label small">Repetir nueva contraseña</label>
                                    <input type="password" name="password_nueva2" class="form-control form-control-sm" required minlength="6">
                                </div>
                                <button type="submit" class="btn btn-warning btn-sm w-100">Cambiar contraseña</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Servicios del usuario -->
        <h6 class="fw-bold mb-2">&#128295; Servicios</h6>
        <c:choose>
            <c:when test="${empty servicios}">
                <p class="text-muted small mb-3">Sin servicios publicados.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="s" items="${servicios}">
                    <div class="servicio-card mb-2 p-2">
                        <div class="d-flex justify-content-between">
                            <span class="small fw-semibold"><c:out value="${s.titulo}"/></span>
                            <span class="badge bg-secondary"><c:out value="${s.categoriaNombre}"/></span>
                        </div>
                        <div class="small text-muted"><c:out value="${s.poblacion}"/> · ${s.precio} €</div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>

        <!-- Trabajos del usuario -->
        <h6 class="fw-bold mb-2 mt-3">&#128188; Trabajos</h6>
        <c:choose>
            <c:when test="${empty trabajos}">
                <p class="text-muted small mb-3">Sin trabajos publicados.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="t" items="${trabajos}">
                    <div class="servicio-card mb-2 p-2">
                        <div class="d-flex justify-content-between">
                            <span class="small fw-semibold"><c:out value="${t.titulo}"/></span>
                            <span class="badge bg-secondary"><c:out value="${t.categoriaNombre}"/></span>
                        </div>
                        <div class="small text-muted"><c:out value="${t.poblacion}"/></div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>

        <!-- Valoraciones recibidas -->
        <h6 class="fw-bold mb-2 mt-3">&#11088; Valoraciones</h6>
        <c:choose>
            <c:when test="${empty valoraciones}">
                <p class="text-muted small">Sin valoraciones todavía.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="v" items="${valoraciones}">
                    <div class="servicio-card mb-2 p-2">
                        <div class="d-flex align-items-center gap-2 mb-1">
                            <img src="${pageContext.request.contextPath}/assets/img/<c:out value='${not empty v.valoradorAvatar ? v.valoradorAvatar : "usuario.jpg"}'/>
                                " class="rounded-circle" style="width:30px;height:30px;object-fit:cover;" alt="av">
                            <span class="small fw-semibold"><c:out value="${v.valoradorNombre}"/></span>
                            <span class="ms-auto">
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
        <a href="${pageContext.request.contextPath}/servicios">
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
        <a href="${pageContext.request.contextPath}/perfil" class="activo">
            <span>&#128100;</span><br><small>Perfil</small>
        </a>
    </nav>
</div>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
</body>
</html>

