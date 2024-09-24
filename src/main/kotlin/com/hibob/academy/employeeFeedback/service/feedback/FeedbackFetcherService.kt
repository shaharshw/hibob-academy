package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.Feedback
import com.hibob.academy.employeeFeedback.model.FilterFeedbackRequest
import com.hibob.academy.employeeFeedback.model.StatusResponse
import com.hibob.academy.employeeFeedback.utils.getLoggedInUser
import org.springframework.stereotype.Service

@Service
class FeedbackFetcherService(
    private val feedbackDao: FeedbackDao
) {

    fun getFeedbackById(feedbackId: Long) : Feedback {
        val loggedInUser = getLoggedInUser()
        return feedbackDao.getFeedbackById(loggedInUser.companyId, feedbackId)
    }

    fun getFeedbacks(filters: FilterFeedbackRequest) : List<Feedback> {
        val loggedInUser = getLoggedInUser()
        return feedbackDao.getFeedbacksByFilters(loggedInUser.companyId, filters)
    }

    fun getFeedbackStatusById(feedbackId: Long) : StatusResponse {
        val loggedInUser = getLoggedInUser()
        return feedbackDao.getFeedbackStatusById(loggedInUser.companyId, feedbackId)
    }
}