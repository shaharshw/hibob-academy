package com.hibob.academy.employeeFeedback.dao

import com.hibob.academy.employeeFeedback.dao.table.EmployeeTable
import com.hibob.academy.employeeFeedback.model.LoggedInUser
import com.hibob.academy.employeeFeedback.model.Role
import jakarta.inject.Inject
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.jooq.RecordMapper
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class EmployeeDao @Inject constructor(
    private val sql: DSLContext
) {

    private val employeeTable = EmployeeTable.instance

    private val roleMapper = RecordMapper<Record, Role>
    { record ->
        Role.valueOf(record[employeeTable.role].uppercase())
    }

    fun getRoleFromLoggedInUser(loggedInUser: LoggedInUser): Role {
        return sql.select(employeeTable.role)
            .from(employeeTable)
            .where(employeeTable.id.eq(loggedInUser.id)
                .and(employeeTable.companyId.eq(loggedInUser.companyId)))
            .fetchOne(roleMapper) ?: throw BadRequestException("User with ${loggedInUser.id} and companyId: ${loggedInUser.companyId} not found")
    }
}