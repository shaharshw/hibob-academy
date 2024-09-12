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

    fun getOwnerByPetId(petId: Long): Owner? {

        val ownerById = petDao.getOwnerByPetId(petId) ?: return null

        if (!ownerById.petExists) {
            throw BadRequestException("Pet with id $petId does not have an owner")
        }

        return ownerById.owner
    }
}