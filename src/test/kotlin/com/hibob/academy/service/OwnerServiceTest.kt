package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.entity.CreateOwnerRequest
import com.hibob.academy.entity.Owner
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class OwnerServiceTest {

    private val ownerDaoMock = mock<OwnerDao> {}
    private val ownerService = OwnerService(ownerDaoMock)

    @Test
    fun `test create owner successfully`() {
        val createOwnerRequest = CreateOwnerRequest(
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        whenever(ownerDaoMock.createOwner(any())).thenReturn(1L)
        val createdOwnerId = ownerService.createOwner(createOwnerRequest)
        verify(ownerDaoMock).createOwner(any())
        assertEquals(1L, createdOwnerId)
    }

    @Test
    fun `test create owner with missing first name and last name`() {
        val createOwnerRequest = CreateOwnerRequest(
            name = "Shahar Shwartz Logashi",
            companyId = 1L,
            employeeId = "123",
            firstName = null,
            lastName = null
        )

        whenever(ownerDaoMock.createOwner(any())).thenReturn(1L)
        val createdOwnerId = ownerService.createOwner(createOwnerRequest)
        verify(ownerDaoMock).createOwner(any())
        assertEquals(1L, createdOwnerId)
    }

    @Test
    fun `test create owner with missing name and first or last name throws exception`() {
        val createOwnerRequest = CreateOwnerRequest(
            name = null,
            companyId = 1L,
            employeeId = "123",
            firstName = null,
            lastName = "Shwartz"
        )

        assertThrows(BadRequestException::class.java) {
            ownerService.createOwner(createOwnerRequest)
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
        val createOwnerRequest1 = CreateOwnerRequest(
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val createOwnerRequest2 = CreateOwnerRequest(
            name = "Or Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Or",
            lastName = "Shwartz"
        )

        whenever(ownerDaoMock.createOwner(any())).thenReturn(1L).thenThrow(IllegalStateException("Owner creation failed. No record was inserted."))

        val createdOwnerId1 = ownerService.createOwner(createOwnerRequest1)
        assertEquals(1L, createdOwnerId1)

        val exception = assertThrows<IllegalStateException> {
            ownerService.createOwner(createOwnerRequest2)
        }
        assertEquals("Owner creation failed. No record was inserted.", exception.message)
    }
}