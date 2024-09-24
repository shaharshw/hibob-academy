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
    private val loggedInUser = LoggedInUser(1L, 1L)


    @Test
    fun `test getResponseById`() {
        val responseId = 1L
        val response = mock(Response::class.java)

        whenever(feedbackResponseDaoMock.getResponseById(any(), any())).thenReturn(response)

        val result = feedbackResponseFetcherService.getResponseById(loggedInUser, responseId)

        verify(feedbackResponseDaoMock).getResponseById(any(),any())
        assertEquals(response, result)
    }

    @Test
    fun `test getResponseById with invalid responseId`() {
        val responseId = 1L

        whenever(feedbackResponseDaoMock.getResponseById(any(), any())).thenThrow(IllegalArgumentException("Respond with $responseId not found"))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            feedbackResponseFetcherService.getResponseById(loggedInUser, responseId)
        }

        assertEquals("Respond with $responseId not found", exception.message)
    }
}