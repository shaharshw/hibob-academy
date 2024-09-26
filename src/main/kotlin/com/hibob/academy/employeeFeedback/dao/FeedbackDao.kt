package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.filter.*
import com.hibob.academy.employeeFeedback.dao.table.EmployeeTable
import com.hibob.academy.employeeFeedback.dao.table.FeedbackTable
import com.hibob.academy.employeeFeedback.model.*
import jakarta.inject.Inject
import jakarta.ws.rs.BadRequestException
import org.jooq.Condition
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
        )
    }

    private val statusResponseMapper = RecordMapper<Record, StatusResponse>
    { record ->
        StatusResponse(
            status = FeedbackStatus.valueOf(record[feedbackTable.status].uppercase())
        )
    }

    fun create(loggedInUser: LoggedInUser, feedbackRequest: CreateFeedbackRequest) : Long {
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

    fun getFeedbackById(companyId: Long, feedbackId: Long): Feedback {
        return sql.select()
            .from(feedbackTable)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .fetchOne(feedbackMapper) ?: throw BadRequestException("Feedback with $feedbackId not found")
    }

    fun getFeedbackStatusById(loggedInUser: LoggedInUser, feedbackId: Long): StatusResponse {
        val feedback = sql.select()
            .from(feedbackTable)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(loggedInUser.companyId))
            .fetchOne(feedbackMapper) ?: throw BadRequestException("Feedback with $feedbackId not found")

        if (feedback.senderId != loggedInUser.id) {
            throw BadRequestException("Logged in user is not the sender of the feedback")
        }

        return StatusResponse(status = feedback.status)
    }

    fun updateFeedbackStatus(companyId: Long, feedbackId: Long, updateFeedbackStatusRequest: UpdateFeedbackStatusRequest) : Boolean {
        return sql.update(feedbackTable)
            .set(feedbackTable.status, updateFeedbackStatusRequest.status.name)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .execute() > 0
    }

    fun getFeedbacksByFilters(companyId: Long, filters: GenericFilterRequest): List<Feedback> {
        val employeeTable = EmployeeTable.instance

        val conditions: List<Condition> = applyFilters(filters.filterList, employeeTable)

        val finalCondition = if (conditions.isEmpty()) {
            feedbackTable.companyId.eq(companyId)
        } else {
            conditions.reduce { combinedCondition, currentCondition ->
                combinedCondition.and(currentCondition)
            }.and(feedbackTable.companyId.eq(companyId))
        }

        val query = sql.select()
            .from(feedbackTable)
            .fullJoin(employeeTable).on(feedbackTable.senderId.eq(employeeTable.id))
            .where(finalCondition)

        return query.fetch(feedbackMapper)
    }

    private fun applyFilters(filters: List<Filter<*>>, employeeTable: EmployeeTable): List<Condition> {
        val feedbackConditions = filters.filterIsInstance<FeedbackFilter>().mapNotNull { it.apply(feedbackTable) }
        val employeeConditions = filters.filterIsInstance<EmployeeFilter>().mapNotNull { it.apply(employeeTable) }

        return feedbackConditions + employeeConditions
    }

}