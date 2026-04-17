# 🍿 AmorDePelis

¡Bienvenido(a) a **AmorDePelis**! Una aplicación Android nativa desarrollada en Kotlin y diseñada con Jetpack Compose. La app permite a los usuarios registrarse, explorar, gestionar películas, organizar sus películas favoritas en listas y estar al tanto de los anuncios importantes.

---

## 🛠 Arquitectura y Tecnologías
El proyecto está estructurado utilizando **Clean Architecture** (Arquitectura Limpia) orientada a características (*Feature-Driven*). Las principales herramientas del stack técnico son:
- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Inyección de Dependencias:** Dagger Hilt
- **Patrón de diseño (Capa UI):** MVI / MVVM usando `UiState`
- **Navegación:** Jetpack Navigation Compose con grafos modulares (`FeatureNavGraph`)

---

## 📱 Funcionalidades Principales y Módulos

La aplicación está dividida en las siguientes funcionalidades núcleo (`features`):

### 1. 🔐 Autenticación (`Auth`) y Roles
- **Login:** Autenticación de un usuario existente.
- **Registro:** Creación de una cuenta nueva en el sistema.
- *Manejo de sesión de usuario y tokens usando un `SessionManager` persistente.*
- **Roles:** La app maneja dos perfiles de usuario:
  - **Usuario Admin:** Encargado de subir y registrar las películas al catálogo general.
  - **Usuario Común:** Explora el catálogo y agrega libremente el contenido a sus listas.

### 2. 🏠 Inicio (`Home`)
- **Pantalla Principal (Dashboard):** Visión general de las últimas novedades.
- **Avisos:** Opción para que usuarios (probablemente con rol administrador o autorizados) añadan anuncios importantes o *Updates*.

### 3. 🎬 Películas (`Movies`) e Integración de Hardware
- **Catálogo de Películas:** Muestra el listado de las películas disponibles (publicadas por administradores) para que los usuarios comunes las descubran y agreguen a sus listas de favoritos.
- **Detalle de Película (Recomendación al sacudir):** Pantalla con la información a fondo (sinopsis, géneros, etc.) de una cinta específica. En esta vista entra en acción el hardware del dispositivo: al **sacudir el celular**, el sensor (acelerómetro) lo detecta, se genera una recomendación u otra acción y **el celular vibra** como respuesta física.
- **Añadir Película (Solo Admin - Uso de Cámara):** Formulario para registrar una nueva película en la app. El administrador puede utilizar la **Cámara** del teléfono directamente para tomar una foto y enlazarla a la película.

### 4. 📋 Listas Personalizadas (`Lists`)
- **Bandeja de Listas:** Vista que expone colecciones de películas creadas.
- **Detalle de Lista:** Contiene las películas asociadas y permite la gestión del contenido.
- **Crear Lista:** Módulo para la creación rápida de nuevos recuadros / agrupaciones fílmicas personalizadas.

### 5. 👤 Perfil de Usuario (`User`)
- **Gestión de Perfil:** Visualización y edición eventual de datos personales del usuario autenticado. Opciones de preferencias o deslogueo (Cerrar sesión).

---

## 🔄 Flujo de Navegación de la Aplicación

1. **Revisión de Sesión:**
   Al lanzar la App en `MainActivity.kt`, se coordina en un `NavigationWrapper` el estado inicial interceptando al `SessionManager`. 
2. **Si el usuario NO está logueado (Auth NavGraph):**
   - Es redirigido automáticamente al `LoginScreen`.
   - Puede transitar al `RegisterScreen` si es nuevo usuario.
3. **Si el usuario ESTÁ logueado (Main/Home NavGraph):**
   - Transita directamente a `HomeScreen`.
   - Puede acceder vía navegación principal a:
     - **Catálogo de Películas** (`MoviesListScreen`) -> De donde puede navegar hacia Detalle o a la vista de Añadir nuevo filme.
     - **Mis Listas** (`ListsScreen`) -> De donde puede crear lista o ver el contenido de alguna.
     - **Perfil de Usuario** (`UserProfileScreen`).

---

## 📁 Estructura del Proyecto

El código base se organiza bajo `app/src/main/java/com/alejandra/amordepelis/`:
- **`core/`**: Código transverso y fundamental (Themes compartidos de Jetpack Compose, enrutadores `navigation`, gestores de almacenamiento persistente).
- **`features/`**:
  - `auth/` (data, domain, presentation, di, navigation)
  - `home/` (...)
  - `lists/` (...)
  - `movies/` (...)
  - `user/` (...)

Cada `feature` respecta la segregación por capas:
- `data/`: Interfaces con base de datos, repositorios remotos/locales.
- `domain/`: Reglas de negocio y Casos de Uso (Use Cases).
- `presentation/`: Específica a la interfaz incluyendo `screens/` (Compose UI), `viewmodels/` y estados gráficos (`UIState.kt`).
- `di/`: Provisión de Inyección de Dependencias particular.
- `navigation/`: Definición de Rutas para interconectar con todo el App Graph sin acoplar fuertemente otras views.
