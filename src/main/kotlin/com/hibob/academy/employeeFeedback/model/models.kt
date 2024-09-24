package com.hibob.academy.employeeFeedback.model

import java.time.LocalDate

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

data class Feedback(
    val id: Long,
    val senderId: Long?,
    val text: String,
    val isAnonymous: Boolean,
    val status: FeedbackStatus
)

data class Response(
    val id: Long,
    val feedbackId: Long,
    val responderId: Long,
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
    val date : LocalDate?,
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