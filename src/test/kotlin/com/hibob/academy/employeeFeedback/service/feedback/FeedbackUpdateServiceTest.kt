package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.FeedbackStatus
import com.hibob.academy.employeeFeedback.model.UpdateFeedbackStatusRequest
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FeedbackUpdateServiceTest {
    private val feedbackDaoMock = mock<FeedbackDao>{}
    private val feedbackUpdateService = FeedbackUpdateService(feedbackDaoMock)

    @Test
    fun `test updateFeedbackStatus`() {
        val feedbackId = 1L
        val updateFeedbackStatusRequest = UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED)
        whenever(feedbackDaoMock.updateFeedbackStatus(any(), any(), any())).thenReturn(true)

        assertDoesNotThrow {
            feedbackUpdateService.updateFeedbackStatus(feedbackId, updateFeedbackStatusRequest)
        }
    }

    @Test
    fun `updateFeedbackStatus should throw exception when update fails`() {
        val feedbackId = 1L
        val updateFeedbackStatusRequest = UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED)
        whenever(feedbackDaoMock.updateFeedbackStatus(any(), any(), any())).thenReturn(false)

        val exception = assertThrows(BadRequestException::class.java) {
            feedbackUpdateService.updateFeedbackStatus(feedbackId, updateFeedbackStatusRequest)
        }

        assertEquals("Failed to update feedback status", exception.message)
    }
}