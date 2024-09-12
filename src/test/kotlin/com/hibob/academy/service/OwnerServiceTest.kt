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
            id = 1L,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        whenever(ownerDaoMock.createOwner(any())).thenReturn(true)
        val createdOwner = ownerService.createOwner(owner)
        verify(ownerDaoMock).createOwner(owner)
        assertEquals(owner, createdOwner)
    }

    @Test
    fun `test create owner with missing first name and last name`() {
        val owner = Owner(
            id = 1L,
            name = "Shahar Shwartz Logashi",
            companyId = 1L,
            employeeId = "123",
            firstName = null,
            lastName = null
        )

        whenever(ownerDaoMock.createOwner(any())).thenReturn(true)
        val expectedOwner = owner.copy(firstName = "Shahar", lastName = "Shwartz Logashi")
        val createdOwner = ownerService.createOwner(owner)
        verify(ownerDaoMock).createOwner(expectedOwner)

        assertEquals(expectedOwner, createdOwner)
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

        // Assert that the service throws BadRequestException
        assertThrows(BadRequestException::class.java) {
            ownerService.createOwner(owner)
        }

        // Verify that the DAO's createOwner method was never called
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
            id = 1L,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = null,
            lastName = null
        )

        val owner2 = Owner(
            id = 2L,
            name = "Or Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Or",
            lastName = "Shwartz"
        )

        whenever(ownerDaoMock.createOwner(owner1)).thenReturn(true)
        whenever(ownerDaoMock.createOwner(owner2)).thenReturn(false)

        val createdOwner1 = ownerService.createOwner(owner1)
        assertEquals(owner1, createdOwner1)

        val exception = assertThrows(BadRequestException::class.java) {
            ownerService.createOwner(owner2)
        }
        assertEquals("Owner with the same employeeId and companyId already exists", exception.message)

        verify(ownerDaoMock).createOwner(owner1)
        verify(ownerDaoMock).createOwner(owner2)
    }

}