package com.codetutor.unittestingbasics.doubles

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO

class FakeCountryDao(
    seed : List<Country> = emptyList<Country>()
): CountryDAO {
    private val backing = seed.toMutableList()

    override suspend fun getAll(): List<Country> {
        return backing
    }

    suspend fun insert(country: Country) { backing += country } // test helper
}