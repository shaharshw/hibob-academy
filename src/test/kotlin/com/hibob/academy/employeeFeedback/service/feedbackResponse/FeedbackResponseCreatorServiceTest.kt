package com.hibob.academy.employeeFeedback.service.feedbackResponse

import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.model.CreateResponseRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class FeedbackResponseCreatorServiceTest {

    private val feedbackResponseDaoMock = mock<FeedbackResponseDao>()
    private val feedbackResponseCreatorService = FeedbackResponseCreatorService(feedbackResponseDaoMock)

    @Test
    fun `test createFeedbackResponse`() {
        val responseId = 1L
        val feedbackId = 1L
        val createFeedbackResponseRequest = CreateResponseRequest("response text")

        whenever(feedbackResponseDaoMock.create(any(), any())).thenReturn(responseId)

        val result = feedbackResponseCreatorService.createFeedbackResponse(feedbackId, createFeedbackResponseRequest)

        assertEquals(responseId, result)
    }

    @Test
    fun `test createFeedbackResponse with empty text`() {
        val feedbackId = 1L
        val createFeedbackResponseRequest = CreateResponseRequest("")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackResponseCreatorService.createFeedbackResponse(feedbackId, createFeedbackResponseRequest)
        }

        assertEquals("Response text must not be blank", exception.message)
    }

    @Test
    fun `test createFeedbackResponse with less 10 characters`() {
        val feedbackId = 1L
        val createFeedbackResponseRequest = CreateResponseRequest("short")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackResponseCreatorService.createFeedbackResponse(feedbackId, createFeedbackResponseRequest)
        }

        assertEquals("Response text must be at least 10 characters long", exception.message)
    }
}