package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.table.FeedbackResponseTable
import com.hibob.academy.employeeFeedback.model.CreateRespondRequest
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Respond
import jakarta.inject.Inject
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record


class FeedbackResponseDao @Inject constructor(
    private val sql: DSLContext
) {

    private val feedbackResponseTable = FeedbackResponseTable.instance

    private val feedbackResponseMapper = RecordMapper<Record, Respond>
    { record ->
        Respond(
            id = record[feedbackResponseTable.id],
            feedbackId = record[feedbackResponseTable.feedbackId],
            responderId = record[feedbackResponseTable.responderId],
            text = record[feedbackResponseTable.text],
            createdAt = record[feedbackResponseTable.createdAt].toLocalDate(),
            lastModifiedAt = record[feedbackResponseTable.lastModifiedAt].toLocalDate()
        )
    }

    fun create(createRespondRequest: CreateRespondRequest, loggedInUser: LoggedInUser) : Long {
        val record = sql.insertInto(feedbackResponseTable)
            .set(feedbackResponseTable.companyId, loggedInUser.companyId)
            .set(feedbackResponseTable.responderId, loggedInUser.id)
            .set(feedbackResponseTable.feedbackId, createRespondRequest.feedbackId)
            .set(feedbackResponseTable.text, createRespondRequest.text)
            .returning(feedbackResponseTable.id)
            .fetchOne() ?: throw BadRequestException("Failed to create respond")

        return record[feedbackResponseTable.id]
    }


}
