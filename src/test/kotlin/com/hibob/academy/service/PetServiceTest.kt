package com.hibob.academy.service

import com.hibob.academy.dao.PetDao
import com.hibob.academy.entity.Owner
import com.hibob.academy.entity.OwnerById
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class PetServiceTest {

    private val petDaoMock = mock<PetDao> {}
    private val petService = PetService(petDaoMock)

    @Test
    fun `test get owner by pet id`() {
        val petId = 1L
        val owner = Owner(
            id = 1L,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val ownerById = OwnerById(
            petExists = true,
            owner = owner
        )

        whenever(petDaoMock.getOwnerByPetId(petId)).thenReturn(ownerById)

        val actualOwner = petService.getOwnerByPetId(petId)

        assertEquals(owner, actualOwner)
    }

    @Test
    fun `test get owner by pet id when pet not found`() {
        val petId = 1L

        whenever(petDaoMock.getOwnerByPetId(petId)).thenReturn(null)

        val exception = assertThrows<BadRequestException> {
            petService.getOwnerByPetId(petId)
        }

        assertEquals("Some error occurred while fetching owner information for pet with id $petId", exception.message)
    }

    @Test
    fun `test get owner by pet id when pet has no owner`() {
        val petId = 1L

        val ownerById = OwnerById(
            petExists = true,
            owner = null
        )

        whenever(petDaoMock.getOwnerByPetId(petId)).thenReturn(ownerById)

        val actualOwner = petService.getOwnerByPetId(petId)

        assertNull(actualOwner)
    }

    @Test
    fun `test get owner by pet id when owner information is missing`() {
        val petId = 1L

        val ownerById = OwnerById(
            petExists = true,
            owner = null
        )

        whenever(petDaoMock.getOwnerByPetId(petId)).thenReturn(ownerById)

        val actualOwner = petService.getOwnerByPetId(petId)

        assertNull(actualOwner)
    }

    @Test
    fun `test assign owner to pet`() {
        val petId = 1L
        val ownerId = 1L

        whenever(petDaoMock.assignOwnerToPet(petId, ownerId)).thenReturn(true)

        val actualResult = petService.assignOwnerToPet(petId, ownerId)

        assertTrue(actualResult)
    }

    @Test
    fun `test assign owner to pet when pet not found`() {
        val petId = 1L
        val ownerId = 1L

        whenever(petDaoMock.assignOwnerToPet(petId, ownerId)).thenReturn(false)

        val actualResult = petService.assignOwnerToPet(petId, ownerId)

        assertFalse(actualResult)
    }
}
