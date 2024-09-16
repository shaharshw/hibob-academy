package com.hibob.academy.dao

import com.hibob.academy.utils.JooqTable
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.springframework.stereotype.Component

class VaccineTable(tableName: String = "vaccine") : JooqTable(tableName) {
    val id = createBigIntField("id")
    val name = createVarcharField("name")

    companion object {
        val instance = VaccineTable()
    }
}


@Component
class VaccineDao @Inject constructor(
    private val sql: DSLContext

) {

    private val vaccineTable = VaccineTable.instance
}