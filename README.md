# SERVI — Marketplace de Servicios Locales

Aplicación web desarrollada en Java con Servlets, JSP y MySQL, desplegable en Tomcat 8/9 mediante XAMPP.

---

## Tecnologías utilizadas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java JDK | 8 | Lenguaje principal |
| Apache Tomcat | 8/9 | Servidor de aplicaciones |
| MySQL | 8.x | Base de datos relacional |
| Maven | 3.8+ | Gestión de dependencias y build |

---

## Requisitos previos

- Java JDK 8 o superior
- Maven 3.8+
- XAMPP con MySQL activo
- Tomcat 8.x o 9.x


---

## Instalación y puesta en marcha

### 1. Clonar el repositorio

```bash
git clone https://github.com/vilasecaa21/servi-app.git
cd servi-app
```

### 2. Crear la base de datos

1. Abre XAMPP y arranca el módulo **MySQL**.
2. Accede a `http://localhost/phpmyadmin`.
3. Importa el script SQL incluido en el proyecto:
   ```
   src/main/resources/servi_schema.sql
   ```
   → phpMyAdmin: **Importar** → selecciona el fichero → **Continuar**



### 3. Configurar la conexión a la base de datos

La conexión se gestiona en `src/main/java/com/servi/util/DBConnection.java` mediante variables de entorno, con los siguientes valores por defecto para desarrollo local:

| Variable | Valor por defecto |
|----------|------------------|
| `DB_HOST` | `localhost` |
| `DB_PORT` | `3306` |
| `DB_NAME` | `servi` |
| `DB_USER` | `root` |
| `DB_PASSWORD` | *(vacío)* |

Para un entorno diferente, define estas variables antes de arrancar Tomcat.

### 4. Compilar el proyecto

```bash
mvn clean package
```

Esto genera el fichero `target/servi.war`.

### 5. Desplegar en Tomcat

**Opción A — Despliegue manual:**
1. Copia `target/servi.war` a `<TOMCAT_HOME>/webapps/`.
2. Arranca Tomcat:
   - Windows: `<TOMCAT_HOME>/bin/startup.bat`
   - Linux/Mac: `<TOMCAT_HOME>/bin/startup.sh`
3. Accede a la aplicación en: `http://localhost:8080/servi`

**Opción B — Tomcat integrado en XAMPP:**
1. Copia el `.war` a `C:\xampp\tomcat\webapps\`.
2. Reinicia el módulo Tomcat desde el panel de XAMPP.
3. Accede a: `http://localhost:8080/servi`


