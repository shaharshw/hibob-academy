package com.hibob.oop

interface Location {
    val street: String
    val city: String
    val county: String
}

data class USLocation(
    override val street : String,
    override val city : String,
    override val county : String,
    val zipCode : String
) : Location

data class UKLocation(
    override val street : String,
    override val city : String,
    override val county : String,
    val postCode : String
) : Location


