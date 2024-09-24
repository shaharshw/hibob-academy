package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.CreateFeedbackRequest
import com.hibob.academy.employeeFeedback.utils.getLoggedInUser
import org.springframework.stereotype.Service

@Service
class FeedbackCreatorService(
    private val feedbackDao: FeedbackDao
) {

    fun createFeedback(createFeedbackRequest: CreateFeedbackRequest) : Long {
        val loggedInUser = getLoggedInUser()
        validateCreateFeedbackRequest(createFeedbackRequest)

        return feedbackDao.create(createFeedbackRequest, loggedInUser)
    }

    private fun validateCreateFeedbackRequest(request: CreateFeedbackRequest) {
        require(request.feedbackText.isNotBlank()) { "Feedback text must not be blank" }
        require(request.feedbackText.length > 10) { "Feedback text must be longer than 10 characters" }
    }
}