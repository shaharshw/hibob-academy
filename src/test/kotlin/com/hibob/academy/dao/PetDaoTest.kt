package com.hibob.academy.dao

import com.hibob.academy.entity.Pet
import com.hibob.academy.entity.PetType
import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Date

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
            dataOfArrival = Date.valueOf("2021-01-01"),
            companyId = companyId,
            ownerId = 1L
        )

        petDao.createPet(pet)
        val pets = petDao.getAllPets()

        assertEquals(1, pets.size)
    }

    @Test
    fun `test get pet by type`() {
        val pet1 = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.CAT,
            dataOfArrival = Date.valueOf("2021-01-01"),
            companyId = companyId,
            ownerId = 1L
        )

        petDao.createPet(pet1)
        val pets = petDao.getPetsByType(PetType.DOG)

        assertEquals(0, pets.size)
    }

    @Test
    fun `test get all pets by owner id`() {
        val pet1 = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.CAT,
            dataOfArrival = Date.valueOf("2021-01-01"),
            companyId = companyId,
            ownerId = 1L
        )

        val pet2 = Pet(
            id = 2L,
            name = "Buddy",
            type = PetType.CAT,
            dataOfArrival = Date(2021, 1, 1),
            companyId = companyId,
            ownerId = 2L
        )

        petDao.createPet(pet1)
        petDao.createPet(pet2)
        val pets = petDao.getAllPetsByOwnerId(1L)

        assertEquals(1, pets.size)
    }

    @Test
    fun `test get count pets by type`() {
        val pet1 = Pet(
            id = 1L,
            name = "Buddy",
            type = PetType.CAT,
            dataOfArrival = Date.valueOf("2021-01-01"),
            companyId = companyId,
            ownerId = 1L
        )

        val pet2 = Pet(
            id = 2L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = Date.valueOf("2021-01-01"),
            companyId = companyId,
            ownerId = 2L
        )

        val pet3 = Pet(
            id = 3L,
            name = "Buddy",
            type = PetType.DOG,
            dataOfArrival = Date.valueOf("2021-01-01"),
            companyId = companyId,
            ownerId = 3L
        )

        petDao.createPet(pet1)
        petDao.createPet(pet2)
        petDao.createPet(pet3)

        val countPetsByType = petDao.getCountPetsByType()

        assertEquals(2, countPetsByType.size)
        assertEquals(1, countPetsByType[PetType.CAT.name])
        assertEquals(2, countPetsByType[PetType.DOG.name])
    }
}






