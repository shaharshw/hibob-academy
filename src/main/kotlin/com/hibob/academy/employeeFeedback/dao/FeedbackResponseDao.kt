package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.table.FeedbackResponseTable
import com.hibob.academy.employeeFeedback.model.CreateResponseRequestWithFeedbackId
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Response
import com.hibob.academy.employeeFeedback.model.UpdateFeedbackResponseRequest
import jakarta.inject.Inject
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class FeedbackResponseDao @Inject constructor(
    private val sql: DSLContext
) {

    private val feedbackResponseTable = FeedbackResponseTable.instance

    private val feedbackResponseMapper = RecordMapper<Record, Response>
    { record ->
        Response(
            id = record[feedbackResponseTable.id],
            feedbackId = record[feedbackResponseTable.feedbackId],
            responderId = record[feedbackResponseTable.responderId],
            text = record[feedbackResponseTable.text],
        )
    }

    fun create(loggedInUser: LoggedInUser, createRespondRequest: CreateResponseRequestWithFeedbackId) : Long {
        val record = sql.insertInto(feedbackResponseTable)
            .set(feedbackResponseTable.companyId, loggedInUser.companyId)
            .set(feedbackResponseTable.responderId, loggedInUser.id)
            .set(feedbackResponseTable.feedbackId, createRespondRequest.feedbackId)
            .set(feedbackResponseTable.text, createRespondRequest.text)
            .onConflict(feedbackResponseTable.companyId, feedbackResponseTable.feedbackId, feedbackResponseTable.responderId)
            .doNothing()
            .returning(feedbackResponseTable.id)
            .fetchOne() ?: throw BadRequestException("A response for feedback with ID: ${createRespondRequest.feedbackId} " +
                "already exists in the company with ID: ${loggedInUser.companyId} from user with ID: ${loggedInUser.id}.")

        return record[feedbackResponseTable.id]
    }

    fun getResponseById(companyId: Long, responseId: Long): Response {
        return sql.select()
            .from(feedbackResponseTable)
            .where(feedbackResponseTable.id.eq(responseId))
            .and(feedbackResponseTable.companyId.eq(companyId))
            .fetchOne(feedbackResponseMapper) ?: throw BadRequestException("Respond with $responseId not found")
    }

    fun updateResponse(loggedInUser: LoggedInUser, updateFeedbackResponseRequest: UpdateFeedbackResponseRequest): Boolean {
        val newText = if (updateFeedbackResponseRequest.append) {
            DSL.concat(feedbackResponseTable.text, DSL.inline("\n"), DSL.inline(updateFeedbackResponseRequest.responseText))
        } else {
            DSL.inline(updateFeedbackResponseRequest.responseText)
        }

        val updateQuery = sql.update(feedbackResponseTable)
            .set(feedbackResponseTable.text, newText)
            .set(feedbackResponseTable.lastModifiedAt, DSL.currentTimestamp())
            .where(feedbackResponseTable.id.eq(updateFeedbackResponseRequest.responseId))
            .and(feedbackResponseTable.companyId.eq(loggedInUser.companyId))
            .and(feedbackResponseTable.responderId.eq(loggedInUser.id))

        return updateQuery.execute() > 0
    }

    fun getAllResponsesByFeedbackId(companyId: Long, feedbackId: Long): List<Response> {
        return sql.select()
            .from(feedbackResponseTable)
            .where(feedbackResponseTable.feedbackId.eq(feedbackId))
            .and(feedbackResponseTable.companyId.eq(companyId))
            .fetch(feedbackResponseMapper)
    }
}