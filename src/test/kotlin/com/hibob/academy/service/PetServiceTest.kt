package com.hibob.academy.service

import com.hibob.academy.dao.OwnerDao
import com.hibob.academy.dao.PetDao
import com.hibob.academy.entity.Owner
import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import jakarta.ws.rs.BadRequestException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.LocalDate

class PetServiceTest {

    private val petDaoMock = mock<PetDao> {}
    private val ownerDaoMock = mock<OwnerDao> {}
    private val petService = PetService(petDaoMock, ownerDaoMock)

    @Test
    fun `test get owner by pet id`() {
        val petId = 1L
        val ownerId = 1L
        val pet = Pet(
            id = petId,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = 1L,
            ownerId = ownerId
        )
        val owner = Owner(
            id = ownerId,
            name = "Shahar Shwartz",
            companyId = 1L,
            employeeId = "123",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        whenever(petDaoMock.getPetById(petId)).thenReturn(pet)
        whenever(ownerDaoMock.getOwnerById(ownerId)).thenReturn(owner)

        val actualOwner = petService.getOwnerByPetId(petId)

        assertEquals(owner, actualOwner)
    }

    @Test
    fun `test get owner by pet id when pet not found`() {
        val petId = 1L

        whenever(petDaoMock.getPetById(petId)).thenReturn(null)

        val exception = assertThrows<BadRequestException> {
            petService.getOwnerByPetId(petId)
        }

        assertEquals("Pet with ID $petId not found", exception.message)
    }
}
