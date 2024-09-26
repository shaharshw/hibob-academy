package com.hibob.academy.employeeFeedback.service.feedbackResponse

import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.model.ResponseCreationRequest
import com.hibob.academy.employeeFeedback.model.CreateResponseRequestWithFeedbackId
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import org.springframework.stereotype.Service

@Service
class FeedbackResponseCreator(
    private val feedbackResponseDao: FeedbackResponseDao
) {

    fun createFeedbackResponse(loggedInUser: LoggedInUser, feedbackId: Long, createFeedbackResponseRequest: ResponseCreationRequest) : Long {
        val createFeedbackResponseWithId = CreateResponseRequestWithFeedbackId(feedbackId, createFeedbackResponseRequest.text)
        validateCreateResponseRequest(createFeedbackResponseWithId)

        return feedbackResponseDao.create(loggedInUser, createFeedbackResponseWithId)
    }

    private fun validateCreateResponseRequest(request: CreateResponseRequestWithFeedbackId) {
        require(request.text.isNotBlank()) { "Response text must not be blank" }
        require(request.text.length > 10) { "Response text must be at least 10 characters long" }
    }
}