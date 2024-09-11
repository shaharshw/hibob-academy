package com.hibob.academy.dao

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
import java.sql.Date
import java.time.LocalDate

@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val petDao = PetDao(sql)
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
            companyId = companyId
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
                companyId = companyId
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
            companyId = companyId
        )

        val pet2 = Pet(
            id = 2L,
            name = "Max",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
        )

        petDao.createPet(pet1)
        petDao.createPet(pet2)

        val expectedPetsResult = listOf(pet2)
        val actualPetsResult = petDao.getPetsByType(PetType.DOG, companyId)

        assertEquals(expectedPetsResult, actualPetsResult)
    }

    @Test
    fun `test get pets by invalid type`() {
        val pet1 = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId
        )

        petDao.createPet(pet1)
        val pets = petDao.getPetsByType(PetType.CAT, companyId)

        assertTrue(pets.isEmpty())
    }
}