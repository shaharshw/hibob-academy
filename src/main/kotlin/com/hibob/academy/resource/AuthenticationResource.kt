package com.hibob.academy.resource

import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Role
import com.hibob.academy.filters.AuthenticationFilter
import com.hibob.academy.service.SessionService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody

@Controller
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/auth")
class AuthenticationResource(
    private val sessionService: SessionService
) {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    fun login(@RequestBody user: LoggedInUser): Response {
        val token = sessionService.createJwtToken(user)
        val cookie = NewCookie.Builder(AuthenticationFilter.AUTH_COOKIE_NAME)
            .value(token)
            .path("/api/")
            .build()

        return Response.ok().cookie(cookie).build()
    }
}

