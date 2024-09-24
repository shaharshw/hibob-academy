package com.hibob.academy.employeeFeedback.service.feedbackResponse

import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.model.UpdateFeedbackResponseRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class FeedbackResponseUpdateServiceTest {

    private val feedbackResponseDaoMock = mock<FeedbackResponseDao>()
    private val feedbackResponseUpdateService = FeedbackResponseUpdateService(feedbackResponseDaoMock)

    @Test
    fun `test updateFeedbackResponse`() {
        val feedbackId = 1L
        val responseId = 1L
        val updateFeedbackResponseRequest = UpdateFeedbackResponseRequest(responseId,"response text", false)

        whenever(feedbackResponseDaoMock.updateResponse(any(), any())).thenReturn(true)

        val result = feedbackResponseUpdateService.updateFeedbackResponse(feedbackId, updateFeedbackResponseRequest)

        verify(feedbackResponseDaoMock).updateResponse(any(), any())
        assertTrue(result)
    }

    @Test
    fun `test updateFeedbackResponse with invalid response text when append is false`() {
        val feedbackId = 1L
        val responseId = 1L
        val updateFeedbackResponseRequest = UpdateFeedbackResponseRequest(responseId,"", false)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackResponseUpdateService.updateFeedbackResponse(feedbackId, updateFeedbackResponseRequest)
        }

        assertEquals("Response text must not be blank", exception.message)
    }

    @Test
    fun `test updateFeedbackResponse with invalid response text length when append is false`() {
        val feedbackId = 1L
        val responseId = 1L
        val updateFeedbackResponseRequest = UpdateFeedbackResponseRequest(responseId,"short", false)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackResponseUpdateService.updateFeedbackResponse(feedbackId, updateFeedbackResponseRequest)
        }

        assertEquals("Response text must be at least 10 characters long", exception.message)
    }
}