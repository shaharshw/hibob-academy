package com.hibob.academy.employeeFeedback.exception

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.springframework.web.server.ResponseStatusException

@Provider
class IllegalArgumentExceptionMapper : ExceptionMapper<IllegalArgumentException> {
    override fun toResponse(ex: IllegalArgumentException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(ex.message).build()
    }
}

@Provider
class ResponseStatusExceptionMapper : ExceptionMapper<ResponseStatusException> {
    override fun toResponse(ex: ResponseStatusException): Response {
        return Response.status(Response.Status.FORBIDDEN).entity(ex.reason).build()
    }
}
