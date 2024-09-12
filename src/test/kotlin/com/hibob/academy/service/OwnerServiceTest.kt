package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.entity.Owner
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class OwnerServiceTest {

    private val ownerDaoMock = mock<OwnerDao> {}
    private val ownerService = OwnerService(ownerDaoMock)

    @Test
    fun `test create owner`() {
        val owner = Owner(
            id = 1L,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        ownerService.createOwner(owner)

        val expectedOwners = listOf(owner)
        whenever(ownerDaoMock.getAllOwnersByCompanyId(1L)).thenReturn(expectedOwners)
        val actualOwners = ownerService.getAllOwnersByCompanyId(1L)

        assertEquals(expectedOwners, actualOwners)
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

        val expectedOwner = owner.copy(firstName = "Shahar", lastName = "Shwartz Logashi")
        ownerService.createOwner(owner)

        whenever(ownerDaoMock.getAllOwnersByCompanyId(1L)).thenReturn(listOf(expectedOwner))
        val actualOwners = ownerService.getAllOwnersByCompanyId(1L)

        assertEquals(listOf(expectedOwner), actualOwners)
    }

    @Test
    fun `test create owner and get all owners`() {
        val owner = Owner(
            id = 1L,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        ownerService.createOwner(owner)

        val expectedOwners = listOf(owner)
        whenever(ownerDaoMock.getAllOwnersByCompanyId(1L)).thenReturn(expectedOwners)
        val actualOwners = ownerService.getAllOwnersByCompanyId(1L)

        assertEquals(expectedOwners, actualOwners)
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

        ownerService.createOwner(owner1)
        ownerService.createOwner(owner2)

        val expectedOwners = listOf(owner1)
        whenever(ownerDaoMock.getAllOwnersByCompanyId(1L)).thenReturn(expectedOwners)
        val actualOwners = ownerService.getAllOwnersByCompanyId(1L)

        assertEquals(expectedOwners, actualOwners)
    }
}