package com.hibob.academy.entity

import java.sql.Date

data class Pet(
    val id: Long,
    val name: String,
    val type : PetType,
    val dataOfArrival : Date,
    val companyId : Long,
    val ownerId : Long
)

data class Owner(
    val id: Long,
    var name: String?,
    val companyId: Long,
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
    val dataOfArrival : Date,
    val companyId : Long
)