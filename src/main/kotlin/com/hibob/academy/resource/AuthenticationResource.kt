package com.hibob.academy.resource

import com.hibob.academy.service.SessionService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
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
    fun login(@RequestBody user: LoginUser): Response {
        val token = sessionService.createJwtToken(user)
        return Response.ok(token).build()
    }

    @GET
    @Path("/check")
    fun check(): String = "OK"

}

data class LoginUser(
    val email: String,
    val userName: String,
    val isAdmin : Boolean
)