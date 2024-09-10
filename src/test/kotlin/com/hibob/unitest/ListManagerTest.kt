package com.hibob.unitest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListManagerTest {

    lateinit var listManager: ListManager

    @BeforeEach
    fun setUp() {
        listManager = ListManager()
    }

    @Test
    fun `should add a unique person`() {
        val person = Person("Shahar", 27)
        assertTrue(listManager.addPerson(person))
    }

    @Test
    fun `should throw exception when adding a duplicate person`() {
        val person = Person("Shahar", 27)
        listManager.addPerson(person)
        assertThrows(IllegalArgumentException::class.java) {
            listManager.addPerson(person)
        }
    }

    @Test
    fun `should grow list when adding multiple people`() {
        val person1 = Person("Shahar", 27)
        val person2 = Person("Or", 30)
        val person3 = Person("Alice", 25)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)
        assertEquals(3, listManager.getPeopleSortedByAgeAndName().size)
    }

    @Test
    fun `should remove a person that exists in the list`() {
        val person = Person("Shahar", 27)
        listManager.addPerson(person)
        assertTrue(listManager.removePerson(person))
    }

    @Test
    fun `should return false when removing a person that does not exist`() {
        val person = Person("Shahar", 27)
        assertFalse(listManager.removePerson(person))
    }

    @Test
    fun `should maintain correct state after multiple add and remove operations`() {
        val person1 = Person("Shahar", 27)
        val person2 = Person("Or", 30)
        val person3 = Person("Alice", 25)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)
        listManager.removePerson(person1)
        listManager.removePerson(person2)
        assertEquals(1, listManager.getPeopleSortedByAgeAndName().size)
    }

    @Test
    fun `should return empty list when getting people from an empty list`() {
        assertTrue(listManager.getPeopleSortedByAgeAndName().isEmpty())
    }

    @Test
    fun `should return one person when getting people from a list with one person`() {
        val person = Person("Shahar", 27)
        listManager.addPerson(person)
        assertEquals(listOf(person), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `should sort people by age and name when getting people from a list with multiple people`() {
        val person1 = Person("Shahar", 27)
        val person2 = Person("Or", 30)
        val person3 = Person("Alice", 25)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)
        assertEquals(listOf(person3, person1, person2), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `should handle edge cases when getting people sorted by age and name`() {
        val person1 = Person("Shahar", 27)
        val person2 = Person("Shahar", 30)
        val person3 = Person("Alice", 25)
        val person4 = Person("Or", 25)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)
        listManager.addPerson(person4)
        assertEquals(listOf(person3, person4, person1, person2), listManager.getPeopleSortedByAgeAndName())
    }

    @Test
    fun `should return null when calculating statistics for an empty list`() {
        assertNull(listManager.calculateStatistics())
    }

    @Test
    fun `should calculate statistics for a list with one person`() {
        val person = Person("Shahar", 27)
        listManager.addPerson(person)
        val statistics = listManager.calculateStatistics()
        assertEquals(27.0, statistics?.averageAge)
        assertEquals(person, statistics?.youngest)
        assertEquals(person, statistics?.oldest)
        assertEquals(mapOf(27 to 1), statistics?.ageCount)
    }

    @Test
    fun `should calculate statistics for a list with multiple people`() {
        val person1 = Person("Shahar", 27)
        val person2 = Person("Or", 30)
        val person3 = Person("Alice", 25)
        val person4 = Person("Bob", 27)
        listManager.addPerson(person1)
        listManager.addPerson(person2)
        listManager.addPerson(person3)
        listManager.addPerson(person4)
        val statistics = listManager.calculateStatistics()
        assertEquals(27.25, statistics?.averageAge)
        assertEquals(person3, statistics?.youngest)
        assertEquals(person2, statistics?.oldest)
        assertEquals(mapOf(27 to 2, 30 to 1, 25 to 1), statistics?.ageCount)
    }
}