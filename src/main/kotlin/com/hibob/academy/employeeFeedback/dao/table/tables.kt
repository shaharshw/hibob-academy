package com.hibob.academy.employeeFeedback.dao.table

import com.hibob.academy.utils.JooqTable

class EmployeeTable(tableName: String = "employees") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val companyId = createBigIntField("company_id")
    val firstName = createVarcharField("first_name")
    val lastName = createVarcharField("last_name")
    val role = createVarcharField("role")
    val department = createVarcharField("department")

    companion object {
        val instance = EmployeeTable()
    }
}

class FeedbackTable(tableName: String = "feedback") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val companyId = createBigIntField("company_id")
    val text = createVarcharField("text")
    val senderId = createBigIntField("sender_id")
    val isAnonymous = createBooleanField("is_anonymous")
    val status = createVarcharField("status")
    val createdAt = createDateField("created_at")
    val lastModifiedAt = createDateField("last_modified_at")

    companion object {
        val instance = FeedbackTable()
    }
}

class RespondTable(tableName: String = "respond") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val companyId = createBigIntField("company_id")
    val feedbackId = createBigIntField("feedback_id")
    val responderId = createBigIntField("reviewer_id")
    val text = createVarcharField("response_text")
    val createdAt = createDateField("created_at")
    val lastModifiedAt = createDateField("last_modified_at")

    companion object {
        val instance = RespondTable()
    }
}