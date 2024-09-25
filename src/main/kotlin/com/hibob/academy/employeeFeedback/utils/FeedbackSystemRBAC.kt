package com.hibob.academy.employeeFeedback.utils

import com.hibob.academy.employeeFeedback.model.Role
import com.hibob.academy.filters.AuthenticationFilter
import com.hibob.academy.service.SessionService
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireRole(vararg val roles: Role)

@Aspect
@Component
class RoleCheckAspect(
    private val sessionService: SessionService,
    private val request: HttpServletRequest
) {

    @Before("@annotation(requireRole)")
    fun checkRole(requireRole: RequireRole) {
        val token = request.getAttribute(AuthenticationFilter.TOKEN_ATTRIBUTE) as? String
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid token")

        val currentUserRole = sessionService.getCurrentUserRoleFromToken(token)

        if (!requireRole.roles.contains(currentUserRole)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for role: $currentUserRole")
        }
    }

}