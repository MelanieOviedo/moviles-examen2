package com.moviles.exam2.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moviles.exam2.data.config.FeatureFlags
import com.moviles.exam2.data.model.Bug
import com.moviles.exam2.data.model.BugStatus
import com.moviles.exam2.data.model.Priority
import com.moviles.exam2.data.model.Severity
import com.moviles.exam2.ui.viewmodel.BugEvent
import com.moviles.exam2.ui.viewmodel.BugListViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugDetailScreen(
    bugId: String,
    viewModel: BugListViewModel,
    onBack: () -> Unit
) {
    val bugs by viewModel.bugs.collectAsState()
    val bug = bugs.find { it.id == bugId }
    val isUpdateSeverityEnabled by FeatureFlags.isUpdateSeverityEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Bug") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (bug != null) {
            BugDetailContent(
                bug = bug,
                isUpdateSeverityEnabled = isUpdateSeverityEnabled,
                modifier = Modifier.padding(paddingValues),
                onSaveChanges = { newStatus, newPriority, newSeverity ->
                    viewModel.onEvent(
                        BugEvent.UpdateBug(
                            bugId = bug.id,
                            newStatus = newStatus,
                            newPriority = newPriority,
                            newSeverity = if (isUpdateSeverityEnabled) newSeverity else bug.severity
                        )
                    )
                    onBack()
                }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Bug no encontrado")
            }
        }
    }
}

@Composable
fun BugDetailContent(
    bug: Bug,
    isUpdateSeverityEnabled: Boolean,
    modifier: Modifier = Modifier,
    onSaveChanges: (BugStatus, Priority, Severity) -> Unit
) {
    var tempStatus by remember { mutableStateOf(bug.status) }
    var tempPriority by remember { mutableStateOf(bug.priority) }
    var tempSeverity by remember { mutableStateOf(bug.severity) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Título
        DetailItem(label = "TÍTULO", value = bug.title)

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        // 2. Severidad
        Text("SEVERIDAD", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        if (isUpdateSeverityEnabled) {
            SeverityDropdown(currentSeverity = tempSeverity, onSeveritySelected = { tempSeverity = it })
        } else {
            Text(tempSeverity.name, style = MaterialTheme.typography.bodyLarge)
        }

        // 3. Prioridad
        Text("PRIORIDAD", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        PriorityDropdown(currentPriority = tempPriority, onPrioritySelected = { tempPriority = it })

        // 4. Estado
        Text("ESTADO", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        StatusDropdown(currentStatus = tempStatus, onStatusSelected = { tempStatus = it })

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        // 5. Sistema Afectado
        DetailItem(label = "SISTEMA AFECTADO", value = bug.affectedSystem)

        // 6. Fecha de Creación
        DetailItem(label = "FECHA DE CREACIÓN", value = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(bug.createdAt))

        // 7. Categoría del Error
        DetailItem(label = "CATEGORÍA DEL ERROR", value = bug.category)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSaveChanges(tempStatus, tempPriority, tempSeverity) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("GUARDAR CAMBIOS")
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun StatusDropdown(currentStatus: BugStatus, onStatusSelected: (BugStatus) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(currentStatus.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            BugStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.name) },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PriorityDropdown(currentPriority: Priority, onPrioritySelected: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(currentPriority.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Priority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name) },
                    onClick = {
                        onPrioritySelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SeverityDropdown(currentSeverity: Severity, onSeveritySelected: (Severity) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(currentSeverity.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Severity.entries.forEach { severity ->
                DropdownMenuItem(
                    text = { Text(severity.name) },
                    onClick = {
                        onSeveritySelected(severity)
                        expanded = false
                    }
                )
            }
        }
    }
}
