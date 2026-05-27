<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="servicios-nav-inferior">
    <a href="${pageContext.request.contextPath}/servicios"
       class="${pageContext.request.servletPath.contains('servicios') ? 'activo' : ''}">
        <span>&#128295;</span><br><small>Servicios</small>
    </a>
    <a href="${pageContext.request.contextPath}/trabajos"
       class="${pageContext.request.servletPath.contains('trabajo') ? 'activo' : ''}">
        <span>&#128188;</span><br><small>Trabajos</small>
    </a>
    <a href="${pageContext.request.contextPath}/chat"
       class="${pageContext.request.servletPath.contains('chat') ? 'activo' : ''}">
        <span>&#128172;</span><br><small>Chat</small>
    </a>
    <a href="${pageContext.request.contextPath}/wallet"
       class="${pageContext.request.servletPath.contains('wallet') ? 'activo' : ''}">
        <span>&#128181;</span><br><small>Wallet</small>
    </a>
    <a href="${pageContext.request.contextPath}/perfil"
       class="${pageContext.request.servletPath.contains('perfil') ? 'activo' : ''}">
        <span>&#128100;</span><br><small>Perfil</small>
    </a>
</nav>

