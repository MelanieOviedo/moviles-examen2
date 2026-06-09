package com.moviles.exam2.data.network.dto

import com.moviles.exam2.data.model.Bug
import com.moviles.exam2.data.model.BugStatus
import com.moviles.exam2.data.model.Priority
import com.moviles.exam2.data.model.Severity
import java.util.Date

/**
 * Data Transfer Object (DTO) para la integración con la API de ButterTech.
 * Representa la estructura de datos que se recibe del backend.
 */
data class BugDto(
    val bug_id: String,
    val title: String,
    val severity: String,
    val priority: String,
    val status: String,
    val system: String,
    val created_at: Long,
    val category: String
) {
    /**
     * Mapea el DTO al modelo de dominio de la aplicación.
     * Asegura la separación de responsabilidades y la integridad de la UI.
     */
    fun toDomain(): Bug {
        return Bug(
            id = bug_id,
            title = title,
            severity = try { Severity.valueOf(severity.uppercase()) } catch (e: Exception) { Severity.LOW },
            priority = try { Priority.valueOf(priority.uppercase()) } catch (e: Exception) { Priority.LOW },
            status = try { BugStatus.valueOf(status.uppercase()) } catch (e: Exception) { BugStatus.OPEN },
            affectedSystem = system,
            createdAt = Date(created_at),
            category = category
        )
    }
}
