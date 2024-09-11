package com.hibob.academy.dao

import com.hibob.academy.entity.Owner
import com.hibob.academy.utils.JooqTable
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component

class OwnerTable(tableName: String = "owner") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val name = createVarcharField("name")
    val companyId = createBigIntField("company_id")
    val employeeId = createVarcharField("employee_id")

    companion object {
        val instance = OwnerTable()
    }
}

@Component
class OwnerDao(
    private val sql: DSLContext
) {

    private val o = OwnerTable.instance

    private val ownerMapper = RecordMapper<Record, Owner>
    { record ->
        Owner(
            id = record[OwnerTable.instance.id],
            name = record[OwnerTable.instance.name],
            companyId = record[OwnerTable.instance.companyId],
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

    fun createOwner(owner: Owner) : Int {
        return sql.insertInto(o)
            .set(o.id, owner.id)
            .set(o.name, owner.name)
            .set(o.companyId, owner.companyId)
            .set(o.employeeId, owner.employeeId)
            .onConflict(o.companyId, o.employeeId)
            .doNothing()
            .execute()
    }
}


