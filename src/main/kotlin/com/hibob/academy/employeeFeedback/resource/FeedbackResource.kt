package com.hibob.academy.employeeFeedback.resource

import com.hibob.academy.employeeFeedback.model.*
import com.hibob.academy.employeeFeedback.service.feedback.FeedbackCreator
import com.hibob.academy.employeeFeedback.service.feedback.FeedbackFetcher
import com.hibob.academy.employeeFeedback.service.feedback.FeedbackUpdator
import com.hibob.academy.employeeFeedback.service.feedbackResponse.FeedbackResponseCreator
import com.hibob.academy.employeeFeedback.service.feedbackResponse.FeedbackResponseFetcher
import com.hibob.academy.employeeFeedback.service.feedbackResponse.FeedbackResponseUpdator
import com.hibob.academy.employeeFeedback.utils.RequirePermission
import com.hibob.academy.filters.AuthenticationFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
@Path("/api/feedback")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FeedbackResource(
    private val feedbackCreator: FeedbackCreator,
    private val feedbackFetcher: FeedbackFetcher,
    private val feedbackUpdator: FeedbackUpdator,
    private val feedbackResponseCreator: FeedbackResponseCreator,
    private val feedbackResponseFetcher: FeedbackResponseFetcher,
    private val feedbackResponseUpdator: FeedbackResponseUpdator,
    private val request: HttpServletRequest

) {

    private fun getLoggedInUser(): LoggedInUser {
        val userId = request.getAttribute(AuthenticationFilter.USER_ID) as Long
        val companyId = request.getAttribute(AuthenticationFilter.COMPANY_ID) as Long
        return LoggedInUser(userId, companyId)
    }

    @POST
    @Path("/submit")
    @RequirePermission(Permission.EMPLOYEES_PERMISSION)
    fun submitFeedback(@RequestBody createFeedbackRequest: CreateFeedbackRequest) : Response {
        val loggedInUser = getLoggedInUser()
        val feedbackId = feedbackCreator.createFeedback(loggedInUser, createFeedbackRequest)
        return Response.status(Response.Status.CREATED).entity(feedbackId).build()
    }

    @POST
    @Path("/view")
    @RequirePermission(Permission.ADMIN_PERMISSION)
    fun viewFeedback(@RequestBody filters: FilterFeedbackRequest) : Response {
        val companyId = getLoggedInUser().companyId
        val feedbacks = feedbackFetcher.getFeedbacksByCompany(companyId, filters)
        return Response.status(Response.Status.OK).entity(feedbacks).build()
    }

    @POST
    @Path("/{feedback_id}/response")
    @RequirePermission(Permission.HR_PERMISSION)
    fun submitFeedbackResponse(@PathParam("feedback_id") feedbackId: Long, @RequestBody createFeedbackResponseRequest: ResponseCreationRequest) : Response {
        val loggedInUser = getLoggedInUser()
        val responseId = feedbackResponseCreator.createFeedbackResponse(loggedInUser, feedbackId, createFeedbackResponseRequest)
        return Response.status(Response.Status.CREATED).entity(responseId).build()
    }

    @GET
    @Path("/{feedback_id}/status")
    @RequirePermission(Permission.EMPLOYEES_PERMISSION)
    fun getFeedbackStatus(@PathParam("feedback_id") feedbackId: Long) : Response {
        val companyId = getLoggedInUser().companyId
        val status = feedbackFetcher.getFeedbackStatusById(companyId, feedbackId)
        return Response.status(Response.Status.OK).entity(status).build()
    }

    @PUT
    @Path("/{feedback_id}/status")
    @RequirePermission(Permission.HR_PERMISSION)
    fun updateFeedbackStatus(@PathParam("feedback_id") feedbackId: Long, @RequestBody updateFeedbackStatusRequest: UpdateFeedbackStatusRequest) : Response {
        val loggedInUser = getLoggedInUser()
        feedbackUpdator.updateFeedbackStatus(loggedInUser, feedbackId, updateFeedbackStatusRequest)
        return Response.status(Response.Status.OK).build()
    }

    @PUT
    @Path("/{feedback_id}/response")
    @RequirePermission(Permission.HR_PERMISSION)
    fun updateFeedbackResponse(@PathParam("feedback_id") feedbackId: Long, @RequestBody updateFeedbackResponseRequest: UpdateFeedbackResponseRequest) : Response {
        val loggedInUser = getLoggedInUser()
        feedbackResponseUpdator.updateFeedbackResponse(loggedInUser, feedbackId, updateFeedbackResponseRequest)
        return Response.status(Response.Status.OK).build()
    }

    @GET
    @Path("/{feedback_id}/response")
    @RequirePermission(Permission.HR_PERMISSION)
    fun getResponsesByFeedbackId(@PathParam("feedback_id") feedbackId: Long) : Response {
        val companyId = getLoggedInUser().companyId
        val responses = feedbackResponseFetcher.getResponsesByFeedbackId(companyId, feedbackId)
        return Response.status(Response.Status.OK).entity(responses).build()
    }
}

