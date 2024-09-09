package com.hibob.academy.entity

import java.sql.Timestamp

data class Pet(
    val id: Long,
    val name: String,
    val type : String,
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
