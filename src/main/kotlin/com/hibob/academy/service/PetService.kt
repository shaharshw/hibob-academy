package com.hibob.academy.service

import com.hibob.academy.dao.PetDao
import com.hibob.academy.entity.*
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Service

@Service
class PetService(
    private val petDao: PetDao,
) {

    fun getAllPets(companyId : Long): List<Pet> {
        return petDao.getAllPetsByCompanyId(companyId)
    }

    fun assignOwnerToPet(petId: Long, ownerId: Long) : Boolean {

        return petDao.assignOwnerToPet(petId, ownerId)
    }

    fun createPet(createPetRequest: CreatePetRequest): Long {
        return petDao.createPet(createPetRequest)
    }

    fun getOwnerByPetId(petId: Long): Owner? {
        val ownerById = petDao.getOwnerByPetId(petId)
            ?: throw BadRequestException("Some error occurred while fetching owner information for pet with id $petId")

        if (!ownerById.petExists) {
            throw BadRequestException("Pet with id $petId does not exist")
        }

        return ownerById.owner
    }

    fun adoptPets(adoptPetsRequest: AdoptPetsRequest) : Map<Long, Boolean> =
        adoptPetsRequest.petIds
            .associateWith { petId -> petDao.assignOwnerToPet(petId, adoptPetsRequest.ownerId) }

    fun getAllPetsByOwnerId(ownerId: Long) : List<Pet> =
        petDao.getAllPetsByOwnerId(ownerId)

    fun getCountPetsByType() : Map<PetType, Int> =
        petDao.getCountPetsByType()

    fun createPets(pets: List<CreatePetRequest>) : List<Long> =
        petDao.createPets(pets)

}