# Justificación Técnica y Decisiones Arquitectónicas

Este documento detalla las decisiones técnicas tomadas para el desarrollo del MVP de ButterTech Bug Tracker.

## 1. Justificación Técnica
Se seleccionó un stack tecnológico moderno basado en **Kotlin** y **Jetpack Compose**. Esta elección se justifica por:
*   **Productividad:** Compose reduce el código boilerplate en comparación con el sistema de Views tradicional.
*   **Rendimiento:** El sistema de composición inteligente asegura una UI fluida y eficiente.
*   **Mantenibilidad:** El uso de herramientas estándar de la industria garantiza que el proyecto sea fácilmente transferible a otros ingenieros.

## 2. Decisiones Arquitectónicas
El proyecto sigue una arquitectura **MVVM (Model-View-ViewModel)** con una clara separación de capas:

### Capa de Datos (Data Layer)
*   **Repository Pattern:** Se implementó `BugRepository` para abstraer el origen de los datos.
*   **DTOs vs Domain Models:** Se crearon `BugDto` (objetos de red) y `Bug` (objeto de dominio). Esta decisión permite que la UI sea independiente de los cambios en los nombres de los campos de la API, cumpliendo con el principio de robustez.
*   **Retrofit Ready:** Aunque se usa un `MockBugRepository`, se incluyó la interfaz `BugApiService` y las dependencias de Retrofit para una transición inmediata a producción.

### Capa de UI (View Layer)
*   **Jetpack Compose:** Manejo declarativo de la interfaz.
*   **State Management:** Se utiliza `StateFlow` para observar cambios en la lista de bugs y en las Feature Flags, garantizando que la UI sea siempre un reflejo fiel del estado actual.

## 3. Estrategia Basada en Eventos (UDF)
Se implementó un patrón de **Flujo de Datos Unidireccional (UDF)**:
*   **Eventos:** Todas las acciones del usuario (crear, actualizar) se encapsulan en una `sealed class BugEvent`.
*   **Procesamiento:** El ViewModel expone una única función `onEvent(BugEvent)`. Esto facilita el debugging y las pruebas unitarias al centralizar toda la lógica de transformación de estado en un solo punto.

## 4. Estrategia de Feature Flags
Las Feature Flags se manejan mediante el objeto `FeatureFlags`:
*   **Implementación:** Utiliza `StateFlow` para emitir cambios en tiempo real.
*   **Justificación:** Permite habilitar o deshabilitar módulos (como la creación de bugs) de forma centralizada sin afectar múltiples partes del código. Está preparado para integrarse con servicios de configuración remota mediante el método `updateFromRemote`.

## 5. Flujo General del Sistema
1.  **Entrada:** El usuario interactúa con la UI (ej. cambia una prioridad).
2.  **Acción:** La View emite un `BugEvent` al ViewModel.
3.  **Proceso:** El ViewModel llama al Repositorio.
4.  **Persistencia:** El Repositorio actualiza su lista interna de DTOs y emite el nuevo estado.
5.  **Salida:** El ViewModel observa el cambio, aplica el **ordenamiento jerárquico** (Prioridad > Severidad) y la UI se recompone automáticamente.

---
**Orientado a Ingenieros de Software - ButterTech Corp.**
