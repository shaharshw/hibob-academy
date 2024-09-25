package com.hibob.academy.employeeFeedback.exception

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class IllegalArgumentExceptionMapper : ExceptionMapper<IllegalArgumentException> {
    override fun toResponse(ex: IllegalArgumentException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(ex.message).build()
    }
}
