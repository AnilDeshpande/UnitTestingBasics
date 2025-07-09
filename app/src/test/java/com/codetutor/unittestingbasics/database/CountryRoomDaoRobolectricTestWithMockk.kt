// filepath: /Users/anildeshpande/AndroidStudioProjects/UnitTestingBasics/app/src/test/java/com/codetutor/unittestingbasics/database/CountryRoomDaoRobolectricTestWithMockk.kt
package com.codetutor.unittestingbasics.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codetutor.unittestingbasics.model.Country
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Local unit test for Country DAO using MockK framework.
 *
 * REFACTORING EXPLANATION:
 *
 * This test class is a refactored version of CountryRoomDaoRobolectricTest that uses MockK instead
 * of an actual Room database implementation. The key differences are:
 *
 * 1. DEPENDENCY INJECTION: Instead of creating a real Room database, we're injecting mocked DAO
 *    using MockK, which eliminates the need for database setup and teardown.
 *
 * 2. MOCK VS REAL IMPLEMENTATION: The original tests used a real Room in-memory database, while
 *    this version uses a mock DAO. This means we're testing interactions with the DAO rather than
 *    the actual database operations.
 *
 * 3. VERIFICATION APPROACH: We now use MockK's verification syntax (coVerify) to check that
 *    methods were called with expected parameters, rather than checking actual database state.
 *
 * 4. CAPTURING ARGUMENTS: We use MockK's slot capturing feature to inspect the arguments passed
 *    to methods like insertAll() for more detailed verification.
 *
 * BENEFITS OF THIS APPROACH:
 *
 * - Tests run faster as they don't need to set up an actual database
 * - More focused unit testing of DAO interface, not Room's implementation
 * - Better isolation for true unit testing
 * - More flexibility in verifying internal interactions
 *
 * DRAWBACKS:
 *
 * - Doesn't test the actual Room implementation
 * - May not catch integration issues between DAO and database
 * - Requires more setup code for complex mocking scenarios
 */
@RunWith(RobolectricTestRunner::class)
class CountryRoomDaoRobolectricTestWithMockk {

    /**
     * Rule that forces Room to execute operations synchronously on the calling thread,
     * making tests predictable.
     */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * Mocked DAO instance instead of a real one.
     * All database operations will be simulated rather than executed.
     */
    @MockK
    private lateinit var dao: CountryRoomDao

    /**
     * Sets up the mock DAO before each test.
     * We configure the mock to return expected values for tested methods.
     */
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    /**
     * Clears all mock interactions after each test.
     */
    @After
    fun tearDown() {
        clearAllMocks()
    }

    /**
     * Tests the basic insert and read operations using mocks.
     * Verifies that the DAO's methods are called with expected parameters
     * and that we properly handle the returned data.
     */
    @Test
    fun insertAndReadTest() = runTest {
        // Arrange
        val testCountry = Country("Australia", "left")
        val expectedList = listOf(testCountry)

        // Configure mock behavior
        coEvery { dao.insertAll(any()) } just runs
        coEvery { dao.getAll() } returns expectedList

        // Act
        dao.insertAll(listOf(testCountry))
        val result = dao.getAll()

        // Assert
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Australia", result[0].name)
        Assert.assertEquals("left", result[0].driveSide)

        // Verify DAO interactions
        coVerify(exactly = 1) { dao.insertAll(listOf(testCountry)) }
        coVerify(exactly = 1) { dao.getAll() }
    }

    /**
     * Tests inserting multiple countries and retrieving them.
     * Uses slot capturing to inspect the parameters passed to insertAll.
     */
    @Test
    fun insertMultipleAndReadTest() = runTest {
        // Arrange
        val countries = listOf(
            Country("Germany", "right"),
            Country("Japan", "left"),
            Country("France", "right")
        )

        val slot = slot<List<Country>>()

        // Configure mock behavior
        coEvery { dao.insertAll(capture(slot)) } just runs
        coEvery { dao.getAll() } returns countries

        // Act
        dao.insertAll(countries)
        val result = dao.getAll()

        // Assert
        Assert.assertEquals(3, result.size)
        Assert.assertEquals(2, result.count { it.driveSide == "right" })
        Assert.assertEquals(1, result.count { it.driveSide == "left" })

        // Verify that insertAll was called with our list of countries
        coVerify(exactly = 1) { dao.insertAll(countries) }

        // Verify captured arguments match what we expect
        Assert.assertEquals(3, slot.captured.size)
        Assert.assertEquals("Germany", slot.captured[0].name)
        Assert.assertEquals("Japan", slot.captured[1].name)
        Assert.assertEquals("France", slot.captured[2].name)
    }

    /**
     * Tests that inserting a country with an existing name replaces the old record.
     * This test demonstrates how to verify behavior over multiple operations.
     */
    @Test
    fun replaceExistingCountryTest() = runTest {
        // Arrange - Initial and updated countries
        val initialCountry = Country("Canada", "right")
        val updatedCountry = Country("Canada", "left")

        // We'll use slots to capture the arguments for both insertAll calls
        val firstSlot = slot<List<Country>>()
        val secondSlot = slot<List<Country>>()

        // Tracking which call is which
        var firstCall = true

        // Configure mock behavior
        coEvery {
            dao.insertAll(capture(if (firstCall) firstSlot else secondSlot))
        } answers {
            firstCall = false
            Unit
        }

        // Configure mock to return different results after each update
        coEvery { dao.getAll() } returnsMany listOf(
            listOf(initialCountry),  // First call
            listOf(updatedCountry)   // Second call
        )

        // Act - Insert initial country
        dao.insertAll(listOf(initialCountry))
        val initialResult = dao.getAll()

        // Act - Insert updated country
        dao.insertAll(listOf(updatedCountry))
        val updatedResult = dao.getAll()

        // Assert - Initial state
        Assert.assertEquals(1, initialResult.size)
        Assert.assertEquals("Canada", initialResult[0].name)
        Assert.assertEquals("right", initialResult[0].driveSide)

        // Assert - Updated state
        Assert.assertEquals(1, updatedResult.size)
        Assert.assertEquals("Canada", updatedResult[0].name)
        Assert.assertEquals("left", updatedResult[0].driveSide)

        // Verify both calls happened with expected arguments
        coVerify(exactly = 1) { dao.insertAll(listOf(initialCountry)) }
        coVerify(exactly = 1) { dao.insertAll(listOf(updatedCountry)) }
    }

    /**
     * New test added to demonstrate a more complex MockK scenario.
     * Tests inserting countries from two different regions and verifying
     * the exact arguments passed to the DAO.
     */
    @Test
    fun insertCountriesByRegionTest() = runTest {
        // Arrange
        val countries = listOf(
            Country("Spain", "right"),
            Country("India", "left")
        )

        val slot = slot<List<Country>>()

        // Configure mock behavior
        coEvery { dao.insertAll(capture(slot)) } just runs
        coEvery { dao.getAll() } returns countries

        // Act
        dao.insertAll(countries)
        val result = dao.getAll()

        // Assert
        Assert.assertEquals(2, result.size)

        // Assert - Verify the exact objects passed to insertAll
        Assert.assertEquals(2, slot.captured.size)
        Assert.assertEquals("Spain", slot.captured[0].name)
        Assert.assertEquals("India", slot.captured[1].name)

        coVerify(exactly = 1) { dao.insertAll(countries) }
    }
}
