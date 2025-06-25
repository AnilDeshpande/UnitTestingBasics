package com.codetutor.unittestingbasics.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.codetutor.unittestingbasics.model.Country
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CountryRoomDaoRobolectricTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: CountryRoomDao

    @Before
    fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.countryDao()
    }

    @After
    fun tearDown(){
        db.close()
    }

    @Test
    fun insertAndReadTest() = runTest {
        // Arrange
        val testCountry = Country("Australia", "left")

        // Act
        dao.insertAll(listOf(testCountry))
        val result = dao.getAll()

        // Assert
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Australia", result[0].name)
        Assert.assertEquals("left", result[0].driveSide)

    }

    @Test
    fun insertMultipleAndReadTest() = runTest {
        // Arrange
        val countries = listOf(
            Country("Germany", "right"),
            Country("Japan", "left"),
            Country("France", "right")
        )

        // Act
        dao.insertAll(countries)
        val result = dao.getAll()

        // Assert
        Assert.assertEquals(3, result.size)
        Assert.assertEquals(2, result.count { it.driveSide == "right" })
        Assert.assertEquals(1, result.count { it.driveSide == "left" })
    }

    @Test
    fun replaceExistingCountryTest() = runTest {
        // Arrange - Insert initial country
        val initialCountry = Country("Canada", "right")
        dao.insertAll(listOf(initialCountry))

        // Act - Insert updated country with same name
        val updatedCountry = Country("Canada", "left") // Changed drive side
        dao.insertAll(listOf(updatedCountry))

        // Get results
        val result = dao.getAll()

        // Assert - Should still have only one record with updated drive side
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Canada", result[0].name)
        Assert.assertEquals("left", result[0].driveSide) // Should be updated to "left"
    }
}