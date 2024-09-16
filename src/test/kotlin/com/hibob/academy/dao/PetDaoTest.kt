package com.hibob.academy.dao

import com.hibob.academy.entity.*
import com.hibob.academy.utils.BobDbTest
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

@BobDbTest
class PetDaoTest @Autowired constructor(private val sql: DSLContext) {

    private val petDao = PetDao(sql)
    private val ownerDao = OwnerDao(sql)
    val table = PetTable.instance
    val companyId = 999L
    val pet1 = CreatePetRequest(
        name = "Buddy",
        type = PetType.DOG,
        dataOfArrival = LocalDate.of(2021, 1, 1),
        companyId = companyId,
        ownerId = 1L
    )
    val pet2 = CreatePetRequest(
        name = "Max",
        type = PetType.DOG,
        dataOfArrival = LocalDate.of(2021, 1, 1),
        companyId = companyId,
        ownerId = 1L
    )



    @BeforeEach
    @AfterEach
    fun cleanup() {
        sql.deleteFrom(table).where(table.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test create pet and get all pets`() {
        val petId = petDao.createPet(pet1)

        val expectedPets = listOf(
            Pet(
                id = petId,
                name = pet1.name,
                type = pet1.type,
                dataOfArrival = pet1.dataOfArrival,
                companyId = pet1.companyId,
                ownerId = pet1.ownerId
            )
        )
        val actualPets = petDao.getAllPetsByCompanyId(companyId)

        assertEquals(expectedPets, actualPets)
    }

    @Test
    fun `test get pet by type`() {
        val petId1 = petDao.createPet(pet1)
        val petId2 = petDao.createPet(pet2)

        val expectedPetsResult = listOf(
            Pet(
                id = petId1,
                name = pet1.name,
                type = pet1.type,
                dataOfArrival = pet1.dataOfArrival,
                companyId = pet1.companyId,
                ownerId = pet1.ownerId
            ),
            Pet(
                id = petId2,
                name = pet2.name,
                type = pet2.type,
                dataOfArrival = pet2.dataOfArrival,
                companyId = pet2.companyId,
                ownerId = pet2.ownerId
            )
        )
        val actualPetsResult = petDao.getPetsByType(PetType.DOG, companyId)

        assertEquals(expectedPetsResult, actualPetsResult)
    }

    @Test
    fun `test get pets with different companyId returns zero results`() {

        val companyId2 = 2L

        petDao.createPet(pet1)
        petDao.createPet(pet2)

        val pets = petDao.getAllPetsByCompanyId(companyId2)

        assertTrue(pets.isEmpty())
    }

    @Test
    fun `test get pets by invalid type`() {

        petDao.createPet(pet1)
        val pets = petDao.getPetsByType(PetType.CAT, companyId)

        assertTrue(pets.isEmpty())
    }

    @Test
    fun `test get pet by id`() {

        val petId = petDao.createPet(pet1)

        val expectedPet = Pet(
            id = petId,
            name = pet1.name,
            type = pet1.type,
            dataOfArrival = pet1.dataOfArrival,
            companyId = pet1.companyId,
            ownerId = pet1.ownerId
        )

        val actualPet = petDao.getPetById(petId)

        assertEquals(expectedPet, actualPet)
    }

    @Test
    fun `test get pet by id when pet does not exist`() {
        val pet = petDao.getPetById(999L)
        assertNull(pet)
    }

    @Test
    fun `test assign owner to pet`() {

        val petId = petDao.createPet(pet1)

        val ownerId = 2L
        val result = petDao.assignOwnerToPet(petId, ownerId) ?: false
        val actualPet = petDao.getPetById(petId)

        assertTrue(result)
        assertEquals(ownerId, actualPet?.ownerId)
    }

    @Test
    fun `test assign owner to pet when pet does not exist`() {
        val ownerId = 2L
        val result = petDao.assignOwnerToPet(999L, ownerId) ?: false
        assertFalse(result)
    }

    @Test
    fun `test get owner by pet id when owner exists`() {
        val owner = CreateOwnerRequest(
            name = "Shahar Shwartz",
            companyId = companyId,
            employeeId = "1234567",
            firstName = "Shahar",
            lastName = "Shwartz"
        )

        val ownerId = ownerDao.createOwner(owner)

        val petId = petDao.createPet(pet1.copy(ownerId = ownerId))

        val ownerById = petDao.getOwnerByPetId(petId)

        assertNotNull(ownerById)
        assertTrue(ownerById!!.petExists)
        assertEquals(ownerId, ownerById.owner?.id)

        sql.deleteFrom(OwnerTable.instance).where(OwnerTable.instance.companyId.eq(companyId)).execute()
    }

    @Test
    fun `test get owner by pet id when owner does not exist`() {

        val petId = petDao.createPet(pet1)

        val ownerById = petDao.getOwnerByPetId(petId)

        assertNotNull(ownerById)
        assertTrue(ownerById!!.petExists)
        assertNull(ownerById.owner)
    }

    @Test
    fun `test get owner by pet id when pet does not exist`() {
        val ownerById = petDao.getOwnerByPetId(999L)
        assertNull(ownerById?.owner)
        assertFalse(ownerById?.petExists ?: false)
    }

    @Test
    fun `test get all pets by owner id`() {

        petDao.createPet(pet1)

        val pets = petDao.getAllPetsByOwnerId(1L)

        assertEquals(1, pets.size)
    }

    @Test
    fun `test get all pets by owner id when owner does not exist`() {

        petDao.createPet(pet1)
        val pets = petDao.getAllPetsByOwnerId(999L)
        assertTrue(pets.isEmpty())
    }

    @Test
    fun `test get count pets by type`() {

        val pet3 = CreatePetRequest(
            name = "Buddy",
            type = PetType.CAT,
            dataOfArrival = LocalDate.of(2021, 1, 1),
            companyId = companyId,
            ownerId = 3L
        )

        petDao.createPet(pet1)
        petDao.createPet(pet2)
        petDao.createPet(pet3)

        val countPetsByType = petDao.getCountPetsByType(companyId)

        assertEquals(2, countPetsByType.size)
        assertEquals(1, countPetsByType[PetType.CAT])
        assertEquals(2, countPetsByType[PetType.DOG])
    }

    @Test
    fun `test create pets`() {
        val pets = listOf(pet1, pet2)

        val petIds = petDao.createPets(pets)

        assertEquals(2, petIds.size)
    }

}
