package com.hibob.academy.employeeFeedback.service.feedbackResponse

import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.model.Response
import com.hibob.academy.employeeFeedback.utils.getLoggedInUser
import org.springframework.stereotype.Service

@Service
class FeedbackResponseFetcherService(
    private val feedbackResponseDao: FeedbackResponseDao
) {

    fun getResponseById(responseId: Long) : Response {
        val loggedInUser = getLoggedInUser()
        return feedbackResponseDao.getResponseById(loggedInUser.companyId, responseId)
    }
}