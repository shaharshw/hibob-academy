package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.entity.Owner
import jakarta.ws.rs.BadRequestException
import org.springframework.stereotype.Service
import java.util.*

@Service
class OwnerService(
    private val ownerDao: OwnerDao
) {

    fun getAllOwnersByCompanyId(companyId: Long) =
        ownerDao.getAllOwnersByCompanyId(companyId)

    fun createOwner(owner: Owner): Owner {

        val generatedId = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
        val ownerWithId = owner.copy(id = generatedId)
        val ownerToCreate = populateOwnerNameFields(ownerWithId)

        val isSuccessful = ownerDao.createOwner(ownerToCreate)

        if (!isSuccessful) {
            throw BadRequestException("Owner with the same employeeId and companyId already exists")
        }

        return ownerToCreate
    }

    private fun populateOwnerNameFields(owner: Owner): Owner {

        owner.name = owner.name ?: run {
            if (owner.firstName.isNullOrEmpty() || owner.lastName.isNullOrEmpty()) {
                throw BadRequestException("Name is required")
            } else {
                "${owner.firstName} ${owner.lastName}"
            }
        }

        owner.name?.let { fullName ->
            val nameParts = fullName.trim().split("\\s+".toRegex())
            owner.firstName = nameParts[0]
            owner.lastName = nameParts.drop(1).joinToString(" ").takeIf { it.isNotEmpty() }
        }

        return owner
    }
}