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

// Data class

data class Feedback(
    val id: Long,
    val companyId: Long,
    val text: String,
    val senderId: Long,
    val receiverId: Long,
    val isAnonymous: Boolean,
    val status: FeedbackStatus,
    val createdAt: LocalDate
)

data class Respond(
    val id: Long,
    val companyId: Long,
    val feedbackId: Long,
    val responderId: Long,
    val text: String,
    val createdAt: LocalDate
)

data class CreateFeedbackRequest(
    val companyId: Long,
    val senderId: Long,
    val receiverId: Long,
    val feedbackText: String,
    val isAnonymous: Boolean
)

data class FilerFeedbackRequest(
    val data : LocalDate?,
    val department: Department?,
    val status: FeedbackStatus?,
    val isAnonymous: Boolean?
)

data class FeedbackResponse(
    val id: Long,
    val companyId: Long,
    val text: String,
    val name: String,
    val status: FeedbackStatus,
    val createdAt: LocalDate
)

data class FeedbacksResponse(
    val feedbacks: List<FeedbackResponse>
)

data class CreateRespondRequest(
    val text: String
)

data class StatusResponse(
    val status: String
)

data class RespondsResponse(
    val responses: List<Respond>
)

