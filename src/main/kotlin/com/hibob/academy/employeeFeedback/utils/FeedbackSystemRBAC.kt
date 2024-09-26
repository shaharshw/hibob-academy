package com.hibob.academy.employeeFeedback.utils

import com.hibob.academy.employeeFeedback.model.Permission
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
annotation class RequirePermission(val permission: Permission)

@Aspect
@Component
class RoleCheckAspect(
    private val sessionService: SessionService,
    private val request: HttpServletRequest
) {

    @Before("@annotation(requirePermission)")
    fun checkPermission(requirePermission: RequirePermission) {
        val userId = request.getAttribute(AuthenticationFilter.USER_ID) as Long
        val companyId = request.getAttribute(AuthenticationFilter.COMPANY_ID) as Long

        val currentUserRole = sessionService.getCurrentUserRoleFromToken(userId, companyId)

        val allowedRoles = Role.PERMISSIONS[requirePermission.permission]
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "No roles configured for permission: ${requirePermission.permission}")

        if (!allowedRoles.contains(currentUserRole)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for role: $currentUserRole")
        }
    }
}