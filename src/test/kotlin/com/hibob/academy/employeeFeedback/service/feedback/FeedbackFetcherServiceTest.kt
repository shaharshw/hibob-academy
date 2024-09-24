package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.Feedback
import com.hibob.academy.employeeFeedback.model.FeedbackStatus
import com.hibob.academy.employeeFeedback.model.FilterFeedbackRequest
import com.hibob.academy.employeeFeedback.model.StatusResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FeedbackFetcherServiceTest {

    private val feedbackDaoMock = mock<FeedbackDao> {}
    private val feedbackFetcherService = FeedbackFetcherService(feedbackDaoMock)

    @Test
    fun `test getFeedbackById`() {
        val feedbackId = 1L
        val expectedFeedback = Feedback(feedbackId, 1L, "This is a feedback", false, FeedbackStatus.UNREVIEWED)
        whenever(feedbackDaoMock.getFeedbackById(any(), any())).thenReturn(expectedFeedback)

        val result = feedbackFetcherService.getFeedbackById(feedbackId)

        assertEquals(expectedFeedback, result)
    }

    @Test
    fun `test getFeedbacks`() {
        val expectedFeedbacks = listOf(
            Feedback(1L, 1L, "This is a feedback", false, FeedbackStatus.UNREVIEWED),
            Feedback(2L, 1L, "This is another feedback", true, FeedbackStatus.REVIEWED)
        )
        val filters = FilterFeedbackRequest(null, null, null, null)
        whenever(feedbackDaoMock.getFeedbacksByFilters(any(), any())).thenReturn(expectedFeedbacks)

        val result = feedbackFetcherService.getFeedbacks(filters)

        assertEquals(expectedFeedbacks, result)
    }

    @Test
    fun `test getFeedbackStatusById`() {
        val feedbackId = 1L
        val expectedStatus = StatusResponse(FeedbackStatus.UNREVIEWED)
        whenever(feedbackDaoMock.getFeedbackStatusById(any(), any())).thenReturn(expectedStatus)

        val result = feedbackFetcherService.getFeedbackStatusById(feedbackId)

        assertEquals(expectedStatus, result)
    }
}