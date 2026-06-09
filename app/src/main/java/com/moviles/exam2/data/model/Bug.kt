package com.moviles.exam2.data.model

import java.util.Date

enum class Severity {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum class Priority {
    LOW, MEDIUM, HIGH, URGENT
}

enum class BugStatus {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED
}

data class Bug(
    val id: String,
    val title: String,
    val severity: Severity,
    val priority: Priority,
    val status: BugStatus,
    val affectedSystem: String,
    val createdAt: Date,
    val category: String
)
