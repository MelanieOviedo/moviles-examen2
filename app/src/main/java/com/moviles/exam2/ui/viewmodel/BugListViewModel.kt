package com.moviles.exam2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.exam2.data.model.Bug
import com.moviles.exam2.data.model.BugStatus
import com.moviles.exam2.data.model.Priority
import com.moviles.exam2.data.model.Severity
import com.moviles.exam2.data.repository.BugRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import java.util.UUID

/**
 * Eventos de la interfaz de usuario para el Bug Tracker.
 * Representa las intenciones del usuario (User Intentions).
 */
sealed class BugEvent {
    data class CreateBug(
        val title: String,
        val severity: Severity,
        val priority: Priority,
        val system: String,
        val category: String
    ) : BugEvent()
    data class UpdateBug(
        val bugId: String,
        val newStatus: BugStatus,
        val newPriority: Priority,
        val newSeverity: Severity
    ) : BugEvent()
}

/**
 * ViewModel encargado de la lógica de negocio para la lista de bugs.
 * Sigue el patrón MVVM con flujo de datos unidireccional (UDF).
 */
class BugListViewModel(
    private val repository: BugRepository
) : ViewModel() {

    /**
     * Lista de bugs expuesta a la UI. 
     * Se mantiene actualizada automáticamente gracias a StateFlow.
     * Incluye lógica de ordenamiento: Severidad CRITICAL aparece primero.
     */
    val bugs: StateFlow<List<Bug>> = repository.getBugs()
        .map { list ->
            // Priorizamos el ordenamiento por Prioridad (según feedback del usuario)
            // y luego por Severidad para desempatar.
            list.sortedWith(
                compareByDescending<Bug> { it.priority.ordinal }
                    .thenByDescending { it.severity.ordinal }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Punto de entrada único para eventos desde la View.
     * Facilita el seguimiento de acciones y el testing.
     */
    fun onEvent(event: BugEvent) {
        when (event) {
            is BugEvent.CreateBug -> createNewBug(
                event.title,
                event.severity,
                event.priority,
                event.system,
                event.category
            )
            is BugEvent.UpdateBug -> updateBug(
                event.bugId,
                event.newStatus,
                event.newPriority,
                event.newSeverity
            )
        }
    }

    private fun createNewBug(
        title: String,
        severity: Severity,
        priority: Priority,
        system: String,
        category: String
    ) {
        val newBug = Bug(
            id = "BUG-${UUID.randomUUID().toString().take(4).uppercase()}",
            title = title,
            severity = severity,
            priority = priority,
            status = BugStatus.OPEN,
            affectedSystem = system,
            createdAt = Date(),
            category = category
        )
        repository.addBug(newBug)
    }

    private fun updateBug(
        bugId: String,
        newStatus: BugStatus,
        newPriority: Priority,
        newSeverity: Severity
    ) {
        repository.updateBugStatus(bugId, newStatus)
        repository.updateBugPriority(bugId, newPriority)
        repository.updateBugSeverity(bugId, newSeverity)
    }
}
