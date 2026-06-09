package com.moviles.exam2.data.repository

import com.moviles.exam2.data.model.Bug
import com.moviles.exam2.data.model.BugStatus
import com.moviles.exam2.data.model.Priority
import com.moviles.exam2.data.model.Severity
import com.moviles.exam2.data.network.dto.BugDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.util.Date


interface BugRepository {
    fun getBugs(): Flow<List<Bug>>
    fun addBug(bug: Bug)
    fun updateBugPriority(bugId: String, newPriority: Priority)
    fun updateBugStatus(bugId: String, newStatus: BugStatus)
    fun updateBugSeverity(bugId: String, newSeverity: Severity)
}


class MockBugRepository : BugRepository {

    // Simulamos la persistencia en el "servidor" usando DTOs
    private val _bugsDto = MutableStateFlow<List<BugDto>>(generateMockBugsDto())
    

    override fun getBugs(): Flow<List<Bug>> = _bugsDto.asStateFlow().map { list ->
        list.map { it.toDomain() }
    }

    override fun addBug(bug: Bug) {
        val newDto = BugDto(
            bug_id = bug.id,
            title = bug.title,
            severity = bug.severity.name,
            priority = bug.priority.name,
            status = bug.status.name,
            system = bug.affectedSystem,
            created_at = bug.createdAt.time,
            category = bug.category
        )
        _bugsDto.value = _bugsDto.value + newDto
    }

    override fun updateBugPriority(bugId: String, newPriority: Priority) {
        _bugsDto.value = _bugsDto.value.map {
            if (it.bug_id == bugId) it.copy(priority = newPriority.name) else it
        }
    }

    override fun updateBugStatus(bugId: String, newStatus: BugStatus) {
        _bugsDto.value = _bugsDto.value.map {
            if (it.bug_id == bugId) it.copy(status = newStatus.name) else it
        }
    }

    override fun updateBugSeverity(bugId: String, newSeverity: Severity) {
        _bugsDto.value = _bugsDto.value.map {
            if (it.bug_id == bugId) it.copy(severity = newSeverity.name) else it
        }
    }

    private fun generateMockBugsDto(): List<BugDto> {
        val now = Date().time
        return listOf(
            BugDto("BUG-001", "Error de autenticación en el login", "CRITICAL", "URGENT", "OPEN", "Identity Provider", now, "Security"),
            BugDto("BUG-006", "Fallo de integración con el servicio de pagos", "CRITICAL", "URGENT", "OPEN", "Payment Gateway", now, "Integration"),
            BugDto("BUG-002", "Fallo de sincronización de base de datos offline", "HIGH", "HIGH", "IN_PROGRESS", "Local Storage", now, "Sync"),
            BugDto("BUG-003", "Error de carga de datos en el perfil de usuario", "MEDIUM", "MEDIUM", "OPEN", "User API", now, "Data"),
            BugDto("BUG-004", "Problemas de rendimiento al scrollear lista", "MEDIUM", "LOW", "OPEN", "Recycler View", now, "Performance"),
            BugDto("BUG-005", "Error de UI: Botón de envío desalineado", "LOW", "LOW", "RESOLVED", "Design System", now, "UI/UX")
        )
    }
}
