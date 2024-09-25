package com.hibob.academy.employeeFeedback.exception

import jakarta.ws.rs.core.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): Response {
        return Response.status(Response.Status.BAD_REQUEST).entity(ex.message).build()
    }
}
