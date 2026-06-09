package com.moviles.exam2.data.config

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Implementado como un Singleton (object) para facilitar el acceso global de manera simple
 * en esta fase de MVP
 */
object FeatureFlags {

    // Flag para habilitar/deshabilitar la creación de nuevos bugs
    private val _isCreateBugEnabled = MutableStateFlow(true)
    val isCreateBugEnabled = _isCreateBugEnabled.asStateFlow()

    // Flag para habilitar/deshabilitar la actualización de la severidad
    private val _isUpdateSeverityEnabled = MutableStateFlow(true)
    val isUpdateSeverityEnabled = _isUpdateSeverityEnabled.asStateFlow()

    /**
     * Permite actualizar el estado de una funcionalidad en tiempo de ejecución.
     * Útil para pruebas A/B o despliegues controlados.
     */
    fun setFeatureEnabled(feature: Feature, enabled: Boolean) {
        when (feature) {
            Feature.CREATE_BUG -> _isCreateBugEnabled.value = enabled
            Feature.UPDATE_SEVERITY -> _isUpdateSeverityEnabled.value = enabled
        }
    }

    /**
     * Simula la actualización de flags desde un servicio remoto (Backend-Ready).
     * En una implementación real, esto sería llamado tras una respuesta exitosa de Retrofit.
     */
    fun updateFromRemote(config: Map<String, Boolean>) {
        config["isCreateBugEnabled"]?.let { _isCreateBugEnabled.value = it }
        config["isUpdateSeverityEnabled"]?.let { _isUpdateSeverityEnabled.value = it }
    }

    enum class Feature {
        CREATE_BUG,
        UPDATE_SEVERITY
    }
}
