package com.hibob.academy.employeeFeedback.service.feedbackResponse

import com.hibob.academy.employeeFeedback.dao.FeedbackResponseDao
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Response
import org.springframework.stereotype.Service

@Service
class FeedbackResponseFetcher(
    private val feedbackResponseDao: FeedbackResponseDao
) {

    fun getResponseById(loggedInUser: LoggedInUser, responseId: Long) : Response {
        return feedbackResponseDao.getResponseById(loggedInUser.companyId, responseId)
    }
}