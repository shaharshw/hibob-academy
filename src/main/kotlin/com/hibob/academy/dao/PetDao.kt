package com.hibob.academy.dao

import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import com.hibob.academy.entity.PetWithoutType
import com.hibob.academy.utils.JooqTable
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.sql.Date

class PetTable(tableName: String = "pets") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val name = createVarcharField("name")
    val type = createVarcharField("type")
    val dateOfArrival = createDateField("data_of_arrival")
    val companyId = createBigIntField("company_id")
    val ownerId = createBigIntField("owner_id")

    companion object {
        val instance = PetTable()
    }
}

@Repository
class PetDao @Inject constructor(
    private val sql: DSLContext
) {

    private val petTable = PetTable.instance

    private val petMapper = RecordMapper<Record, Pet>
    { record ->
        Pet(
            id = record[petTable.id].toString().toLong(),
            name = record[petTable.name],
            type = PetType.fromString(record[petTable.type]),
            dataOfArrival = Date(record[petTable.dateOfArrival].time),
            companyId = record[petTable.companyId].toString().toLong(),
            ownerId = record[petTable.ownerId].toString().toLong()
        )
    }

    private val petMapperWithoutType = RecordMapper<Record, PetWithoutType>
    { record ->
        PetWithoutType(
            id = record[petTable.id].toString().toLong(),
            name = record[petTable.name],
            dataOfArrival = Date(record[petTable.dateOfArrival].time),
            companyId = record[petTable.companyId].toString().toLong()
        )
    }

    fun createPet(pet: Pet) : Int {
        return sql.insertInto(petTable)
            .set(petTable.id, pet.id)
            .set(petTable.name, pet.name)
            .set(petTable.type, pet.type.name)
            .set(petTable.dateOfArrival, pet.dataOfArrival)
            .set(petTable.companyId, pet.companyId)
            .set(petTable.ownerId, pet.ownerId)
            .execute()
    }

    fun getAllPets(): List<Pet> {

        return sql.select(
            petTable.id,
            petTable.name,
            petTable.type,
            petTable.dateOfArrival,
            petTable.companyId,
            petTable.ownerId
        )
            .from(petTable)
            .fetch (petMapper)
    }

    fun getPetsByType(petType: PetType): List<PetWithoutType> {

        return sql.select(petTable.id, petTable.name, petTable.dateOfArrival, petTable.companyId)
            .from(petTable)
            .where(petTable.type.eq(petType.name))
            .fetch (petMapperWithoutType)
    }

    fun getAllPetsByOwnerId(ownerId: Long) : List<Pet> {

        return sql.select(
            petTable.id,
            petTable.name,
            petTable.type,
            petTable.dateOfArrival,
            petTable.companyId,
            petTable.ownerId
        )
            .from(petTable)
            .where(petTable.ownerId.eq(ownerId))
            .fetch (petMapper)
    }

    fun getCountPetsByType(): Map<String, Int> {

        return sql.select(petTable.type, DSL.count())
            .from(petTable)
            .groupBy(petTable.type)
            .fetch()
            .associate { it[petTable.type] to (it[DSL.count()] as Int) }
    }
}