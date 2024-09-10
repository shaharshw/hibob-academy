package com.hibob.academy.dao

import com.hibob.academy.entity.Owner
import com.hibob.academy.utils.JooqTable
import org.jooq.DSLContext

class OwnerDao(
    private val sql: DSLContext
) {
    fun getAllOwners(): List<Owner> {
        val o = OwnerTable.instance

        return sql.select(o.id, o.name, o.companyId, o.employeeId)
            .from(o)
            .fetch { record ->
                Owner(
                    id = record[o.id].toString().toLong(),
                    name = record[o.name],
                    companyId = record[o.companyId].toString().toLong(),
                    employeeId = record[o.employeeId].toString().toLong(),
                    firstName = null,
                    lastName = null
                )
            }
    }
}


class OwnerTable(tableName: String = "owner") : JooqTable(tableName) {
    val id = createUUIDField("id")
    val name = createVarcharField("name")
    val companyId = createUUIDField("company_id")
    val employeeId = createUUIDField("employee_id")

    companion object {
        val instance = OwnerTable()
    }
}
