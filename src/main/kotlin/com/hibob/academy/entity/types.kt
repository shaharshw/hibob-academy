package com.hibob.academy.entity

import java.sql.Timestamp
import java.util.UUID

data class Pet(
    val id: Long,
    val name: String,
    val type : PetType,
    val dataOfArrival : Timestamp,
    val companyId : Long
)

data class Owner(
    val id: UUID,
    var name: String?,
    val companyId: UUID,
    val employeeId : String,
    var firstName : String?,
    var lastName : String?
)

enum class PetType {
    DOG,
    CAT,
    BIRD,
    FISH,
    RABBIT;

    companion object {
        fun fromString(type: String): PetType {
               return valueOf(type.uppercase())
        }
    }
}

data class PetWithoutType(
    val id: Long,
    val name: String,
    val dataOfArrival : Timestamp,
    val companyId : Long
)

