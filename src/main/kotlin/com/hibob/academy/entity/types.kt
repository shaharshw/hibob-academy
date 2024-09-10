package com.hibob.academy.entity

import java.sql.Timestamp

data class Pet(
    val id: Long,
    val name: String,
    val type : PetType,
    val dataOfArrival : Timestamp,
    val companyId : Long
)

data class Owner(
    val id: Long,
    var name: String?,
    val companyId: Long,
    val employeeId : Long,
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

