package com.hibob.academy.entity

import java.time.LocalDate

data class Pet(
    val id: Long,
    val name: String,
    val type : PetType,
    val dataOfArrival : LocalDate,
    val companyId : Long,
    val ownerId : Long?
)

data class CreatePetRequest(
    val name: String,
    val type : PetType,
    val dataOfArrival : LocalDate,
    val companyId : Long,
    val ownerId : Long?
)

data class Owner(
    val id: Long,
    var name: String?,
    val companyId: Long,
    val employeeId : String,
    var firstName : String?,
    var lastName : String?
)

data class CreateOwnerRequest(
    var name: String?,
    val companyId: Long,
    val employeeId : String,
    var firstName : String?,
    var lastName : String?
)

data class OwnerById(
    val petExists: Boolean,
    val owner: Owner?
)

enum class PetType {
    DOG,
    CAT,
    BIRD,
    FISH,
    RABBIT;

    companion object {
        fun fromString(type: String): PetType {
            return try {
                valueOf(type.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid pet type: $type")
            }
        }
    }
}
