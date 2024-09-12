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
    private val ownerDao: OwnerDao
) {

    fun assignOwnerToPet(petId: Long, ownerId: Long) {

        val pet = petDao.getPetById(petId) ?: throw BadRequestException("Pet with ID $petId not found")
        petDao.assignOwnerToPet(pet.id, ownerId)
    }

    fun getOwnerByPetId(petId: Long): Owner? {

        val pet = petDao.getPetById(petId) ?: throw BadRequestException("Pet with ID $petId not found")
        return ownerDao.getOwnerById(pet.ownerId)
    }
}