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
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

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

        val status = feedbackDao.getFeedbackStatusById(loggedInUser.companyId, feedbackId)
        val expectedStatus = StatusResponse(FeedbackStatus.UNREVIEWED)

        assertEquals(expectedStatus, status)
    }

    @Test
    fun `test get status by id with wrong id`() {
        val feedbackId = feedbackDao.create(loggedInUser, feedback1)

        val status = feedbackDao.getFeedbackStatusById(loggedInUser.companyId, feedbackId)
        val expectedStatus = StatusResponse(FeedbackStatus.UNREVIEWED)

        assertEquals(expectedStatus, status)

        val exception = assertThrows<BadRequestException> {
            feedbackDao.getFeedbackStatusById(loggedInUser.companyId,9999 )
        }
        assertEquals("Feedback with 9999 not found", exception.message)
    }

    @Test
    fun `test update feedback status`() {
        val feedbackId = feedbackDao.create(loggedInUser, feedback1)

        feedbackDao.updateFeedbackStatus(companyId, feedbackId, UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED))

        val status = feedbackDao.getFeedbackStatusById(companyId, feedbackId)
        val expectedStatus = StatusResponse(FeedbackStatus.REVIEWED)

        assertEquals(expectedStatus, status)
    }

    @Test
    fun `test get feedbacks with isAnonymous filter`() {
        feedbackDao.create(loggedInUser, feedback1)
        feedbackDao.create(loggedInUser, anonymousFeedback1)

        val filter = FilterFeedbackRequest(
            date = null,
            department = null,
            status = null,
            isAnonymous = true
        )

        val feedbacks = feedbackDao.getFeedbacksByFilters(companyId, filter)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks = listOf(anonymousFeedback1)

        assertEquals(1, feedbacks.size)
        assertEquals(exceptedFeedbacks, feedbackAfterConvert)
    }

    @Test
    fun `test get feedbacks with status filter`() {
        val feedbackID = feedbackDao.create(loggedInUser, feedback1)
        feedbackDao.create(loggedInUser, anonymousFeedback1)

        feedbackDao.updateFeedbackStatus(companyId, feedbackID, UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED))

        val filter = FilterFeedbackRequest(
            date = null,
            department = null,
            status = FeedbackStatus.REVIEWED,
            isAnonymous = null
        )

        val feedbacks = feedbackDao.getFeedbacksByFilters(companyId, filter)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks = listOf(feedback1)

        assertEquals(1, feedbacks.size)
        assertEquals(exceptedFeedbacks, feedbackAfterConvert)
    }

    @Test
    fun `test get feedbacks with date filter`() {
        feedbackDao.create(loggedInUser, feedback1)
        feedbackDao.create(loggedInUser, anonymousFeedback1)

        val filter = FilterFeedbackRequest(
            date =  LocalDate.of(2024, 9, 22),
            department = null,
            status = null,
            isAnonymous = null
        )

        val filter2 = FilterFeedbackRequest(
            date =  LocalDate.of(2026, 9, 21),
            department = null,
            status = null,
            isAnonymous = null
        )

        val feedbacks1 = feedbackDao.getFeedbacksByFilters(companyId, filter)
        val feedbackAfterConvert = feedbacks1.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks = listOf(feedback1, anonymousFeedback1)

        val feedbacks2 = feedbackDao.getFeedbacksByFilters(companyId, filter2)
        val feedbackAfterConvert2 = feedbacks2.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks2 = emptyList<CreateFeedbackRequest>()

        assertEquals(2, feedbacks1.size)
        assertEquals(exceptedFeedbacks, feedbackAfterConvert)

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

        val filter = FilterFeedbackRequest(
            date = null,
            department = Department.HR,
            status = null,
            isAnonymous = null
        )

        val feedbacks = feedbackDao.getFeedbacksByFilters(companyId, filter)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks = listOf(feedback1)

        assertEquals(1, feedbacks.size)
        assertEquals(exceptedFeedbacks, feedbackAfterConvert)

        sql.delete(employeeTable).where(employeeTable.id.eq(employeeId)).execute()
    }

    @Test
    fun `test get feedback without filters`() {
        feedbackDao.create(loggedInUser, feedback1)
        feedbackDao.create(loggedInUser, anonymousFeedback1)

        val filters = FilterFeedbackRequest(
            date = null,
            department = null,
            status = null,
            isAnonymous = null
        )

        val feedbacks = feedbackDao.getFeedbacksByFilters(companyId, filters)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }
        val expectedFeedbacks = listOf(feedback1, anonymousFeedback1)

        assertEquals(2, feedbacks.size)
        assertEquals(expectedFeedbacks, feedbackAfterConvert)
    }

    private fun Feedback.toCreateFeedbackRequest() = CreateFeedbackRequest(
        feedbackText = text,
        isAnonymous = isAnonymous
    )
}

