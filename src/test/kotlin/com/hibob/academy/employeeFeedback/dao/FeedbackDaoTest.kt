package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.table.EmployeeTable
import com.hibob.academy.employeeFeedback.dao.table.FeedbackTable
import com.hibob.academy.employeeFeedback.model.*
import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import com.hibob.academy.employeeFeedback.dao.filter.FilterFactory

@BobDbTest
class FeedbackDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val feedbackDao = FeedbackDao(sql)
    private val table = FeedbackTable.instance
    private val companyId = 999L
    private val senderId = 1L

    private val loggedInUser = LoggedInUser(senderId, companyId)

    private val feedback1 = CreateFeedbackRequest(
        feedbackText = "Great job!",
        isAnonymous = false
    )

    private val anonymousFeedback1 = CreateFeedbackRequest(
        feedbackText = "Looks good!",
        isAnonymous = true
    )

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test create feedback and get feedback by id`() {
        val feedbackId = feedbackDao.create(loggedInUser, feedback1)

        val actualFeedback = feedbackDao.getFeedbackById(companyId, feedbackId)

        val actualFeedbackAfterConvert = actualFeedback.toCreateFeedbackRequest()

        assertTrue(feedbackId > 0)
        assertEquals(feedback1, actualFeedbackAfterConvert)
    }

    @Test
    fun `test create feedback with anonymous option`() {
        val feedbackId = feedbackDao.create(loggedInUser, anonymousFeedback1)

        val actualFeedback = feedbackDao.getFeedbackById(companyId, feedbackId)

        val actualFeedbackAfterConvert = actualFeedback.toCreateFeedbackRequest()

        assertTrue(feedbackId > 0)
        assertNull(actualFeedback.senderId)
        assertEquals(anonymousFeedback1, actualFeedbackAfterConvert)
    }

    @Test
    fun `test get feedback by id with wrong id`() {
        val feedbackId = feedbackDao.create(loggedInUser, feedback1)

        val actualFeedback = feedbackDao.getFeedbackById(loggedInUser.companyId, feedbackId)
        val actualFeedbackAfterConvert = actualFeedback.toCreateFeedbackRequest()
        assertEquals(feedback1, actualFeedbackAfterConvert)

        val exception = assertThrows<BadRequestException> {
            feedbackDao.getFeedbackById(companyId, 9999)
        }
        assertEquals("Feedback with 9999 not found", exception.message)
    }

    @Test
    fun `test get status by id`() {
        val feedbackId = feedbackDao.create(loggedInUser, feedback1)

        val status = feedbackDao.getFeedbackStatusById(loggedInUser, feedbackId)
        val expectedStatus = StatusResponse(FeedbackStatus.UNREVIEWED)

        assertEquals(expectedStatus, status)
    }

    @Test
    fun `test get status by id with wrong id`() {
        val feedbackId = feedbackDao.create(loggedInUser, feedback1)

        val status = feedbackDao.getFeedbackStatusById(loggedInUser, feedbackId)
        val expectedStatus = StatusResponse(FeedbackStatus.UNREVIEWED)

        assertEquals(expectedStatus, status)

        val exception = assertThrows<BadRequestException> {
            feedbackDao.getFeedbackStatusById(loggedInUser,9999 )
        }
        assertEquals("Feedback with 9999 not found", exception.message)
    }

    @Test
    fun `test update feedback status`() {
        val feedbackId = feedbackDao.create(loggedInUser, feedback1)

        feedbackDao.updateFeedbackStatus(companyId, feedbackId, UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED))

        val status = feedbackDao.getFeedbackStatusById(loggedInUser, feedbackId)
        val expectedStatus = StatusResponse(FeedbackStatus.REVIEWED)

        assertEquals(expectedStatus, status)
    }

    @Test
    fun `test get feedbacks with isAnonymous filter`() {
        feedbackDao.create(loggedInUser, feedback1)
        feedbackDao.create(loggedInUser, anonymousFeedback1)
        val filterParams = mapOf("isAnonymous" to true)
        val filter = FilterFactory.convertToGenericFilterRequest(filterParams)

        val feedbacks = feedbackDao.getFeedbacksByFilters(companyId, filter)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks = listOf(anonymousFeedback1)

        assertEquals(1, feedbacks.size)
        assertTrue(feedbackAfterConvert.containsAll(exceptedFeedbacks))
    }

    @Test
    fun `test get feedbacks with status filter`() {
        val feedbackID = feedbackDao.create(loggedInUser, feedback1)
        feedbackDao.create(loggedInUser, anonymousFeedback1)

        feedbackDao.updateFeedbackStatus(companyId, feedbackID, UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED))

        val filterParams = mapOf("status" to FeedbackStatus.REVIEWED.name)
        val filter = FilterFactory.convertToGenericFilterRequest(filterParams)

        val feedbacks = feedbackDao.getFeedbacksByFilters(companyId, filter)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks = listOf(feedback1)

        assertEquals(1, feedbacks.size)
        assertTrue(feedbackAfterConvert.containsAll(exceptedFeedbacks))
    }

    @Test
    fun `test get feedbacks with date filter`() {
        feedbackDao.create(loggedInUser, feedback1)
        feedbackDao.create(loggedInUser, anonymousFeedback1)

        val filterParams1 = mapOf("date" to "2024-09-22")
        val filter1 = FilterFactory.convertToGenericFilterRequest(filterParams1)

        val filterParams2 = mapOf("date" to "2026-09-21")
        val filter2 = FilterFactory.convertToGenericFilterRequest(filterParams2)

        val feedbacks1 = feedbackDao.getFeedbacksByFilters(companyId, filter1)
        val feedbackAfterConvert1 = feedbacks1.map { it.toCreateFeedbackRequest() }.toSet()
        val exceptedFeedbacks1 = setOf(anonymousFeedback1, feedback1)

        val feedbacks2 = feedbackDao.getFeedbacksByFilters(companyId, filter2)
        val feedbackAfterConvert2 = feedbacks2.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks2 = emptyList<CreateFeedbackRequest>()

        assertEquals(2, feedbacks1.size)
        assertTrue(feedbackAfterConvert1.containsAll(exceptedFeedbacks1))

        assertEquals(0, feedbacks2.size)
        assertEquals(exceptedFeedbacks2, feedbackAfterConvert2)
    }

    @Test
    fun `test get feedbacks with department filter`() {
        feedbackDao.create(loggedInUser, feedback1)

        val employeeTable = EmployeeTable.instance
        val employeeId = sql.insertInto(employeeTable)
            .set(employeeTable.companyId, companyId)
            .set(employeeTable.department, Department.HR.name)
            .set(employeeTable.firstName, "Shahar")
            .set(employeeTable.lastName, "Shwartz")
            .set(employeeTable.role, Role.EMPLOYEE.name)
            .returning(employeeTable.id)
            .fetchOne()
            ?.getValue(employeeTable.id)

        val loggedInUser2 = LoggedInUser(employeeId!!, companyId)

        feedbackDao.create(loggedInUser2, feedback1)

        val filterParams = mapOf("department" to Department.HR.name)
        val filter = FilterFactory.convertToGenericFilterRequest(filterParams)

        val feedbacks = feedbackDao.getFeedbacksByFilters(companyId, filter)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks = listOf(feedback1)

        assertEquals(1, feedbacks.size)
        assertTrue(feedbackAfterConvert.containsAll(exceptedFeedbacks))

        sql.delete(employeeTable).where(employeeTable.id.eq(employeeId)).execute()
    }

    @Test
    fun `test get feedback without filters`() {
        feedbackDao.create(loggedInUser, feedback1)
        feedbackDao.create(loggedInUser, anonymousFeedback1)

        val filterParams = emptyMap<String, Any>()
        val filters = FilterFactory.convertToGenericFilterRequest(filterParams)

        val feedbacks = feedbackDao.getFeedbacksByFilters(companyId, filters)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }.toSet()
        val exceptedFeedbacks = setOf(anonymousFeedback1, feedback1)

        assertEquals(2, feedbacks.size)
        assertTrue(feedbackAfterConvert.containsAll(exceptedFeedbacks))
    }

    private fun Feedback.toCreateFeedbackRequest() = CreateFeedbackRequest(
        feedbackText = text,
        isAnonymous = isAnonymous
    )
}