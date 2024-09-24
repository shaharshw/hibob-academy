package com.hibob.academy.employeeFeedback.model

import com.hibob.nullability.Department
import java.time.LocalDate

// Enum

enum class FeedbackStatus {
    UNREVIEWED,
    REVIEWED
}

enum class Department {
    HR,
    OPERATIONS,
    DEVELOPMENT,
    MANAGEMENT
}

enum class Role {
    EMPLOYEE,
    HR,
    ADMIN
}

// Data class

data class Feedback(
    val id: Long,
    val senderId: Long?,
    val text: String,
    val isAnonymous: Boolean,
    val status: FeedbackStatus,
    val createdAt: LocalDate,
    val lastModifiedAt: LocalDate
)

data class Response(
    val id: Long,
    val feedbackId: Long,
    val responderId: Long,
    val text: String,
    val createdAt: LocalDate,
    val lastModifiedAt: LocalDate
)

data class LoggedInUser(
    val id: Long,
    val companyId: Long,
)

data class CreateFeedbackRequest(
    val feedbackText: String,
    val isAnonymous: Boolean
)

data class FilerFeedbackRequest(
    val data : LocalDate?,
    val department: Department?,
    val status: FeedbackStatus?,
    val isAnonymous: Boolean?
)

data class FeedbacksResponse(
    val feedbacks: List<Feedback>
)

data class CreateResponseRequest(
    val text: String
)

data class CreateResponseRequestWithFeedbackId(
    val feedbackId: Long,
    val text: String
)

data class StatusResponse(
    val status: FeedbackStatus
)

data class UpdateFeedbackStatusRequest(
    val status: FeedbackStatus
)

data class UpdateFeedbackResponseRequest(
    val responseId: Long,
    val responseText: String,
    val append: Boolean = false
)

