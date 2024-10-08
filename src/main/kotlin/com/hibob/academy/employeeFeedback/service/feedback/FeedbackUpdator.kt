package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.UpdateFeedbackStatusRequest
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Service

@Service
class FeedbackUpdator(
    private val feedbackDao: FeedbackDao
) {

    fun updateFeedbackStatus(loggedInUser: LoggedInUser, feedbackId: Long, updateFeedbackStatusRequest: UpdateFeedbackStatusRequest) {
        val isSuccess = feedbackDao.updateFeedbackStatus(loggedInUser.companyId, feedbackId,updateFeedbackStatusRequest)

        if (!isSuccess) {
            throw BadRequestException("Failed to update feedback status")
        }
    }
}