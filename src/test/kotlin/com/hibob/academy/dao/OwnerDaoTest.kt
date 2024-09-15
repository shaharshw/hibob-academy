package com.hibob.academy.dao

import com.hibob.academy.entity.CreateOwnerRequest
import com.hibob.academy.entity.Owner
import com.hibob.academy.utils.BobDbTest
import jakarta.ws.rs.BadRequestException
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

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
        val createOwnerRequest = CreateOwnerRequest(
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val ownerId = ownerDao.createOwner(createOwnerRequest)
        val expectedOwner = Owner(
            id = ownerId,
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val actualOwners = ownerDao.getAllOwnersByCompanyId(companyId)

        assertEquals(listOf(expectedOwner), actualOwners)
    }

    @Test
    fun `test create owner with missing first name and last name`() {
        val createOwnerRequest = CreateOwnerRequest(
            name = "Shahar Shwartz Logashi",
            companyId = companyId,
            employeeId = "123",
            firstName = null,
            lastName = null
        )

        val ownerId = ownerDao.createOwner(createOwnerRequest)
        val expectedOwner = Owner(
            id = ownerId,
            name = "Shahar Shwartz Logashi",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz Logashi"
        )

        val actualOwners = ownerDao.getAllOwnersByCompanyId(companyId)

        assertEquals(listOf(expectedOwner), actualOwners)
    }

    @Test
    fun `test create owner with the same employeeId and companyId`() {
        val createOwnerRequest1 = CreateOwnerRequest(
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val createOwnerRequest2 = CreateOwnerRequest(
            name = "Or Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Or",
            lastName = "Shwartz"
        )

        val ownerId = ownerDao.createOwner(createOwnerRequest1)

        val exception = assertThrows<BadRequestException> {
            ownerDao.createOwner(createOwnerRequest2)
        }

        assertEquals("Owner creation failed. No record was inserted.", exception.message)

        val expectedOwner = Owner(
            id = ownerId,
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )
        val actualOwners = ownerDao.getAllOwnersByCompanyId(companyId)

        assertEquals(listOf(expectedOwner), actualOwners)
    }

    @Test
    fun `test get all owners when no owners exist`() {
        val owners = ownerDao.getAllOwnersByCompanyId(companyId)
        assertTrue(owners.isEmpty())
    }

    @Test
    fun `test get owners with different companyId returns zero results`() {
        val companyId1 = 1L
        val companyId2 = 2L

        val createOwnerRequest1 = CreateOwnerRequest(
            name = "Shahar Shwartz",
            companyId = companyId1,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val createOwnerRequest2 = CreateOwnerRequest(
            name = "Or Shwartz",
            companyId = companyId1,
            employeeId = "124",
            firstName = "Or",
            lastName = "Shwartz"
        )

        ownerDao.createOwner(createOwnerRequest1)
        ownerDao.createOwner(createOwnerRequest2)

        val owners = ownerDao.getAllOwnersByCompanyId(companyId2)

        assertTrue(owners.isEmpty())
    }

    @Test
    fun `test get owner by id`() {
        val createOwnerRequest = CreateOwnerRequest(
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val ownerId = ownerDao.createOwner(createOwnerRequest)
        val expectedOwner = Owner(
            id = ownerId,
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val actualOwner = ownerDao.getOwnerById(ownerId)

        assertEquals(expectedOwner, actualOwner)
    }

    @Test
    fun `test get owner by id when owner does not exist`() {
        val owner = ownerDao.getOwnerById(1L)
        assertNull(owner)
    }
}