package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.CreateFeedbackRequest
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import org.springframework.stereotype.Service

@Service
class FeedbackCreator(
    private val feedbackDao: FeedbackDao
) {

    fun createFeedback(loggedInUser: LoggedInUser, createFeedbackRequest: CreateFeedbackRequest) : Long {
        validateCreateFeedbackRequest(createFeedbackRequest)

        return feedbackDao.create(loggedInUser, createFeedbackRequest)
    }

    private fun validateCreateFeedbackRequest(request: CreateFeedbackRequest) {
        require(request.feedbackText.isNotBlank()) { "Feedback text must not be blank" }
        require(request.feedbackText.length > 10) { "Feedback text must be longer than 10 characters" }
    }
}