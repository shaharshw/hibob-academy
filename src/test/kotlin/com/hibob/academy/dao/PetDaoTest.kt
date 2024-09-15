package com.hibob.academy.dao

import com.hibob.academy.entity.CreateOwnerRequest
import com.hibob.academy.entity.Owner
import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val petDao = PetDao(sql)
    private val ownerDao = OwnerDao(sql)
    val table = PetTable.instance
    val companyId = 1L

    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test create pet and get all pets`() {

        val pet = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = 1L
        )

        petDao.createPet(pet)

        val expectedPets = listOf(pet)
        val actualPets = petDao.getAllPetsByCompanyId(companyId)

        assertEquals(expectedPets, actualPets)
    }

    @Test
    fun `test create pet with invalid type`() {
        val invalidPetType = "INVALID_TYPE"

        val exception = assertThrows<IllegalArgumentException> {
            val pet = Pet(
                id = 1L,
                name = "Buddy",
                type = PetType.fromString(invalidPetType), // This should throw an exception or handle the error
                dataOfArrival = LocalDate.of(2021, 1, 1),
                companyId = companyId,
                ownerId = 1L
            )
            petDao.createPet(pet)
        }

        assertEquals("No enum constant com.hibob.academy.entity.PetType.$invalidPetType", exception.message)
    }

    @Test
    fun `test get pet by type`() {
        val pet1 = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.CAT,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = 1L
        )

        val pet2 = Pet(
            id = 2L,
            name = "Max",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = 1L
        )

        petDao.createPet(pet1)
        petDao.createPet(pet2)

        val expectedPetsResult = listOf(pet2)
        val actualPetsResult = petDao.getPetsByType(PetType.DOG, companyId)

        assertEquals(expectedPetsResult, actualPetsResult)
    }

    @Test
    fun `test get pets with different companyId returns zero results`() {
        val companyId1 = 1L
        val companyId2 = 2L

        val pet1 = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId1,
            ownerId = 1L
        )

        val pet2 = Pet(
            id = 2L,
            name = "Max",
            type = PetType.CAT,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId1,
            ownerId = 1L
        )

        petDao.createPet(pet1)
        petDao.createPet(pet2)

        val pets = petDao.getAllPetsByCompanyId(companyId2)

        assertTrue(pets.isEmpty())
    }

    @Test
    fun `test get pets by invalid type`() {
        val pet1 = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = 1L
        )

        petDao.createPet(pet1)
        val pets = petDao.getPetsByType(PetType.CAT, companyId)

        assertTrue(pets.isEmpty())
    }

    @Test
    fun `test get pet by id`() {
        val pet = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = 1L
        )

        petDao.createPet(pet)

        val expectedPet = pet
        val actualPet = petDao.getPetById(pet.id)

        assertEquals(expectedPet, actualPet)
    }

    @Test
    fun `test get pet by id when pet does not exist`() {
        val pet = petDao.getPetById(1L)
        assertNull(pet)
    }

    @Test
    fun `test assign owner to pet`() {
        val pet = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = 1L
        )

        petDao.createPet(pet)

        val ownerId = 2L
        val result = petDao.assignOwnerToPet(pet.id, ownerId) ?: false
        val actualPet = petDao.getPetById(pet.id)

        assertTrue(result)
        assertEquals(ownerId, actualPet?.ownerId)
    }

    @Test
    fun `test assign owner to pet when pet does not exist`() {
        val ownerId = 2L
        val result = petDao.assignOwnerToPet(1L, ownerId) ?: false
        assertFalse(result)
    }

    @Test
    fun `test get owner by pet id when owner exists`() {
        val owner = CreateOwnerRequest(
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "12345",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val ownerId = ownerDao.createOwner(owner)

        val pet = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = ownerId
        )

        petDao.createPet(pet)

        val ownerById = petDao.getOwnerByPetId(pet.id)

        assertNotNull(ownerById)
        assertTrue(ownerById!!.petExists)
        assertEquals(ownerId, ownerById.owner?.id)

        sql.deleteFrom(OwnerTable.instance).where(OwnerTable.instance.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test get owner by pet id when owner does not exist`() {
        val pet = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = null
        )

        petDao.createPet(pet)

        val ownerById = petDao.getOwnerByPetId(pet.id)

        assertNotNull(ownerById)
        assertTrue(ownerById!!.petExists)
        assertNull(ownerById.owner)
    }

    @Test
    fun `test get owner by pet id when pet does not exist`() {
        val ownerById = petDao.getOwnerByPetId(1L)
        assertNull(ownerById)
        assertFalse(ownerById?.petExists ?: false)
    }
}