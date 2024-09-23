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
    val status: FeedbackStatus
)

data class Respond(
    val id: Long,
    val feedbackId: Long,
    val text: String,
)

data class LoggedInUser(
    val id: Long,
    val companyId: Long,
)

data class LoggedInUserWithRole(
    val id: Long,
    val companyId: Long,
    val role: Role
)

data class CreateFeedbackRequest(
    val feedbackText: String,
    val isAnonymous: Boolean
)

data class FilterFeedbackRequest(
    val data : LocalDate?,
    val department: Department?,
    val status: FeedbackStatus?,
    val isAnonymous: Boolean?
)

data class FeedbacksResponse(
    val feedbacks: List<Feedback>
)

data class CreateRespondRequest(
    val text: String
)

data class StatusResponse(
    val status: FeedbackStatus
)

data class UpdateFeedbackStatusRequest(
    val status: FeedbackStatus
)

data class UpdateFeedbackResponseRequest(
    val responseText: String,
    val append: Boolean = false
)

