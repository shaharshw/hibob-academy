package com.hibob.academy.employeeFeedback.dao

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
import java.sql.Date

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

    fun getFeedbackById(companyId: Long, feedbackId: Long): Feedback {
        return sql.select()
            .from(feedbackTable)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .fetchOne(feedbackMapper) ?: throw BadRequestException("Feedback with $feedbackId not found")
    }

    fun getFeedbackStatusById(companyId: Long, feedbackId: Long) : StatusResponse {
        return sql.select(feedbackTable.status)
            .from(feedbackTable)
            .where(feedbackTable.companyId.eq(companyId))
            .and(feedbackTable.id.eq(feedbackId))
            .fetchOne(statusResponseMapper) ?: throw BadRequestException("Feedback with $feedbackId not found")
    }

    fun updateFeedbackStatus(companyId: Long, feedbackId: Long, updateFeedbackStatusRequest: UpdateFeedbackStatusRequest) : Boolean {
        return sql.update(feedbackTable)
            .set(feedbackTable.status, updateFeedbackStatusRequest.status.name)
            .where(feedbackTable.id.eq(feedbackId))
            .and(feedbackTable.companyId.eq(companyId))
            .execute() > 0
    }

    fun getFeedbacksByFilters(companyId: Long, filters: FilterFeedbackRequest): List<Feedback> {
        val employeeTable = EmployeeTable.instance
        val conditions = mutableListOf<Condition>()

        filters.department?.let {
            conditions.add(employeeTable.department.eq(it.name))
        }
        filters.date?.let {
            val sqlDate = Date.valueOf(it)
            conditions.add(feedbackTable.createdAt.gt(sqlDate))
        }
        filters.status?.let {
            conditions.add(feedbackTable.status.eq(it.name))
        }
        filters.isAnonymous?.let {
            conditions.add(feedbackTable.isAnonymous.eq(it))
        }

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
            .where(feedbackTable.companyId.eq(companyId).and(finalCondition))

        return query.fetch(feedbackMapper)
    }




}