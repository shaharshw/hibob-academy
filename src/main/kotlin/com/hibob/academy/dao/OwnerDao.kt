package com.hibob.academy.dao

import com.hibob.academy.entity.Owner
import com.hibob.academy.utils.JooqTable
import jakarta.ws.rs.BadRequestException
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

    private val ownerTable = OwnerTable.instance

    companion object {
        val ownerMapper = RecordMapper<Record, Owner>
        { record ->
            val nameParts = record.getValue("name", String::class.java)?.split("\\s+".toRegex())
            val firstName = nameParts?.getOrNull(0).orEmpty()
            val lastName = nameParts?.drop(1)?.joinToString(" ").takeIf { it?.isNotEmpty() == true }

            Owner(
                id = record.getValue("id", Long::class.java),
                name = record.getValue("name", String::class.java),
                companyId = record.getValue("company_id", Long::class.java),
                employeeId = record.getValue("employee_id", String::class.java),
                firstName = firstName,
                lastName = lastName
            )
        }
    }

    fun getAllOwnersByCompanyId(companyId: Long): List<Owner> {

        return sql.select(ownerTable.id, ownerTable.name, ownerTable.companyId, ownerTable.employeeId)
            .from(ownerTable)
            .where(ownerTable.companyId.eq(companyId))
            .fetch(ownerMapper)
    }

    fun createOwner(owner: Owner): Long {
        val record = sql.insertInto(ownerTable)
            .set(ownerTable.id, owner.id)
            .set(ownerTable.name, owner.name)
            .set(ownerTable.companyId, owner.companyId)
            .set(ownerTable.employeeId, owner.employeeId)
            .onConflict(ownerTable.companyId, ownerTable.employeeId)
            .doNothing()
            .returning(ownerTable.id)
            .fetchOne()

        if (record?.getValue(ownerTable.id) == null) {
            throw BadRequestException("Owner creation failed. No record was inserted.")
        }

        return record.getValue(ownerTable.id)
    }


    fun getOwnerById(ownerId: Long): Owner? {

        return sql.select(ownerTable.id, ownerTable.name, ownerTable.companyId, ownerTable.employeeId)
            .from(ownerTable)
            .where(ownerTable.id.eq(ownerId))
            .fetchOne(ownerMapper)
    }
}