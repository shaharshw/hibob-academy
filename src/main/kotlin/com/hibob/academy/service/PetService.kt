package com.hibob.academy.service

import com.hibob.academy.dao.PetDao
import com.hibob.academy.entity.Pet
import org.springframework.stereotype.Service

@Service
class PetService(
    private val petDao: PetDao
) {

    fun getAllPetsByOwnerId(ownerId: Long) : List<Pet> = petDao.getAllPetsByOwnerId(ownerId)

    fun getCountPetsByType() : Map<String, Int> = petDao.getCountPetsByType()
}