package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.*
import com.hibob.academy.employeeFeedback.dao.filter.FilterFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FeedbackFetcherTest {

    private val feedbackDaoMock = mock<FeedbackDao> {}
    private val feedbackFetcherService = FeedbackFetcher(feedbackDaoMock)
    private val companyId = 1L
    private val loggedInUser = LoggedInUser(1L, companyId)

    @Test
    fun `test getFeedbackById`() {
        val feedbackId = 1L
        val expectedFeedback = Feedback(feedbackId, 1L, "This is a feedback", false, FeedbackStatus.UNREVIEWED)
        whenever(feedbackDaoMock.getFeedbackById(any(), any())).thenReturn(expectedFeedback)

        val result = feedbackFetcherService.getFeedbackById(companyId, feedbackId)

        assertEquals(expectedFeedback, result)
    }

    @Test
    fun `test getFeedbacks`() {
        val expectedFeedbacks = listOf(
            Feedback(1L, 1L, "This is a feedback", false, FeedbackStatus.UNREVIEWED),
            Feedback(2L, 1L, "This is another feedback", true, FeedbackStatus.REVIEWED)
        )
        val filters = emptyMap<String, Any>()
      //  val filters = FilterFactory.convertToGenericFilterRequest(filterParams)
        whenever(feedbackDaoMock.getFeedbacksByFilters(any(), any())).thenReturn(expectedFeedbacks)

        val result = feedbackFetcherService.getFeedbacksByCompany(companyId, filters)

        assertEquals(expectedFeedbacks, result)
    }

    @Test
    fun `test getFeedbackStatusById`() {
        val feedbackId = 1L
        val expectedStatus = StatusResponse(FeedbackStatus.UNREVIEWED)
        whenever(feedbackDaoMock.getFeedbackStatusById(any(), any())).thenReturn(expectedStatus)

        val result = feedbackFetcherService.getFeedbackStatusById(loggedInUser, feedbackId)

        assertEquals(expectedStatus, result)
    }
}