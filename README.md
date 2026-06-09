# ButterTech Bug Tracker - PoC Android MVP

Este proyecto es una Prueba de Concepto (PoC) desarrollada para **ButterTech** con el objetivo de centralizar la gestión de bugs internos. Implementa una arquitectura moderna, reactiva y flexible, lista para la integración con servicios backend.

## 🏛️ Arquitectura y Patrones

La aplicación sigue los principios de **Clean Architecture** y el patrón **MVVM (Model-View-ViewModel)** combinado con **UDF (Unidirectional Data Flow)**:

*   **Model:** Representa la lógica de datos, incluyendo modelos de dominio (`Bug.kt`) y DTOs de red (`BugDto.kt`).
*   **View (Jetpack Compose):** Interfaz declarativa que reacciona a los cambios de estado emitidos por el ViewModel.
*   **ViewModel:** Centraliza el estado de la UI utilizando `StateFlow`. Expone un único punto de entrada de eventos (`BugEvent`) para procesar las intenciones del usuario, garantizando un flujo de datos en una sola dirección.

## 🚩 Feature Flags

Se ha implementado un sistema de **Feature Flags** (centralizado en `FeatureFlags.kt`) que permite habilitar o deshabilitar funcionalidades sin modificar la lógica central del negocio:

1.  **Creación de Bugs:** Controla la visibilidad del botón flotante (FAB) y el acceso al módulo de creación.
2.  **Edición de Severidad:** Permite alternar entre el modo lectura y edición de la severidad en el detalle de cada bug.

El sistema está diseñado para ser **"Backend-Ready"**, permitiendo actualizaciones reactivas de configuración desde servicios remotos (Firebase Remote Config o APIs propietarias).

## 📊 Lógica de Ordenamiento

Para mejorar la eficiencia operativa en ButterTech, la lista de bugs implementa una lógica de ordenamiento jerárquico:

1.  **Prioridad (Criterio Principal):** Los bugs se ordenan de mayor a menor prioridad (`URGENT` -> `HIGH` -> `MEDIUM` -> `LOW`).
2.  **Severidad (Criterio Secundario):** En caso de empate en prioridad, los bugs se ordenan por su impacto técnico (`CRITICAL` -> `HIGH` -> `MEDIUM` -> `LOW`).

Este ordenamiento se realiza de forma reactiva en el `BugListViewModel` cada vez que el repositorio emite una actualización de datos.

## 🛠️ Estructura del Proyecto

*   `/app`: Código fuente de la aplicación Android.
*   `/contracts`: Definición de la API utilizando OpenAPI 3.0 (`bugs-api.yaml`).
*   `/docs`: Documentación técnica y lineamientos del proyecto.
*   `/video`: Demostración funcional del MVP.

---
**Desarrollado para el Examen 2 de Diseño y Programación de Plataformas
Móviles - ButterTech Corp.**
