package com.hibob.academy.employeeFeedback.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.hibob.academy.employeeFeedback.dao.filter.Filter
import com.hibob.academy.employeeFeedback.utils.GenericFilterRequestDeserializer
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
    ADMIN;

    companion object {
        val PERMISSIONS = mapOf(
            Permission.EMPLOYEES_PERMISSION to setOf(EMPLOYEE, HR, ADMIN),
            Permission.ADMIN_PERMISSION to setOf(HR, ADMIN),
            Permission.HR_PERMISSION to setOf(HR)
        )
    }
}

enum class Permission {
    EMPLOYEES_PERMISSION,
    HR_PERMISSION,
    ADMIN_PERMISSION
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

data class CreateFeedbackRequest(
    val feedbackText: String,
    val isAnonymous: Boolean
)

@JsonDeserialize(using = GenericFilterRequestDeserializer::class)
data class GenericFilterRequest(
    val filterList: List<Filter<*>>
)

data class DateRange(
    val fromDate: LocalDate? = null,
    val untilDate: LocalDate? = null
) {
    init {
        if (fromDate != null && untilDate != null && fromDate.isAfter(untilDate)) {
            throw IllegalArgumentException("The 'fromDate' must be before 'untilDate'")
        }
    }
}

data class ResponseCreationRequest(
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