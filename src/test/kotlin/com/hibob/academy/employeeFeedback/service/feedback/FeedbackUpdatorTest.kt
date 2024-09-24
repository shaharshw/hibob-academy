package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.FeedbackStatus
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.UpdateFeedbackStatusRequest
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FeedbackUpdatorTest {

    private val feedbackDaoMock = mock<FeedbackDao>{}
    private val feedbackUpdateService = FeedbackUpdator(feedbackDaoMock)
    private val loggedInUser = LoggedInUser(1L, 1L)

    @Test
    fun `test updateFeedbackStatus`() {
        val feedbackId = 1L
        val updateFeedbackStatusRequest = UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED)
        whenever(feedbackDaoMock.updateFeedbackStatus(any(), any(), any())).thenReturn(true)

        assertDoesNotThrow {
            feedbackUpdateService.updateFeedbackStatus(loggedInUser, feedbackId, updateFeedbackStatusRequest)
        }
    }

    @Test
    fun `updateFeedbackStatus should throw exception when update fails`() {
        val feedbackId = 1L
        val updateFeedbackStatusRequest = UpdateFeedbackStatusRequest(FeedbackStatus.REVIEWED)
        whenever(feedbackDaoMock.updateFeedbackStatus(any(), any(), any())).thenReturn(false)

        val exception = assertThrows(BadRequestException::class.java) {
            feedbackUpdateService.updateFeedbackStatus(loggedInUser, feedbackId, updateFeedbackStatusRequest)
        }

        assertEquals("Failed to update feedback status", exception.message)
    }
}