package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.Feedback
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.StatusResponse
import com.hibob.academy.employeeFeedback.dao.filter.FilterFactory
import org.springframework.stereotype.Service

@Service
class FeedbackFetcher(
    private val feedbackDao: FeedbackDao
) {

    fun getFeedbackById(companyId: Long, feedbackId: Long) : Feedback {
        return feedbackDao.getFeedbackById(companyId, feedbackId)
    }

    fun getFeedbacksByCompany(companyId: Long, filters: Map<String, Any>) : List<Feedback> {
        val genericFilterRequest = FilterFactory.convertToGenericFilterRequest(filters)
        return feedbackDao.getFeedbacksByFilters(companyId, genericFilterRequest)
    }

    fun getFeedbackStatusById(loggedInUser: LoggedInUser, feedbackId: Long) : StatusResponse {
        return feedbackDao.getFeedbackStatusById(loggedInUser, feedbackId)
    }
}