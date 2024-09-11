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
    val companyId = 1L

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test create owner and get all owners`() {
        val owner = Owner(
            id = 1L,
            name = "John",
            companyId = companyId,
            employeeId = "123",
            firstName = "John",
            lastName = "Doe"
        )

        ownerDao.createOwner(owner)
        val owners = ownerDao.getAllOwners()

        assertEquals(1, owners.size)
    }

    @Test
    fun `test create owner with the same employeeId and companyId`() {

        val owner1 = Owner(
            id = 1L,
            name = "John",
            companyId = companyId,
            employeeId = "123",
            firstName = "John",
            lastName = "Doe"
        )

        val owner2 = Owner(
            id = 2L,
            name = "Shahar",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        ownerDao.createOwner(owner1)
        ownerDao.createOwner(owner2)

        val owners = ownerDao.getAllOwners()
        assertEquals(1, owners.size)
    }
}