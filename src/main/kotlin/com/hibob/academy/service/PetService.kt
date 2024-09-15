package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.dao.PetDao
import com.hibob.academy.entity.Owner
import com.hibob.academy.entity.Pet
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Service

@Service
class PetService(
    private val petDao: PetDao,
) {

    fun assignOwnerToPet(petId: Long, ownerId: Long) : Boolean {

        return petDao.assignOwnerToPet(petId, ownerId)
    }


    fun getOwnerByPetId(petId: Long): String {
        val ownerById = petDao.getOwnerByPetId(petId)
            ?: throw BadRequestException("Some error occurred while fetching owner information for pet with id $petId")

        if (!ownerById.petExists) {
            throw BadRequestException("Pet with id $petId does not exist")
        }

        return ownerById.owner?.let { "Owner: ${it.name}" } ?: "This pet does not have an owner"
    }
}