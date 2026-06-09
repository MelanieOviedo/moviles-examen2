package com.moviles.exam2.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moviles.exam2.data.config.FeatureFlags
import com.moviles.exam2.data.model.Bug
import com.moviles.exam2.data.model.Severity
import com.moviles.exam2.ui.theme.Exam2Theme
import com.moviles.exam2.ui.viewmodel.BugEvent
import com.moviles.exam2.ui.viewmodel.BugListViewModel

@Composable
fun BugListScreen(
    viewModel: BugListViewModel,
    onBugClick: (String) -> Unit,
    onCreateClick: () -> Unit
) {
    val bugs by viewModel.bugs.collectAsState()
    val isCreateEnabled by FeatureFlags.isCreateBugEnabled.collectAsState()

    BugListContent(
        bugs = bugs,
        isCreateEnabled = isCreateEnabled,
        onCreateBug = onCreateClick,
        onBugClick = onBugClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugListContent(
    bugs: List<Bug>,
    isCreateEnabled: Boolean,
    onCreateBug: () -> Unit,
    onBugClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ButterTech Bug Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (isCreateEnabled) {
                FloatingActionButton(
                    onClick = onCreateBug,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Crear Bug")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(bugs, key = { it.id }) { bug ->
                BugItem(bug, onClick = { onBugClick(bug.id) })
            }
        }
    }
}

@Composable
fun BugItem(bug: Bug, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = bug.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column {
                    InfoTag(
                        label = "PRIORIDAD",
                        value = bug.priority.name,
                        color = getPriorityColor(bug.priority)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoTag(
                        label = "ESTADO",
                        value = bug.status.name,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                SeverityBadge(bug.severity)
            }
        }
    }
}

@Composable
fun SeverityBadge(severity: Severity) {
    val color = when (severity) {
        Severity.CRITICAL -> Color.Red
        Severity.HIGH -> Color(0xFFFFA500)
        Severity.MEDIUM -> Color.Blue
        Severity.LOW -> Color.Green
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.small,
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text = severity.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InfoTag(label: String, value: String, color: Color = Color.Gray) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}

private fun getPriorityColor(priority: com.moviles.exam2.data.model.Priority): Color {
    return when (priority) {
        com.moviles.exam2.data.model.Priority.URGENT -> Color(0xFFD32F2F)
        com.moviles.exam2.data.model.Priority.HIGH -> Color(0xFFF57C00)
        com.moviles.exam2.data.model.Priority.MEDIUM -> Color(0xFF1976D2)
        com.moviles.exam2.data.model.Priority.LOW -> Color(0xFF388E3C)
    }
}


