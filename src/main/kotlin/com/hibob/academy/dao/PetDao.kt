package com.hibob.academy.dao

import com.hibob.academy.entity.Owner
import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import com.hibob.academy.utils.JooqTable
import jakarta.inject.Inject
import org.jooq.DSLContext
import java.sql.Timestamp

class PetDao @Inject constructor(
    private val sql: DSLContext
) {

    fun getPetsByType(petType: PetType): List<Pet> {
        val p = PetTable.instance

        return sql.select(p.id, p.name, p.dateOfArrival, p.companyId)
            .from(p)
            .where(p.type.eq(petType.name))
            .fetch { record ->
                Pet(
                    id = record[p.id].toString().toLong(),
                    name = record[p.name],
                    type = PetType.fromString(record[p.type]),
                    dataOfArrival = Timestamp(record[p.dateOfArrival].time),
                    companyId = record[p.companyId].toString().toLong()
                )
            }
    }
}


class PetTable(tableName: String = "pets") : JooqTable(tableName) {
    val id = createUUIDField("id")
    val name = createVarcharField("name")
    val type = createVarcharField("type")
    val dateOfArrival = createDateField("data_of_arrival")
    val companyId = createUUIDField("company_id")

    companion object {
        val instance = PetTable()
    }
}