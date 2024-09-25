package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.table.EmployeeTable
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Role
import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@BobDbTest
class EmployeeDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val employeeDao = EmployeeDao(sql)
    private val table = EmployeeTable.instance
    private val companyId = 999L
    private val employeeId = 999L
    private val loggedInUser = LoggedInUser(employeeId, companyId)


    private fun setup() {
        sql.insertInto(table)
            .columns(table.id, table.companyId, table.firstName, table.lastName, table.role, table.department)
            .values(employeeId, companyId, "John", "Doe", "EMPLOYEE", "IT")
            .execute()
    }

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test get role from logged in user`() {
        setup()
        val role = employeeDao.getRoleFromLoggedInUser(loggedInUser)

        assertEquals(Role.EMPLOYEE, role)
    }

    @Test
    fun `test get role from logged in user with invalid id`() {
        setup()
        val invalidId = 9999L
        val loggedInUser = LoggedInUser(invalidId, companyId)

        val exception = assertThrows<BadRequestException> {
            employeeDao.getRoleFromLoggedInUser(loggedInUser)
        }

        assertEquals("User with $invalidId and companyId: $companyId not found", exception.message)
    }
}