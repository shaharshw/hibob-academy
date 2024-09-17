package com.hibob.academy.service

import com.hibob.academy.dao.PetDao
import com.hibob.academy.entity.*
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.LocalDate

class PetServiceTest {

    private val petDaoMock = mock<PetDao> {}
    private val petService = PetService(petDaoMock)
    private val companyId = 999L

    @Test
    fun `test get owner by pet id`() {
        val petId = 1L
        val owner = Owner(
            id = 1L,
            name = "Shahar Shwartz",
            companyId = companyId,
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

    @Test
    fun `test adopt pets`() {
        val ownerId = 1L
        val petIds = listOf(1L, 2L)
        val adoptPetsRequest = AdoptPetsRequest(ownerId, petIds)

        whenever(petDaoMock.assignOwnerToPet(any(), any())).thenReturn(true)

        val results = petService.adoptPets(adoptPetsRequest)

        verify(petDaoMock, times(2)).assignOwnerToPet(any(), any())

        petIds.forEach { petId ->
            verify(petDaoMock).assignOwnerToPet(petId, ownerId)
        }

        assertEquals(mapOf(1L to true, 2L to true), results)

    }

    @Test
    fun `test adopt pets when some error occurred`() {

        val ownerId = 1L
        val petIds = listOf(1L, 2L)
        val adoptPetsRequest = AdoptPetsRequest(ownerId, petIds)

        whenever(petDaoMock.assignOwnerToPet(any(), any())).thenReturn(false)

        val results = petService.adoptPets(adoptPetsRequest)

        assertEquals(mapOf(1L to false, 2L to false), results)
    }

    @Test
    fun `test get all pets by owner id`() {
        val ownerId = 1L
        val pets = listOf(
            Pet(id = 1L, name = "Buddy", type = PetType.DOG, dataOfArrival = LocalDate.of(2021, 1, 1), companyId = 1L, ownerId = ownerId),
            Pet(id = 2L, name = "Mittens", type = PetType.CAT, dataOfArrival = LocalDate.of(2021, 2, 1), companyId = 1L, ownerId = ownerId)
        )

        whenever(petDaoMock.getAllPetsByOwnerId(ownerId)).thenReturn(pets)

        val actualPets = petService.getAllPetsByOwnerId(ownerId)

        assertEquals(pets, actualPets)
    }

    @Test
    fun `test get count pets by type`() {
        val petCountByType = mapOf(
            PetType.DOG to 2,
            PetType.CAT to 1
        )

        whenever(petDaoMock.getCountPetsByType(companyId)).thenReturn(petCountByType)

        val actualPetCountByType = petService.getCountPetsByType(companyId)

        assertEquals(petCountByType, actualPetCountByType)
    }

    @Test
    fun `test create pets`() {
        val pets = listOf(
            CreatePetRequest(
                name = "Buddy",
                type = PetType.DOG,
                dataOfArrival = LocalDate.of(2021, 1, 1),
                companyId = companyId,
                ownerId = 1L
            ),
            CreatePetRequest(
                name = "Mittens",
                type = PetType.CAT,
                dataOfArrival = LocalDate.of(2021, 2, 1),
                companyId = companyId,
                ownerId = 1L
            )
        )

        petService.createPets(pets)

        verify(petDaoMock).createPets(pets)
    }

    @Test
    fun `test create pets when some error occurred`() {
        val pets = listOf(
            CreatePetRequest(
                name = "Buddy",
                type = PetType.DOG,
                dataOfArrival = LocalDate.of(2021, 1, 1),
                companyId = companyId,
                ownerId = 1L
            ),
            CreatePetRequest(
                name = "Mittens",
                type = PetType.CAT,
                dataOfArrival = LocalDate.of(2021, 2, 1),
                companyId = companyId,
                ownerId = 1L
            )
        )

        whenever(petDaoMock.createPets(pets)).thenThrow(BadRequestException("No pets to create"))

        val exception = assertThrows<BadRequestException> {
            petService.createPets(pets)
        }

        assertEquals("Some error occurred while creating pets: No pets to create", exception.message)
    }
}
