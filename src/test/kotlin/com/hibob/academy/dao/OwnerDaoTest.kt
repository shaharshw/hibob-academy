package com.hibob.academy.dao

import com.hibob.academy.entity.Owner
import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@BobDbTest
class OwnerDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val ownerDao = OwnerDao(sql)
    val table = OwnerTable.instance
    val companyId = UUID.randomUUID()

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test create owner`() {
        val owner = Owner(
            id = UUID.randomUUID(),
            name = "John",
            companyId = UUID.randomUUID(),
            employeeId = "123",
            firstName = "John",
            lastName = "Doe"
        )
        val result = ownerDao.createOwner(owner)
        assertEquals(1, result)
    }

    @Test
    fun `test get all owners`() {
        val owners = ownerDao.getAllOwners()
        assertEquals(1, owners.size)
    }
}