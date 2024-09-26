package com.hibob.academy.employeeFeedback.service.feedback

import com.hibob.academy.employeeFeedback.dao.FeedbackDao
import com.hibob.academy.employeeFeedback.model.Feedback
import com.hibob.academy.employeeFeedback.model.FilterFeedbackRequest
import com.hibob.academy.employeeFeedback.model.StatusResponse
import org.springframework.stereotype.Service

@Service
class FeedbackFetcher(
    private val feedbackDao: FeedbackDao
) {

    fun getFeedbackById(companyId: Long, feedbackId: Long) : Feedback {
        return feedbackDao.getFeedbackById(companyId, feedbackId)
    }

    fun getFeedbacksByCompany(companyId: Long, filters: FilterFeedbackRequest) : List<Feedback> {
        return feedbackDao.getFeedbacksByFilters(companyId, filters)
    }

    fun getFeedbackStatusById(companyId: Long, feedbackId: Long) : StatusResponse {
        return feedbackDao.getFeedbackStatusById(companyId, feedbackId)
    }
}