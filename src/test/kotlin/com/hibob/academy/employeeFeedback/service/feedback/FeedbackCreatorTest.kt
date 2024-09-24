package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.CreateFeedbackRequest
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FeedbackCreatorTest {

    private val feedbackDaoMock = mock<FeedbackDao> {}
    private val feedbackCreatorService = FeedbackCreator(feedbackDaoMock)
    private val loggedInUser = LoggedInUser(1L, 1L)

    @Test
    fun `test createFeedback`() {

        val createFeedbackRequest = CreateFeedbackRequest("This is a feedback", false)

        whenever(feedbackDaoMock.create(any(), any())).thenReturn(1L)

        val feedbackId = feedbackCreatorService.createFeedback(loggedInUser, createFeedbackRequest)

        assertEquals(1L, feedbackId)
    }

    @Test
    fun `createFeedback should throw exception when feedback text is blank`() {
        val createFeedbackRequest = CreateFeedbackRequest("", false)
        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackCreatorService.createFeedback(loggedInUser, createFeedbackRequest)
        }

        assertEquals("Feedback text must not be blank", exception.message)
    }

    @Test
    fun `createFeedback should throw exception when feedback text is shorter than 10 characters`() {
        val createFeedbackRequest = CreateFeedbackRequest("123456789", false)
        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackCreatorService.createFeedback(loggedInUser, createFeedbackRequest)
        }

        assertEquals("Feedback text must be longer than 10 characters", exception.message)
    }
}