package com.codetutor.unittestingbasics.database

import com.codetutor.unittestingbasics.doubles.FakeCountryDao
import com.codetutor.unittestingbasics.model.Country
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class InMemoryCountryDaoTest {
    private lateinit var fakeDao: InMemoryCountryDao

    @Before
    fun setUp(){
        fakeDao = InMemoryCountryDao()
    }

    @Test
    fun insertAndReadTest() = runTest {
        //Arrange
        val testCountry = Country("Brazil", "right")
        //Act
        fakeDao.insertAll(listOf(testCountry))

        val result = fakeDao.getAll()
        //Assert
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Brazil", result[0].name)
        Assert.assertEquals("right", result[0].driveSide)

    }

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
}