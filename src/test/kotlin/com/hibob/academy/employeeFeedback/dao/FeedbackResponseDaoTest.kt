package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.table.FeedbackResponseTable
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
class FeedbackResponseDaoTest@Autowired constructor(private val sql: DSLContext) {

    private val feedbackResponseDao = FeedbackResponseDao(sql)
    private val feedbackDao = FeedbackDao(sql)
    private val feedbackResponseTable = FeedbackResponseTable.instance
    private val companyId = 999L
    private val senderId = 1L

    private val loggedInUser = LoggedInUser(senderId, companyId)

    lateinit var response1: CreateResponseRequestWithFeedbackId
    lateinit var response2: CreateResponseRequestWithFeedbackId

    private val feedback1 = CreateFeedbackRequest(
        feedbackText = "Great job!",
        isAnonymous = false
    )

    private val feedback2Anonymous = CreateFeedbackRequest(
        feedbackText = "Looks good!",
        isAnonymous = true
    )

    @BeforeEach
    fun createFeedback() {
        val feedbackId1 = feedbackDao.create(loggedInUser, feedback1)
        val feedbackId2 = feedbackDao.create(loggedInUser, feedback2Anonymous)

        response1 = CreateResponseRequestWithFeedbackId(
            feedbackId = feedbackId1,
            text = "Great job!"
        )

        response2 = CreateResponseRequestWithFeedbackId(
            feedbackId = feedbackId2,
            text = "Looks good!"
        )
    }

    @AfterEach
    fun cleanupFeedback() {
        val feedbackTable = FeedbackTable.instance
        sql.deleteFrom(feedbackTable).where(feedbackTable.companyId.eq(companyId)).execute()
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(feedbackResponseTable).where(feedbackResponseTable.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test create response`() {

        val responseId = feedbackResponseDao.create(loggedInUser, response1)

        val actualRespond = feedbackResponseDao.getResponseById(companyId, responseId)
        val actualRespondAfterConvert = actualRespond.toCreateRespondRequest()

        assertTrue(responseId > 0)
        assertEquals(response1, actualRespondAfterConvert)
    }

    @Test
    fun `test create response with existing response`() {
        feedbackResponseDao.create(loggedInUser, response1)

        assertThrows<BadRequestException> {
            feedbackResponseDao.create(loggedInUser, response1)
        }
    }

    @Test
    fun `test get response by id`() {
        val responseId = feedbackResponseDao.create(loggedInUser, response1)

        val actualRespond = feedbackResponseDao.getResponseById(companyId, responseId)
        val actualRespondAfterConvert = actualRespond.toCreateRespondRequest()

        assertEquals(response1, actualRespondAfterConvert)
    }

    @Test
    fun `test get response by id with wrong id`() {
        assertThrows<BadRequestException> {
            feedbackResponseDao.getResponseById(companyId, 999)
        }
    }

    @Test
    fun `test update response with append text`() {
        val responseId = feedbackResponseDao.create(loggedInUser, response1)

        val updateFeedbackResponseRequest = UpdateFeedbackResponseRequest(
            responseId = responseId,
            responseText = "I agree!",
            append = true
        )

        val isUpdated = feedbackResponseDao.updateResponse(loggedInUser, updateFeedbackResponseRequest)

        val actualRespond = feedbackResponseDao.getResponseById(companyId, responseId)
        val actualRespondAfterConvert = actualRespond.toCreateRespondRequest()
        val exceptedRespond = response1.copy(text = "Great job!\nI agree!")

        assertTrue(isUpdated)
        assertEquals(exceptedRespond, actualRespondAfterConvert)
    }

    @Test
    fun `test update response with override text`() {
        val responseId = feedbackResponseDao.create(loggedInUser, response1)

        val updateFeedbackResponseRequest = UpdateFeedbackResponseRequest(
            responseId = responseId,
            responseText = "I agree!",
            append = false
        )

        val isUpdated = feedbackResponseDao.updateResponse(loggedInUser, updateFeedbackResponseRequest)

        val actualRespond = feedbackResponseDao.getResponseById(companyId, responseId)
        val actualRespondAfterConvert = actualRespond.toCreateRespondRequest()
        val exceptedRespond = response1.copy(text = "I agree!")

        assertTrue(isUpdated)
        assertEquals(exceptedRespond, actualRespondAfterConvert)
    }

    private fun Response.toCreateRespondRequest(): CreateResponseRequestWithFeedbackId {
        return CreateResponseRequestWithFeedbackId(
            feedbackId = feedbackId,
            text = text
        )
    }
}
