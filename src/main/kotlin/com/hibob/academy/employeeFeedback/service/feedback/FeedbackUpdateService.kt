package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.FeedbackStatus
import com.hibob.academy.employeeFeedback.model.UpdateFeedbackStatusRequest
import com.hibob.academy.employeeFeedback.utils.getLoggedInUser
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Service

@Service
class FeedbackUpdateService(
    private val feedbackDao: FeedbackDao
) {

    fun updateFeedbackStatus(feedbackId: Long, updateFeedbackStatusRequest: UpdateFeedbackStatusRequest) {
        val loggedInUser = getLoggedInUser()
        val isSuccess = feedbackDao.updateFeedbackStatus(loggedInUser.companyId, feedbackId,updateFeedbackStatusRequest)

        if (!isSuccess) {
            throw BadRequestException("Failed to update feedback status")
        }
    }

}