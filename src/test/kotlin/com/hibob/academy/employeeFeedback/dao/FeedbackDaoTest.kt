package com.hibob.academy.employeeFeedback.dao

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
        val feedbackId = feedbackDao.create(feedback1, loggedInUser)

        val actualFeedback = feedbackDao.getFeedbackById(feedbackId, companyId)

        val actualFeedbackAfterConvert = actualFeedback.toCreateFeedbackRequest()

        assertTrue(feedbackId > 0)
        assertEquals(feedback1, actualFeedbackAfterConvert)
    }

    @Test
    fun `test create feedback with anonymous option`() {
        val feedbackId = feedbackDao.create(anonymousFeedback1, loggedInUser)

        val actualFeedback = feedbackDao.getFeedbackById(feedbackId, companyId)

        val actualFeedbackAfterConvert = actualFeedback.toCreateFeedbackRequest()

        assertTrue(feedbackId > 0)
        assertNull(actualFeedback.senderId)
        assertEquals(anonymousFeedback1, actualFeedbackAfterConvert)
    }

    @Test
    fun `test get feedback by id with wrong id`() {
        val feedbackId = feedbackDao.create(feedback1, loggedInUser)

        val actualFeedback = feedbackDao.getFeedbackById(feedbackId, loggedInUser.companyId)
        val actualFeedbackAfterConvert = actualFeedback.toCreateFeedbackRequest()
        assertEquals(feedback1, actualFeedbackAfterConvert)

        val exception = assertThrows<BadRequestException> {
            feedbackDao.getFeedbackById(9999, companyId)
        }
        assertEquals("Feedback with 9999 not found", exception.message)
    }

    @Test
    fun `test get status by id`() {
        val feedbackId = feedbackDao.create(feedback1, loggedInUser)

        val status = feedbackDao.getFeedbackStatusById(feedbackId, loggedInUser.companyId)
        val expectedStatus = StatusResponse(FeedbackStatus.UNREVIEWED)

        assertEquals(expectedStatus, status)
    }

    @Test
    fun `test get status by id with wrong id`() {
        val feedbackId = feedbackDao.create(feedback1, loggedInUser)

        val status = feedbackDao.getFeedbackStatusById(feedbackId, loggedInUser.companyId)
        val expectedStatus = StatusResponse(FeedbackStatus.UNREVIEWED)

        assertEquals(expectedStatus, status)

        val exception = assertThrows<BadRequestException> {
            feedbackDao.getFeedbackStatusById(9999, loggedInUser.companyId)
        }
        assertEquals("Feedback with 9999 not found", exception.message)
    }

    @Test
    fun `test update feedback status`() {
        val feedbackId = feedbackDao.create(feedback1, loggedInUser)

        feedbackDao.updateFeedbackStatus(feedbackId, companyId, UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED))

        val status = feedbackDao.getFeedbackStatusById(feedbackId, companyId)
        val expectedStatus = StatusResponse(FeedbackStatus.REVIEWED)

        assertEquals(expectedStatus, status)
    }

    @Test
    fun `test update status by id with wrong id`() {
        feedbackDao.create(feedback1, loggedInUser)

        val exception = assertThrows<BadRequestException> {
            feedbackDao.updateFeedbackStatus(9999, companyId, UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED))
        }
        assertEquals("Feedback with 9999 not found", exception.message)
    }

    @Test
    fun `test get feedbacks without filter`() {
        feedbackDao.create(feedback1, loggedInUser)
        feedbackDao.create(anonymousFeedback1, loggedInUser)

        val feedbacks = feedbackDao.getFeedbacks(companyId)
        val feedbackAfterConvert = feedbacks.map { it.toCreateFeedbackRequest() }
        val exceptedFeedbacks = listOf(feedback1, anonymousFeedback1)

        assertEquals(2, feedbacks.size)
        assertEquals(exceptedFeedbacks, feedbackAfterConvert)

    }

    @Test
    fun `test get feedback and return empty list`() {
        val feedbacks = feedbackDao.getFeedbacks(companyId)
        assertTrue(feedbacks.isEmpty())
    }

    private fun Feedback.toCreateFeedbackRequest() = CreateFeedbackRequest(
        feedbackText = text,
        isAnonymous = isAnonymous
    )
}
