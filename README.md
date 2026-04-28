<div align="center">

<img src="https://capsule-render.vercel.app/api?type=soft&color=0:e30613,20:c2185b,50:6B21A8,80:1d4ed8,100:0d47a1&height=260&section=header&text=VERISURE%20VOLUNTEERING&fontSize=44&fontColor=ffffff&animation=twinkling&fontAlignY=40&desc=Conectando%20corazones%20con%20causas%20que%20importan&descAlignY=60&descSize=17" />

<br/>

[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=22&duration=3000&pause=1000&color=e30613&center=true&vCenter=true&multiline=true&repeat=true&width=700&height=120&lines=Plataforma+de+voluntariado+corporativo;React+19+%2B+Spring+Boot+%2B+Atomic+Design;Tecnolog%C3%ADa+al+servicio+del+bien+com%C3%BAn+%F0%9F%8C%8D)](https://git.io/typing-svg)

<br/>

![Version](https://img.shields.io/badge/version-1.0.0-e30613?style=for-the-badge&logo=semver&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

</div>

---

## ✨ ¿Qué es Verisure Volunteering API?

> *El núcleo lógico y base de datos que da vida a la plataforma de voluntariado corporativo. Una API RESTful segura, rápida y robusta que conecta empleados de Verisure con ONGs que necesitan su talento y tiempo.*


Tres mundos. Un ecosistema. Impacto real.

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│   👤 VOLUNTARIO          🏢 ONG              🛡️ ADMIN          │
│                                                                 │
│  Explora proyectos   Publica proyectos    Supervisa todo        │
│  Aplica y participa  Gestiona equipos     Aprueba proyectos     │
│  Obtén certificados  Mide tu impacto      Genera métricas       │
│                                                                 │
│         ↕️                 ↕️                  ↕️              │
│                                                                 │
│  ────────────── API REST (Spring Boot + JWT) ──────────────     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Stack Tecnológico 

<div align="center">

[![My Skills](https://skillicons.dev/icons?i=java,spring,mysql,maven,git,github,postman&theme=dark)](https://skillicons.dev)

</div>

| Categoría | Tecnología | Uso en el proyecto |
|-----------|-----------|--------------------|
| ☕ Lenguaje | Java 21 | Lógica de negocio core |
| 🌱 Framework | Spring Boot 3.4 | Estructura principal y servidor web |
| 🛡️ Seguridad | Spring Security + JWT | Autenticación y control de roles |
| 🗄️ ORM | Spring Data JPA (Hibernate) | Mapeo de entidades y consultas |
| 🛢️ Base de Datos | MySQL 8.0 | Persistencia de datos relacionales |
| 🛠️ Build Tool | Maven | Gestión de dependencias y compilación |
| ☁️ Almacenamiento | Cloudinary API | Subida y gestión de imágenes |
| 🌶️ Utilidades | Lombok | Reducción de código boilerplate |
| 🧪 Testing | JUnit 5 + Mockito | Pruebas unitarias de servicios |

---

## 🗄️ Modelo Entidad-Relación y Endpoints

### 🗺️ Mapa de la Base de Datos
El núcleo de nuestra aplicación está diseñado sobre una base de datos relacional (MySQL) estructurada para garantizar la integridad, escalabilidad y el manejo seguro de los datos (implementando borrados lógicos o *Soft Deletes* donde es necesario).

<div align="center">
  <img <img width="264![U<img width="1889" height="1181" alt="ER_Verisure(1)" src="https://github.com/user-attachments/assets/efe44a1c-43b2-40d0-b27b-2f4ed3ed701e"/>
</div>

---

### 🌐 Mapa de Endpoints (API REST)

Nuestra API expone los siguientes recursos principales bajo la ruta base `/api/v1`:

```text
🌐 /api/v1
│
├── 🔑 /auth                 → Autenticación y Seguridad
│   └── POST /login          → Validación de credenciales y generación de JWT
│
├── 👤 /users                → Gestión de Perfiles
│   ├── GET /                → Listado de usuarios (Voluntarios / ONGs)
│   └── GET /{id}            → Detalle de un perfil específico
│
├── 🏢 /projects             → Gestión de Proyectos
│   ├── GET /                → Catálogo completo (con filtros por ODS, estado, etc.)
│   ├── POST /               → Creación de un nuevo proyecto (Requiere rol ONG)
│   ├── POST /{id}/favorite  → Marcar/desmarcar proyecto como favorito (Toggle)
│   └── GET /my-projects     → Proyectos asociados al usuario autenticado
│
├── 📝 /applications         → Postulaciones e Inscripciones
│   ├── POST /               → Aplicar a un proyecto de voluntariado
│   └── GET /me              → Listado de mis postulaciones activas e histórico
│
├── 🌍 /sdgs                 → Categorías (Objetivos de Desarrollo Sostenible)
│   └── GET /                → Catálogo de ODS disponibles para filtrado
│
└── 📊 /dashboard            → Panel de Administración
    └── GET /metrics         → Generación de KPIs y datos agregados para gráficas
```

---

## 🎭 Roles y Funcionalidades

<details>
<summary><b>👤 Voluntario (EMPLOYEE)</b> — click para expandir</summary>

<br/>

El corazón de la plataforma. El voluntario puede:

- 🔍 **Explorar** el catálogo de proyectos publicados con filtros por categoría, modalidad y ciudad
- ❤️ **Guardar favoritos** con un solo click
- 📝 **Aplicar** a proyectos o unirse a lista de espera si están llenos
- 📋 **Gestionar** sus voluntariados activos y cancelar participación
- 🏆 **Descargar certificados** digitales de cada proyecto completado
- 🔍 Filtrar aplicaciones por estado: `PENDIENTE` · `APROBADO` · `LISTA DE ESPERA` · `CERRADO`

</details>

<details>
<summary><b>🏢 ONG (ORGANIZATION)</b> — click para expandir</summary>

<br/>

La voz de las organizaciones. La ONG puede:

- ➕ **Crear proyectos** con formulario completo y validaciones en tiempo real
- 📸 **Subir imagen** del proyecto (PNG/JPG/WEBP · máx. 10MB)
- 🌍 **Configurar modalidad**: Presencial, Semipresencial o Virtual (con URL válida)
- 📅 **Gestionar fechas**, participantes requeridos y horas de dedicación
- 🎯 **Categorizar** por ODS (Objetivos de Desarrollo Sostenible)
- 📊 **Ver estado** de cada proyecto: `EN REVISIÓN` · `PUBLICADO` · `RECHAZADO` · `CANCELADO`

</details>

<details>
<summary><b>🛡️ Admin</b> — click para expandir</summary>

<br/>

El guardián del ecosistema. El admin puede:

- ✅ **Aprobar o rechazar** proyectos enviados por las ONGs
- 📊 **Ver métricas** con gráficas de dona y barras por categoría
- 👥 **Gestionar perfiles** de ONGs y voluntarios
- 📁 **Secciones especializadas**: En Revisión · Activos · Archivados · Rechazados
- 👁️ **Ver participantes** por proyecto con modal detallado
- 🔍 **Buscar y filtrar** proyectos con barra de búsqueda en tiempo real
- 🏢 **Crear perfiles de ONG** directamente desde el panel admin

</details>

---

## 🧩 Arquitectura — Patrón N-Capas (N-Tier)

Construido bajo una arquitectura de capas bien definidas para garantizar la separación de responsabilidades, la escalabilidad y la facilidad de testing.

```text
src/main/java/com/verisure/backend/
│
├── 🌐 controller/       (Capa de Presentación / API REST)
│   ├── ApplicationController, ProjectController, DashboardController...
│   └── Reciben HTTP Requests, validan DTOs de entrada y devuelven HTTP Responses.
│
├── ⚙️ service/          (Capa de Lógica de Negocio)
│   ├── Implementaciones de las reglas core de Verisure Volunteering.
│   └── Cálculos de KPIs, validaciones complejas y subida de imágenes.
│
├── 🗄️ repository/       (Capa de Acceso a Datos)
│   ├── Interfaces de Spring Data JPA.
│   └── Consultas personalizadas (Querys) y manejo de Soft Deletes.
│
├── 📦 entity/           (Modelo de Dominio)
│   ├── Clases mapeadas a las tablas de MySQL (@Entity).
│   └── Enums (`Role`, `StatusProject`) y Proyecciones (`ProjectAdminProjection`).
│
└── 🛡️ security/         (Capa de Seguridad)
    ├── Filtros JWT (`JWTAuthenticationFilter`, `JWTAuthorizationFilter`).
    └── Configuración de Spring Security y encriptación de contraseñas.
```

---

## ⚙️ Arquitectura Interna y Componentes

### 📦 Capa de Transporte de Datos (`src/dto/` & `src/mapper/`)

| Componente | Responsabilidad | Ejemplos |
|------------|-----------------|---------|
| **Request DTOs** | Validar y encapsular datos que entran a la API (`@NotNull`, `@Email`) | `ProjectRequestDTO`, `LoginRequestDTO` |
| **Response DTOs** | Formatear datos de salida (ej. ocultar contraseñas) para el Front-end | `ProjectResponseDTO`, `DashboardKpiResponseDTO` |
| **Mappers** | Convertir Entities de base de datos a DTOs y viceversa | `ProjectMapper`, `ApplicationMapper` |

---

### 🛡️ Seguridad y Autenticación (`src/security/`)

```text
Spring Security Filter Chain
 ├── JWTAuthenticationFilter → Intercepta /login, valida usuario y emite JWT Token.
 ├── JWTAuthorizationFilter  → Intercepta el resto de rutas, valida el Bearer token y roles.
 └── CustomAuthenticationManager → Encripta y compara contraseñas usando BCrypt.

Protección de Endpoints basada en Roles: @PreAuthorize("hasRole('ADMIN')")
Roles reconocidos: ADMIN · EMPLOYEE · ONG
```


---
### 🚨 Manejo Global de Errores (`src/exception/`)

| Excepción Personalizada | Código HTTP | Caso de Uso |
|-------------------------|-------------|-------------|
| `ResourceNotFoundException` | 404 Not Found | Entidad no encontrada (ej. ID de proyecto inexistente) |
| `BadRequestException` | 400 Bad Request | Datos inválidos o lógica de negocio no cumplida |
| `UnauthorizedActionException`| 403 Forbidden | Intento de acción sin los permisos necesarios |
| `DuplicateResourceException`| 409 Conflict | Email ya registrado o postulación duplicada |
| `InvalidImageException` | 415 Unsupported | Archivo de imagen dañado o formato no permitido |

---

### 🛠️ Utilidades, Tareas y Configuración

| Carpeta / Archivo | Propósito |
|---------|-----------|
| `tasks/ProjectStatusCronJob.java` | Script automático (Cron Job) que actualiza el estado de los proyectos expirados en segundo plano. |
| `config/CloudinaryConfig.java` | Credenciales y conexión para la gestión de imágenes en la nube. |
| `config/CorsConfig.java` | Permite las peticiones cruzadas (CORS) desde el Front-end. |
| `seeder/DataSeeder.java` | Pobla la base de datos con datos iniciales (Usuarios y ODS) al arrancar. |

---


## 🚀 Instalación y Arranque

```bash
### ⚙️ 1. Requisitos Previos y Configuración
Antes de arrancar el proyecto, necesitas tener instalado **Java 21**, **Maven** y un servidor de **MySQL** corriendo en tu máquina.

Debes configurar tus variables de entorno en el archivo `src/main/resources/application.properties`:

```properties
# Configuración de Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/verisure_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

# Configuración JWT (Seguridad)
jwt.secret=tu_clave_secreta_jwt_aqui

# Configuración Cloudinary (Imágenes)
cloudinary.url=cloudinary://API_KEY:API_SECRET@CLOUD_NAME

### 🏃‍♀️ 2. Clonar y Arrancar

# 1. Clona el repositorio
git clone [https://github.com/PF-VERISURE/Back_PF_Verisure.git](https://github.com/PF-VERISURE/Back_PF_Verisure.git)

# 2. Entra al directorio
cd Back_PF_Verisure

# 3. Instala dependencias y limpia la caché
mvn clean install

# 4. Inicia el servidor de Spring Boot
mvn spring-boot:run
```
---

## 💻 Proyecto Full Stack

Este repositorio contiene exclusivamente el **Motor Backend**. Para que la plataforma funcione completamente, debe conectarse con el cliente.

* **Repositorio Front-end:** [Verisure Volunteering Client](https://github.com/PF-VERISURE/Front_PF_Verisure)

---

> ⚠️ Asegúrate de tener el servidor MySQL encendido antes de ejecutar el paso 4. La API se levantará en `http://localhost:8080`

```bash
# Otros comandos útiles
mvn package          # Empaqueta la app en un archivo .jar para producción
mvn test             # Ejecuta toda la suite de pruebas unitarias
mvn clean            # Limpia la carpeta /target de compilaciones anteriores
mvn clean compile    # Recompila el proyecto desde cero
```

---

## 🧪 Tests

```text
src/test/java/com/verisure/backend/
│
├── ⚙️ service/
│   ├── ApplicationServiceTest.java        → Pruebas de postulaciones y cancelaciones
│   ├── ParticipationRecordServiceTest.java→ Pruebas de registro de horas y certificados
│   ├── ProjectServiceTest.java            → Pruebas de creación, favoritos y validaciones
│   ├── SdgServiceTest.java                → Pruebas de listado de categorías ODS
│   └── UserServiceTest.java               → Pruebas de autenticación y Soft Delete
│
└── 🚀 BackendApplicationTests.java        → Smoke test (Verificación de carga del contexto Spring)
```

```bash
mvn test      # Ejecuta toda la suite de pruebas unitarias y genera el reporte
```

Framework: **JUnit 5 (Jupiter)** · Mocking: **Mockito** · Entorno: **Spring Boot Test**

---

## 🔐 Autenticación y Seguridad (JWT)

El sistema utiliza **JSON Web Tokens (JWT)** para asegurar los endpoints y gestionar los permisos por roles.

```text
┌─────────────────────────────────────────────────────────────────┐
│                   FLUJO DE SEGURIDAD EN LA API                  │
│                                                                 │
│  1. Recepción de Credenciales (POST /auth/login)                │
│           ↓                                                     │
│  2. Validación en DB + Generación de Claims (Role, Email)       │
│           ↓                                                     │
│  3. Firma del Token con Clave Secreta (HS512)                   │
│           ↓                                                     │
│  4. Retorno de JWT al cliente                                   │
│           ↓                                                     │
│  5. Validación en cada petición (Authorization: Bearer <token>) │
│           ↓                                                     │
│  6. Filtro de Autorización: ¿Tiene el ROL necesario?            │
└─────────────────────────────────────────────────────────────────┘

Nota: Las contraseñas se almacenan de forma segura utilizando el algoritmo de hashing BCrypt
```

---

## 🌍 ODS — Categorías de Proyectos

Los proyectos se clasifican según los **Objetivos de Desarrollo Sostenible** de la ONU:

| # | Categoría |
|---|-----------|
| 1 | 💧 Agua limpia y saneamiento |
| 2 | 🏥 Salud y bienestar |
| 3 | 🏙️ Ciudades y comunidades sostenibles |
| 4 | ⚡ Energía asequible y no contaminante |
| 5 | 🌾 Hambre cero |
| 6 | ⚖️ Igualdad de género |
| 7 | 💰 Fin de la pobreza |
| 8 | ♻️ Producción y consumo responsables |
| 9 | 🤝 Reducción de las desigualdades |
| 10 | 🌊 Vida submarina |

"La API gestiona la lógica de filtrado y categorización basada en estos 10 objetivos, permitiendo una segmentación precisa de las causas sociales.

---

## 👩‍💻 Desarrolladoras

<div align="center">

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/GeraldineSaco">
        <img src="https://api.dicebear.com/9.x/adventurer/png?seed=Geraldine&size=200&backgroundColor=ffd5dc" width="100px" height="100px" style="border-radius:50%; border: 3px solid #e30613; padding: 3px; object-fit:cover;"/>
      </a><br/><br/>
      <b>Geraldine Saco</b><br/>
      <a href="https://github.com/GeraldineSaco">
        <img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/krissvinti-ux">
        <img src="https://api.dicebear.com/9.x/adventurer/png?seed=Isabel&size=200&backgroundColor=d5e8ff&hair[]=long16" width="100px" height="100px" style="border-radius:50%; border: 3px solid #e30613; padding: 3px; object-fit:cover;"/>
      </a><br/><br/>
      <b>Cristina Viejó</b><br/>
      <a href="https://github.com/krissvinti-ux">
        <img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/AdaXana">
        <img src="https://api.dicebear.com/9.x/adventurer/png?seed=Guadalupe&size=200&backgroundColor=d5ffd5" width="100px" height="100px" style="border-radius:50%; border: 3px solid #e30613; padding: 3px; object-fit:cover;"/>
      </a><br/><br/>
      <b>Guadalupe Peña</b><br/>
      <a href="https://github.com/AdaXana">
        <img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/ManonChab">
        <img src="https://api.dicebear.com/9.x/adventurer/png?seed=Marie&size=200&backgroundColor=ffe8d5&hair[]=long14" width="100px" height="100px" style="border-radius:50%; border: 3px solid #e30613; padding: 3px; object-fit:cover;"/>
      </a><br/><br/>
      <b>Manon Godfroy</b><br/>
      <a href="https://github.com/ManonChab">
        <img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white"/>
      </a>
    </td>
  </tr>
</table>

</div>

---

<div align="center">

<img src="https://capsule-render.vercel.app/api?type=slice&color=0:0d47a1,30:1d4ed8,60:6B21A8,80:c2185b,100:e30613&height=150&section=footer&animation=twinkling" />

**Hecho con 💜 por el equipo de Femcoders · Bootcamp Full Stack - FemCoders 2026**

*"La tecnología más poderosa es la que conecta personas con propósito"*

![Visitors](https://visitor-badge.laobi.icu/badge?page_id=PF-VERISURE.Front_PF_Verisure&style=flat&color=e30613)

</div>
