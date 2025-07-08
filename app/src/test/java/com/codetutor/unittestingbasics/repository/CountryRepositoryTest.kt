package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Test class for CountryRepository using MockK framework.
 *
 * This test class has been refactored from using custom test doubles to using MockK:
 *
 * 1. DEPENDENCY INJECTION:
 *    - Before: Manual creation of test stubs and mocks
 *    - After: MockK annotations (@MockK) for automatic mock creation
 *
 * 2. BEHAVIOR SPECIFICATION:
 *    - Before: Custom stub implementation with hardcoded return values
 *    - After: Declarative behavior using coEvery { } returns syntax
 *
 * 3. VERIFICATION:
 *    - Before: No explicit verification of interactions
 *    - After: Explicit verification using coVerify to ensure correct methods are called
 *
 * 4. CLEANUP:
 *    - Before: No explicit cleanup between tests
 *    - After: clearAllMocks() in tearDown() to prevent test interference
 *
 * Benefits of using MockK:
 * - More concise and readable test code
 * - Better separation of test setup, execution, and verification
 * - More powerful mocking capabilities for Kotlin's suspending functions
 * - Explicit verification of interactions between components
 */
class CountryRepositoryTest {

    // Using MockK annotations to create mocks
    @MockK
    private lateinit var countryDAO: CountryDAO

    @MockK
    private lateinit var countryService: CountryService

    // The class under test
    private lateinit var repository: CountryRepository

    // Test data
    private val indiaCountry = Country("India", "left")
    private val usaCountry = Country("USA", "right")
    private val ukCountry = Country("UK", "left")
    private val allCountries = listOf(indiaCountry, usaCountry, ukCountry)
    private val leftCountries = listOf(indiaCountry, ukCountry)
    private val rightCountries = listOf(usaCountry)

    @Before
    fun setup() {
        // Initialize the mocks
        MockKAnnotations.init(this)

        // Configure mock behavior for countryDAO
        // Instead of custom stub class, we use MockK's coEvery {} to define behavior
        coEvery { countryDAO.getAll() } returns allCountries

        // Configure mock for countryService (not used in these tests but needed for initialization)
        coEvery { countryService.fetchAll() } returns emptyList()

        // Create the repository with mocked dependencies
        repository = CountryRepository(countryDAO, countryService)
    }

    @After
    fun tearDown() {
        // Clear all recorded calls after each test
        // This is important to avoid test interference and is a best practice with MockK
        clearAllMocks()
    }

    /**
     * Tests the basic functionality of getAll() method to verify it correctly
     * returns the full list of countries from the DAO.
     *
     * Refactoring changes:
     * - Added explicit verification with coVerify
     * - Split test into Action and Assertions sections for better readability
     */
    @Test
    fun testGetAll() = runBlocking {
        // Action
        val countries = repository.getAll()

        // Assertions
        assertEquals(3, countries.size)
        assertEquals("India", countries[0].name)
        assertEquals("USA", countries[1].name)
        assertEquals("UK", countries[2].name)

        // Verify the DAO was called but the service was not
        // This is a key benefit of MockK - explicit verification of interactions
        coVerify(exactly = 1) { countryDAO.getAll() }
        coVerify(exactly = 0) { countryService.fetchAll() }
    }

    /**
     * Tests that countriesBySide() correctly filters and returns only countries
     * that drive on the left side of the road.
     *
     * Refactoring changes:
     * - Added explicit verification with coVerify
     * - Improved test structure with clear Action and Assertions sections
     */
    @Test
    fun testGetAllLeft() = runBlocking {
        // Action
        val countries = repository.countriesBySide("left")

        // Assertions
        assertEquals(2, countries.size)
        assertEquals("India", countries[0].name)
        assertEquals("UK", countries[1].name)

        // Verify the DAO was called
        coVerify(exactly = 1) { countryDAO.getAll() }
    }

    /**
     * Tests that countriesBySide() correctly filters and returns only countries
     * that drive on the right side of the road.
     *
     * Refactoring changes:
     * - Added explicit verification with coVerify
     * - Improved test structure with clear Action and Assertions sections
     */
    @Test
    fun testGetAllRight() = runBlocking {
        // Action
        val countries = repository.countriesBySide("right")

        // Assertions
        assertEquals(1, countries.size)
        assertEquals("USA", countries[0].name)

        // Verify the DAO was called
        coVerify(exactly = 1) { countryDAO.getAll() }
    }

    /**
     * Tests the error handling in countriesBySide() when an invalid drive side
     * is provided. Should return an empty list.
     *
     * Refactoring changes:
     * - Added explicit verification with coVerify
     * - Improved test structure with clear Action and Assertions sections
     */
    @Test
    fun testGetAllNonMatching() = runBlocking {
        // Action
        val countries = repository.countriesBySide("non-matching")

        // Assertions
        assertTrue(countries.isEmpty())

        // Verify the DAO was called
        coVerify(exactly = 1) { countryDAO.getAll() }
    }

    /**
     * Tests filtering countries with "left" drive side to ensure the
     * correct subset is returned. Validates filter behavior and data integrity.
     *
     * Refactoring changes:
     * - Renamed test to be more descriptive
     * - Added explicit verification with coVerify
     * - Improved test structure with clear Action and Assertions sections
     */
    @Test
    fun testGetAllLeftCountries() = runBlocking {
        // Action
        val countries = repository.countriesBySide("left")

        // Assertions
        assertEquals(2, countries.size)
        assertEquals("India", countries[0].name)
        assertEquals("UK", countries[1].name)

        // Verify the DAO was called
        coVerify(exactly = 1) { countryDAO.getAll() }
    }
}