package com.hibob.academy.dao

import com.hibob.academy.entity.Owner
import com.hibob.academy.utils.JooqTable
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

class OwnerTable(tableName: String = "owner") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val name = createVarcharField("name")
    val companyId = createBigIntField("company_id")
    val employeeId = createVarcharField("employee_id")

    companion object {
        val instance = OwnerTable()
    }
}

@Repository
class OwnerDao(
    private val sql: DSLContext
) {

    private val ownerTable = OwnerTable.instance

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

        return sql.select(ownerTable.id, ownerTable.name, ownerTable.companyId, ownerTable.employeeId)
            .from(ownerTable)
            .fetch (ownerMapper)
    }

    fun createOwner(owner: Owner) : Int {
        return sql.insertInto(ownerTable)
            .set(ownerTable.id, owner.id)
            .set(ownerTable.name, owner.name)
            .set(ownerTable.companyId, owner.companyId)
            .set(ownerTable.employeeId, owner.employeeId)
            .onConflict(ownerTable.companyId, ownerTable.employeeId)
            .doNothing()
            .execute()
    }
}