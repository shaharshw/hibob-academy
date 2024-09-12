package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.entity.Owner
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class OwnerServiceTest {

    private val ownerDaoMock = mock<OwnerDao> {}
    private val ownerService = OwnerService(ownerDaoMock)

    @Test
    fun `test create owner successfully`() {
        val owner = Owner(
            id = null,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        whenever(ownerDaoMock.createOwner(any())).thenReturn(true)
        val createdOwner = ownerService.createOwner(owner)
        verify(ownerDaoMock).createOwner(any())
        assertEquals(owner.copy(id = createdOwner.id), createdOwner)
    }

    @Test
    fun `test create owner with missing first name and last name`() {
        val owner = Owner(
            id = null,
            name = "Shahar Shwartz Logashi",
            companyId = 1L,
            employeeId = "123",
            firstName = null,
            lastName = null
        )

        whenever(ownerDaoMock.createOwner(any())).thenReturn(true)
        val expectedOwner = owner.copy(firstName = "Shahar", lastName = "Shwartz Logashi")
        val createdOwner = ownerService.createOwner(owner)
        verify(ownerDaoMock).createOwner(any())

        assertEquals(expectedOwner.copy(id = createdOwner.id), createdOwner)
    }

    @Test
    fun `test create owner with missing name and first or last name throws exception`() {
        val owner = Owner(
            id = 1L,
            name = null,
            companyId = 1L,
            employeeId = "123",
            firstName = null,
            lastName = "Shwartz"
        )

        assertThrows(BadRequestException::class.java) {
            ownerService.createOwner(owner)
        }

        verify(ownerDaoMock, never()).createOwner(any())
    }

    @Test
    fun `test get all owners by company id`() {
        val owner = Owner(
            id = 1L,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        whenever(ownerDaoMock.getAllOwnersByCompanyId(1L)).thenReturn(listOf(owner))
        val owners = ownerService.getAllOwnersByCompanyId(1L)
        assertEquals(listOf(owner), owners)

        verify(ownerDaoMock).getAllOwnersByCompanyId(1L)
    }

    @Test
    fun `test create owner with the same employeeId and companyId`() {
        val owner1 = Owner(
            id = null,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val owner2 = Owner(
            id = null,
            name = "Or Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Or",
            lastName = "Shwartz"
        )

        whenever(ownerDaoMock.createOwner(any())).thenReturn(true).thenReturn(false)

        val createdOwner1 = ownerService.createOwner(owner1)
        assertEquals(owner1.copy(id = createdOwner1.id), createdOwner1)

        val exception = assertThrows(BadRequestException::class.java) {
            ownerService.createOwner(owner2)
        }
        assertEquals("Owner with the same employeeId and companyId already exists", exception.message)

    }
}