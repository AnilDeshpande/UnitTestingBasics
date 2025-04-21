package com.codetutor.unittestingbasics.doubles

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO

class FakeCountryDAO(
    seed: List<Country> = emptyList()
) : CountryDAO {

    private val backing = seed.toMutableList()

    override suspend fun getAll(): List<Country> = backing

    suspend fun insert(country: Country) { backing += country }   // test helper
}
