package com.hibob.academy.employeeFeedback.service.feedbackResponse

import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.UpdateFeedbackResponseRequest
import org.springframework.stereotype.Service


@Service
class FeedbackResponseUpdator(
    private val feedbackResponseDao: FeedbackResponseDao
) {

    fun updateFeedbackResponse(loggedInUser: LoggedInUser, feedbackId: Long, updateFeedbackResponseRequest: UpdateFeedbackResponseRequest) : Boolean {
        validateUpdateResponseRequest(updateFeedbackResponseRequest)

        return feedbackResponseDao.updateResponse(loggedInUser, updateFeedbackResponseRequest)
    }

    private fun validateUpdateResponseRequest(request: UpdateFeedbackResponseRequest) {
        if(!request.append) {
            require(request.responseText.isNotBlank()) { "Response text must not be blank" }
            require(request.responseText.length > 10) { "Response text must be at least 10 characters long" }
        }
    }
}