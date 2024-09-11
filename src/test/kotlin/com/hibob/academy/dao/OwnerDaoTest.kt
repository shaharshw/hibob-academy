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
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        ownerDao.createOwner(owner)

        val expectedOwners = listOf(owner)
        val actualOwners = ownerDao.getAllOwnersByCompanyId(companyId)

        assertEquals(expectedOwners, actualOwners)
    }

    @Test
    fun `test create owner with missing first name and last name`() {
        val owner = Owner(
            id = 1L,
            name = "Shahar Shwartz Logashi",
            companyId = companyId,
            employeeId = "123",
            firstName = null,
            lastName = null
        )

        ownerDao.createOwner(owner)

        val expectedOwners = listOf(owner.copy(firstName = "Shahar", lastName = "Shwartz Logashi"))
        val actualOwners = ownerDao.getAllOwnersByCompanyId(companyId)

        assertEquals(expectedOwners, actualOwners)
    }

    @Test
    fun `test create owner with the same employeeId and companyId`() {

        val owner1 = Owner(
            id = 1L,
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = null,
            lastName = null
        )

        val owner2 = Owner(
            id = 2L,
            name = "Or Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Or",
            lastName = "Shwartz"
        )

        ownerDao.createOwner(owner1)
        ownerDao.createOwner(owner2)

        val expectedOwners = listOf(owner1.copy(firstName = "Shahar", lastName = "Shwartz"), owner2)
        val actualOwners = ownerDao.getAllOwnersByCompanyId(companyId)

        assertEquals(expectedOwners, actualOwners)
    }

    @Test
    fun `test get all owners when no owners exist`() {
        val owners = ownerDao.getAllOwnersByCompanyId(companyId)
        assertTrue(owners.isEmpty())
    }
}