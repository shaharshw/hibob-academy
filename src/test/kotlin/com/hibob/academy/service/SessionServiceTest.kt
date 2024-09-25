package com.hibob.academy.service

import com.hibob.academy.employeeFeedback.dao.EmployeeDao
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Role
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SessionServiceTest {

    private val employeeDaoMock = mock<EmployeeDao> {}
    private val sessionService = SessionService(employeeDaoMock)
    private val userId = 1L
    private val companyId = 1L
    private val loggedInUser = LoggedInUser(userId, companyId)

    @Test
    fun `test createJwtToken`() {
        val token = sessionService.createJwtToken(loggedInUser)
        assertNotNull(token)
    }

    @Test
    fun `test getCurrentUserRoleFromToken`() {
        whenever(employeeDaoMock.getRoleFromLoggedInUser(loggedInUser)).thenReturn(Role.ADMIN)

        val role = sessionService.getCurrentUserRoleFromToken(userId, companyId)

        assertEquals(Role.ADMIN, role)
    }
}