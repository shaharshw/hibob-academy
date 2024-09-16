package com.hibob.academy.dao

import com.hibob.academy.utils.JooqTable
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.springframework.stereotype.Component

class VaccineToPetTable(tableName: String = "vaccine_to_pet") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val vaccineId = createBigIntField("vaccine_id")
    val petId = createBigIntField("pet_id")
    val dateOfVaccine = createDateField("date_of_vaccine")

    companion object {
        val instance = VaccineToPetTable()
    }
}

@Component
class VaccineToPetDao @Inject constructor(
    private val sql: DSLContext
) {

    private val vaccineToPetTable = VaccineToPetTable.instance
}