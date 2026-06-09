package com.moviles.exam2.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moviles.exam2.data.config.FeatureFlags
import com.moviles.exam2.data.model.BugStatus
import com.moviles.exam2.data.model.Priority
import com.moviles.exam2.data.model.Severity
import com.moviles.exam2.ui.viewmodel.BugEvent
import com.moviles.exam2.ui.viewmodel.BugListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBugScreen(
    viewModel: BugListViewModel,
    onBack: () -> Unit
) {
    val isCreateEnabled by FeatureFlags.isCreateBugEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportar Nuevo Bug") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isCreateEnabled) {
            CreateBugForm(
                modifier = Modifier.padding(paddingValues),
                onSave = { title, severity, priority, system, category ->
                    viewModel.onEvent(
                        BugEvent.CreateBug(title, severity, priority, system, category)
                    )
                    onBack()
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "La creación de bugs no está disponible temporalmente.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CreateBugForm(
    modifier: Modifier = Modifier,
    onSave: (String, Severity, Priority, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf(Severity.MEDIUM) }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var system by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título del Bug") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Severidad", style = MaterialTheme.typography.labelLarge)
        SeveritySelector(selectedSeverity = severity, onSeveritySelected = { severity = it })

        Text("Prioridad", style = MaterialTheme.typography.labelLarge)
        PrioritySelector(selectedPriority = priority, onPrioritySelected = { priority = it })

        OutlinedTextField(
            value = system,
            onValueChange = { system = it },
            label = { Text("Sistema Afectado") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (title.isNotBlank() && system.isNotBlank() && category.isNotBlank()) {
                    onSave(title, severity, priority, system, category)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && system.isNotBlank() && category.isNotBlank()
        ) {
            Text("Guardar Bug")
        }
    }
}

@Composable
fun SeveritySelector(selectedSeverity: Severity, onSeveritySelected: (Severity) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedSeverity.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Severity.values().forEach { s ->
                DropdownMenuItem(
                    text = { Text(s.name) },
                    onClick = {
                        onSeveritySelected(s)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PrioritySelector(selectedPriority: Priority, onPrioritySelected: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedPriority.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Priority.values().forEach { p ->
                DropdownMenuItem(
                    text = { Text(p.name) },
                    onClick = {
                        onPrioritySelected(p)
                        expanded = false
                    }
                )
            }
        }
    }
}
