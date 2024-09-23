package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.table.FeedbackTable
import com.hibob.academy.employeeFeedback.model.*
import jakarta.inject.Inject
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class FeedbackDao @Inject constructor(
    private val sql: DSLContext
) {

    private val feedbackTable = FeedbackTable.instance

    private val feedbackMapper = RecordMapper<Record, Feedback>
    { record ->
        Feedback(
            id = record[feedbackTable.id],
            text = record[feedbackTable.text],
            senderId = record[feedbackTable.senderId],
            isAnonymous = record[feedbackTable.isAnonymous],
            status = FeedbackStatus.valueOf(record[feedbackTable.status].uppercase()),
            createdAt = record[feedbackTable.createdAt].toLocalDate(),
            lastModifiedAt = record[feedbackTable.lastModifiedAt].toLocalDate()
        )
    }

    private val statusResponseMapper = RecordMapper<Record, StatusResponse>
    { record ->
        StatusResponse(
            status = FeedbackStatus.valueOf(record[feedbackTable.status].uppercase())
        )
    }

    fun create(feedbackRequest: CreateFeedbackRequest, loggedInUser: LoggedInUser) : Long {
        val senderId = if (feedbackRequest.isAnonymous) null else loggedInUser.id

        val record = sql.insertInto(feedbackTable)
            .set(feedbackTable.companyId, loggedInUser.companyId)
            .set(feedbackTable.text, feedbackRequest.feedbackText)
            .set(feedbackTable.senderId, senderId)
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
            .fetchOne(statusResponseMapper) ?: throw BadRequestException("Feedback with $feedbackId not found")
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