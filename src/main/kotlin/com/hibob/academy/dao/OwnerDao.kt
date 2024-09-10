package com.hibob.academy.dao

import com.hibob.academy.entity.Owner
import com.hibob.academy.utils.JooqTable
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper

class OwnerTable(tableName: String = "owner") : JooqTable(tableName) {
    val id = createUUIDField("id")
    val name = createVarcharField("name")
    val companyId = createUUIDField("company_id")
    val employeeId = createUUIDField("employee_id")

    companion object {
        val instance = OwnerTable()
    }
}

class OwnerDao(
    private val sql: DSLContext
) {

    private val o = OwnerTable.instance

    private val ownerMapper = RecordMapper<Record, Owner>
    { record ->
        Owner(
            id = record[OwnerTable.instance.id].toString().toLong(),
            name = record[OwnerTable.instance.name],
            companyId = record[OwnerTable.instance.companyId].toString().toLong(),
            employeeId = record[OwnerTable.instance.employeeId].toString(),
            firstName = null,
            lastName = null
        )
    }

    fun getAllOwners(): List<Owner> {

        return sql.select(o.id, o.name, o.companyId, o.employeeId)
            .from(o)
            .fetch (ownerMapper)
    }
}


