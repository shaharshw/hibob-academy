package com.hibob.academy.dao

import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import com.hibob.academy.entity.PetWithoutType
import com.hibob.academy.utils.JooqTable
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import java.sql.Timestamp

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

@Component
class PetDao @Inject constructor(
    private val sql: DSLContext
) {

    private val p = PetTable.instance

    private val petMapper = RecordMapper<Record, Pet>
    { record ->
        Pet(
            id = record[PetTable.instance.id].toString().toLong(),
            name = record[PetTable.instance.name],
            type = PetType.fromString(record[PetTable.instance.type]),
            dataOfArrival = Timestamp(record[PetTable.instance.dateOfArrival].time),
            companyId = record[PetTable.instance.companyId].toString().toLong()
        )
    }

    private val petMapperWithoutType = RecordMapper<Record, PetWithoutType>
    { record ->
        PetWithoutType(
            id = record[PetTable.instance.id].toString().toLong(),
            name = record[PetTable.instance.name],
            dataOfArrival = Timestamp(record[PetTable.instance.dateOfArrival].time),
            companyId = record[PetTable.instance.companyId].toString().toLong()
        )
    }

    fun getPetsByType(petType: PetType): List<PetWithoutType> {

        return sql.select(p.id, p.name, p.dateOfArrival, p.companyId)
            .from(p)
            .where(p.type.eq(petType.name))
            .fetch (petMapperWithoutType)
    }
}


