package com.hibob.academy.dao

import com.hibob.academy.entity.Owner
import com.hibob.academy.entity.OwnerById
import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import com.hibob.academy.utils.JooqTable
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component
import java.sql.Date
import java.util.*


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

@Component
class PetDao @Inject constructor(
    private val sql: DSLContext

) {

    private val petTable = PetTable.instance

    private val petMapper = RecordMapper<Record, Pet>
    { record ->
        Pet(
            id = record[petTable.id],
            name = record[petTable.name],
            type = PetType.fromString(record[petTable.type]),
            dataOfArrival = record[PetTable.instance.dateOfArrival].toLocalDate(),
            companyId = record[petTable.companyId],
            ownerId = record[petTable.ownerId]
        )
    }

    private val ownerByIdMapper = RecordMapper<Record, OwnerById> { record ->
        OwnerById(
            petExists = record["pet_id"] != null,
            owner = record["id"]?.let {
                ownerMapper.map(record)
            }
        )
    }

    private val ownerMapper = OwnerDao.ownerMapper

    fun createPet(pet: Pet) : Int {
        return sql.insertInto(petTable)
            .set(petTable.id, pet.id)
            .set(petTable.name, pet.name)
            .set(petTable.type, pet.type.name)
            .set(petTable.dateOfArrival, Date.valueOf(pet.dataOfArrival))
            .set(petTable.companyId, pet.companyId)
            .set(petTable.ownerId, pet.ownerId)
            .execute()
    }

    fun getAllPetsByCompanyId(companyId : Long): List<Pet> {

        return sql.select(petTable.id, petTable.name, petTable.type, petTable.dateOfArrival, petTable.companyId, petTable.ownerId)
            .from(petTable)
            .where(petTable.companyId.eq(companyId))
            .fetch (petMapper)
    }

    fun getPetsByType(petType: PetType, companyId: Long): List<Pet> {

        return sql.select(petTable.id, petTable.name, petTable.type, petTable.dateOfArrival, petTable.companyId, petTable.ownerId)
            .from(petTable)
            .where(petTable.type.eq(petType.name).and(petTable.companyId.eq(companyId)))
            .fetch (petMapper)
    }

    fun getPetById(petId: Long): Pet? {
        return sql.selectFrom(petTable)
            .where(petTable.id.eq(petId))
            .fetchOne(petMapper)
    }

    fun getOwnerByPetId(petId: Long): OwnerById? {
        val ownerTable = OwnerTable.instance

        return sql.select(
            petTable.id.`as`("pet_id"),
            ownerTable.id,
            ownerTable.name,
            ownerTable.companyId,
            ownerTable.employeeId
        )
            .from(petTable)
            .leftJoin(ownerTable).on(petTable.ownerId.eq(ownerTable.id))
            .where(petTable.id.eq(petId))
            .fetchOne(ownerByIdMapper)
    }

    fun assignOwnerToPet(petId: Long, ownerId: Long): Boolean {

        return sql.update(petTable)
            .set(petTable.ownerId, ownerId)
            .where(petTable.id.eq(petId))
            .execute() > 0
    }
}