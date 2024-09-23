package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.table.EmployeesTable
import com.hibob.academy.employeeFeedback.dao.table.FeedbackTable
import com.hibob.academy.employeeFeedback.model.*
import com.hibob.academy.utils.JooqTable
import jakarta.inject.Inject
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class FeedbackDao @Inject constructor(
    private val sql: DSLContext
) {

    private val feedbackTable = FeedbackTable.instance

    private val feedbackMapper = RecordMapper<Record, Feedback>
    { record ->
        Feedback(
            id = record[feedbackTable.id],
            companyId = record[feedbackTable.companyId],
            text = record[feedbackTable.text],
            senderId = record[feedbackTable.senderId],
            isAnonymous = record[feedbackTable.isAnonymous],
            status = FeedbackStatus.valueOf(record[feedbackTable.status].uppercase()),
            createdAt = record[feedbackTable.createdAt].toLocalDate(),
            lastModifiedAt = record[feedbackTable.lastModifiedAt].toLocalDate()
        )
    }

    private val StatusResponseMapper = RecordMapper<Record, StatusResponse>
    { record ->
        StatusResponse(
            status = FeedbackStatus.valueOf(record[feedbackTable.status].uppercase())
        )
    }

    fun create(feedbackRequest: CreateFeedbackRequest) : Long {
        val record = sql.insertInto(feedbackTable)
            .set(feedbackTable.companyId, feedbackRequest.companyId)
            .set(feedbackTable.text, feedbackRequest.feedbackText)
            .set(feedbackTable.senderId, feedbackRequest.senderId)
            .set(feedbackTable.isAnonymous, feedbackRequest.isAnonymous)
            .returning(feedbackTable.id)
            .fetchOne() ?: throw BadRequestException("Failed to create feedback")

        return record[feedbackTable.id]
    }

    fun getFeedbackById(feedbackId: Long, companyId: Long): Feedback {
        return sql.select()
            .from(feedbackTable)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .fetchOne(feedbackMapper) ?: throw BadRequestException("Feedback with $feedbackId not found")
    }

    fun getFeedbackStatusById(feedbackId: Long, companyId: Long) : StatusResponse {
        return sql.select(feedbackTable.status)
            .from(feedbackTable)
            .where(feedbackTable.companyId.eq(companyId))
            .and(feedbackTable.id.eq(feedbackId))
            .fetchOne(StatusResponseMapper) ?: throw BadRequestException("Feedback with $feedbackId not found")
    }

    fun updateFeedbackStatus(feedbackId: Long, companyId: Long, updateFeedbackStatusRequest: UpdateFeedbackStatusRequest) {
        val updated = sql.update(feedbackTable)
            .set(feedbackTable.status, updateFeedbackStatusRequest.status.name)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .execute()

        if (updated == 0) {
            throw BadRequestException("Feedback with $feedbackId not found")
        }
    }

    fun getFeedbacks(companyId: Long) : List<Feedback> {
        return sql.select()
            .from(feedbackTable)
            .where(feedbackTable.companyId.eq(companyId))
            .fetch(feedbackMapper)
    }
}