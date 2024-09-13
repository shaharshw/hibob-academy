package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.entity.CreateOwnerRequest
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

    fun createOwner(createOwnerRequest: CreateOwnerRequest): Long {

        val generatedId = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
        val owner = Owner(
            id = generatedId,
            name = createOwnerRequest.name,
            companyId = createOwnerRequest.companyId,
            employeeId = createOwnerRequest.employeeId,
            firstName = createOwnerRequest.firstName,
            lastName = createOwnerRequest.lastName
        )

        val ownerToCreate = populateOwnerNameFields(owner)
        return ownerDao.createOwner(ownerToCreate)
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