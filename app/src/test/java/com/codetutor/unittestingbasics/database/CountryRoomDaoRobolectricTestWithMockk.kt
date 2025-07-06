package com.codetutor.unittestingbasics.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codetutor.unittestingbasics.model.Country
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Demonstrates how to test Room database operations using MockK.
 * This approach mocks the DAO interface rather than creating a real database instance,
 * which can be faster and eliminates the need for Robolectric in many cases.
 */
@RunWith(RobolectricTestRunner::class)
class CountryRoomDaoRobolectricTestWithMockk {

    /**
     * Rule that forces Room to execute operations synchronously.
     * Still useful for other Room components that might be used alongside mocks.
     */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // MockK will create a mock implementation of this interface
    private lateinit var dao: CountryRoomDao

    /**
     * Sets up fresh mocks before each test.
     */
    @Before
    fun setUp() {
        // Create a mock of the DAO
        dao = mockk<CountryRoomDao>()
    }

    /**
     * Clears all MockK records after each test.
     */
    @After
    fun tearDown() {
        clearAllMocks()
    }

    /**
     * Tests the basic insert and read operations of the DAO using mocks.
     * Demonstrates how to verify both the insertion and retrieval operations.
     */
    @Test
    fun insertAndReadTest() = runTest {
        // Arrange
        val testCountry = Country("Australia", "left")
        val countries = listOf(testCountry)

        // Mock behavior
        coEvery { dao.insertAll(any()) } just Runs
        coEvery { dao.getAll() } returns countries

        // Act
        dao.insertAll(countries)
        val result = dao.getAll()

        // Assert
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Australia", result[0].name)
        Assert.assertEquals("left", result[0].driveSide)

        // Verify that methods were called with the correct parameters
        coVerify(exactly = 1) { dao.insertAll(countries) }
        coVerify(exactly = 1) { dao.getAll() }
    }

    /**
     * Tests inserting multiple countries and retrieving them.
     * Shows how MockK can be used to return predefined data for testing.
     */
    @Test
    fun insertMultipleAndReadTest() = runTest {
        // Arrange
        val countries = listOf(
            Country("Germany", "right"),
            Country("Japan", "left"),
            Country("France", "right")
        )

        // Mock behavior
        coEvery { dao.insertAll(any()) } just Runs
        coEvery { dao.getAll() } returns countries

        // Act
        dao.insertAll(countries)
        val result = dao.getAll()

        // Assert
        Assert.assertEquals(3, result.size)
        Assert.assertEquals(2, result.count { it.driveSide == "right" })
        Assert.assertEquals(1, result.count { it.driveSide == "left" })

        // Verify method calls
        coVerify(exactly = 1) { dao.insertAll(countries) }
        coVerify(exactly = 1) { dao.getAll() }
    }

    /**
     * Tests that inserting a country with an existing name replaces the old record.
     * This test demonstrates how to set up sequential return values with MockK.
     */
    @Test
    fun replaceExistingCountryTest() = runTest {
        // Arrange
        val initialCountry = Country("Canada", "right")
        val updatedCountry = Country("Canada", "left") // Changed drive side

        // Mock behavior - first return initial country, then return updated country
        coEvery { dao.insertAll(listOf(initialCountry)) } just Runs
        coEvery { dao.insertAll(listOf(updatedCountry)) } just Runs

        // Sequential responses for getAll() calls
        coEvery { dao.getAll() } returnsMany listOf(
            listOf(initialCountry),  // First call returns the initial country
            listOf(updatedCountry)   // Second call returns the updated country
        )

        // Act - First insertion and read
        dao.insertAll(listOf(initialCountry))
        val initialResult = dao.getAll()

        // Assert - Initial state
        Assert.assertEquals(1, initialResult.size)
        Assert.assertEquals("Canada", initialResult[0].name)
        Assert.assertEquals("right", initialResult[0].driveSide)

        // Act - Second insertion and read
        dao.insertAll(listOf(updatedCountry))
        val updatedResult = dao.getAll()

        // Assert - Updated state
        Assert.assertEquals(1, updatedResult.size)
        Assert.assertEquals("Canada", updatedResult[0].name)
        Assert.assertEquals("left", updatedResult[0].driveSide) // Should be updated to "left"

        // Verify all method calls
        coVerify(exactly = 1) { dao.insertAll(listOf(initialCountry)) }
        coVerify(exactly = 1) { dao.insertAll(listOf(updatedCountry)) }
        coVerify(exactly = 2) { dao.getAll() }
    }

    /**
     * Tests error handling when the database operation fails.
     * Demonstrates how to mock exceptions with MockK.
     */
    @Test
    fun databaseErrorTest() = runTest {
        // Arrange - Configure the mock to throw an exception
        val dbException = RuntimeException("Database error")
        coEvery { dao.getAll() } throws dbException

        // Act & Assert - Verify the exception is thrown
        try {
            dao.getAll()
            Assert.fail("Expected an exception to be thrown")
        } catch (e: RuntimeException) {
            Assert.assertEquals("Database error", e.message)
        }

        coVerify(exactly = 1) { dao.getAll() }
    }

    /**
     * Tests what happens when no data is found in the database.
     * Shows how to mock an empty result.
     */
    @Test
    fun emptyDatabaseTest() = runTest {
        // Arrange - Configure the mock to return an empty list
        coEvery { dao.getAll() } returns emptyList()

        // Act
        val result = dao.getAll()

        // Assert
        Assert.assertTrue(result.isEmpty())

        coVerify(exactly = 1) { dao.getAll() }
    }

    /**
     * Tests capturing and verifying the exact parameters passed to DAO methods.
     * This demonstrates MockK's powerful parameter capturing feature.
     */
    @Test
    fun captureParametersTest() = runTest {
        // Arrange
        val countries = listOf(
            Country("Spain", "right"),
            Country("India", "left")
        )

        // Setup parameter capturing
        val slot = slot<List<Country>>()

        // Mock behavior
        coEvery { dao.insertAll(capture(slot)) } just Runs
        coEvery { dao.getAll() } returns countries

        // Act
        dao.insertAll(countries)

        // Assert - Verify the exact objects passed to insertAll
        Assert.assertEquals(2, slot.captured.size)
        Assert.assertEquals("Spain", slot.captured[0].name)
        Assert.assertEquals("India", slot.captured[1].name)

        coVerify(exactly = 1) { dao.insertAll(countries) }
    }
}
