package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Pure JUnit test for CountryDAO without using Room or Robolectric.
 * Uses a fake in-memory implementation of CountryDAO to test the contract.
 */
class InMemoryCountryDaoTest {

    private lateinit var fakeDao: InMemoryCountryDao

    @Before
    fun setUp() {
        fakeDao = InMemoryCountryDao()
    }

    /**
     * Tests the basic insert and read operations of the DAO.
     * Verifies that a country can be inserted and retrieved with all properties intact.
     */
    @Test
    fun insertAndReadTest() = runTest {
        // Arrange
        val testCountry = Country("Brazil", "right")

        // Act
        fakeDao.insertAll(listOf(testCountry))
        val result = fakeDao.getAll()

        // Assert
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Brazil", result[0].name)
        Assert.assertEquals("right", result[0].driveSide)
    }

    /**
     * Tests inserting multiple countries and retrieving them.
     */
    @Test
    fun insertMultipleAndReadTest() = runTest {
        // Arrange
        val countries = listOf(
            Country("Spain", "right"),
            Country("Thailand", "left"),
            Country("Italy", "right")
        )

        // Act
        fakeDao.insertAll(countries)
        val result = fakeDao.getAll()

        // Assert
        Assert.assertEquals(3, result.size)
        Assert.assertEquals(2, result.count { it.driveSide == "right" })
        Assert.assertEquals(1, result.count { it.driveSide == "left" })
    }

    /**
     * Tests that inserting a country with an existing name replaces the old record.
     */
    @Test
    fun replaceExistingCountryTest() = runTest {
        // Arrange - Insert initial country
        val initialCountry = Country("Sweden", "right")
        fakeDao.insertAll(listOf(initialCountry))

        // Act - Insert updated country with same name
        val updatedCountry = Country("Sweden", "left") // Changed drive side
        fakeDao.insertAll(listOf(updatedCountry))

        // Get results
        val result = fakeDao.getAll()

        // Assert - Should still have only one record with updated drive side
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Sweden", result[0].name)
        Assert.assertEquals("left", result[0].driveSide) // Should be updated to "left"
    }

    /**
     * A fake implementation of CountryDAO that uses a simple in-memory map
     * to store and retrieve countries. This implementation mimics the behavior
     * of the actual Room DAO without requiring database access.
     */
    class InMemoryCountryDao : CountryDAO {
        private val countriesMap = mutableMapOf<String, Country>()

        /**
         * Returns all countries stored in the in-memory map.
         */
        override suspend fun getAll(): List<Country> {
            return countriesMap.values.toList()
        }

        /**
         * Inserts all countries into the in-memory map.
         * If a country with the same name already exists, it will be replaced.
         */
        override suspend fun insertAll(countries: List<Country>) {
            countries.forEach { country ->
                countriesMap[country.name] = country
            }
        }
    }
}
