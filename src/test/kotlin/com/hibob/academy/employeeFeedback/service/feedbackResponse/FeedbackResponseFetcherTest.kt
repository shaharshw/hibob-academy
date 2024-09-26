package com.hibob.academy.employeeFeedback.service.feedbackResponse

import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Response
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class FeedbackResponseFetcherTest {

    private val feedbackResponseDaoMock = mock<FeedbackResponseDao>()
    private val feedbackResponseFetcherService = FeedbackResponseFetcher(feedbackResponseDaoMock)
    private val companyId = 1L

    @Test
    fun `test getResponseById`() {
        val responseId = 1L
        val response = mock(Response::class.java)

        whenever(feedbackResponseDaoMock.getResponseById(any(), any())).thenReturn(response)

        val result = feedbackResponseFetcherService.getResponseById(companyId, responseId)

        verify(feedbackResponseDaoMock).getResponseById(any(),any())
        assertEquals(response, result)
    }

    @Test
    fun `test getResponseById with invalid responseId`() {
        val responseId = 1L

        whenever(feedbackResponseDaoMock.getResponseById(any(), any())).thenThrow(IllegalArgumentException("Respond with $responseId not found"))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackResponseFetcherService.getResponseById(companyId, responseId)
        }

        assertEquals("Respond with $responseId not found", exception.message)
    }

    @Test
    fun `test getResponsesByFeedbackId`() {
        val feedbackId = 1L
        val response = mock(Response::class.java)
        val responses = listOf(response)

        whenever(feedbackResponseDaoMock.getResponsesByFeedbackId(any(), any())).thenReturn(responses)

        val result = feedbackResponseFetcherService.getResponsesByFeedbackId(companyId, feedbackId)

        verify(feedbackResponseDaoMock).getResponsesByFeedbackId(any(),any())
        assertTrue(result.containsAll(responses))
    }
}