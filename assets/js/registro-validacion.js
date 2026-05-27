document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formRegistro");
    const nombre = document.getElementById("nombre");
    const email = document.getElementById("email");
    const password = document.getElementById("password");

    const errorNombre = document.getElementById("errorNombre");
    const errorEmail = document.getElementById("errorEmail");
    const errorPassword = document.getElementById("errorPassword");
    const mensajeExito = document.getElementById("mensajeExito");

    function limpiarMensajes() {
        errorNombre.textContent = "";
        errorEmail.textContent = "";
        errorPassword.textContent = "";
        mensajeExito.textContent = "";

        nombre.classList.remove("input-error", "input-ok");
        email.classList.remove("input-error", "input-ok");
        password.classList.remove("input-error", "input-ok");
    }

    function validarEmail(valor) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(valor);
    }

    function validarNombre() {
        const valor = nombre.value.trim();

        if (valor === "") {
            errorNombre.textContent = "El nombre y apellidos es obligatorio.";
            nombre.classList.add("input-error");
            nombre.classList.remove("input-ok");
            return false;
        }

        if (valor.length < 3) {
            errorNombre.textContent = "Introduce un nombre más completo.";
            nombre.classList.add("input-error");
            nombre.classList.remove("input-ok");
            return false;
        }

        errorNombre.textContent = "";
        nombre.classList.remove("input-error");
        nombre.classList.add("input-ok");
        return true;
    }

    function validarCampoEmail() {
        const valor = email.value.trim();

        if (valor === "") {
            errorEmail.textContent = "El email es obligatorio.";
            email.classList.add("input-error");
            email.classList.remove("input-ok");
            return false;
        }

        if (!validarEmail(valor)) {
            errorEmail.textContent = "Introduce un email válido.";
            email.classList.add("input-error");
            email.classList.remove("input-ok");
            return false;
        }

        errorEmail.textContent = "";
        email.classList.remove("input-error");
        email.classList.add("input-ok");
        return true;
    }

    function validarPassword() {
        const valor = password.value.trim();

        if (valor === "") {
            errorPassword.textContent = "La contraseña es obligatoria.";
            password.classList.add("input-error");
            password.classList.remove("input-ok");
            return false;
        }

        if (valor.length < 6) {
            errorPassword.textContent = "La contraseña debe tener al menos 6 caracteres.";
            password.classList.add("input-error");
            password.classList.remove("input-ok");
            return false;
        }

        errorPassword.textContent = "";
        password.classList.remove("input-error");
        password.classList.add("input-ok");
        return true;
    }

    nombre.addEventListener("blur", validarNombre);
    email.addEventListener("blur", validarCampoEmail);
    password.addEventListener("blur", validarPassword);

    form.addEventListener("submit", function (event) {
        event.preventDefault();
        limpiarMensajes();

        const nombreValido = validarNombre();
        const emailValido = validarCampoEmail();
        const passwordValida = validarPassword();

        if (nombreValido && emailValido && passwordValida) {
            mensajeExito.textContent = "Formulario validado correctamente.";
            form.reset();

            nombre.classList.remove("input-ok");
            email.classList.remove("input-ok");
            password.classList.remove("input-ok");
        }
    });
});