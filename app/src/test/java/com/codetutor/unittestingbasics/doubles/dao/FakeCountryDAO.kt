package com.codetutor.unittestingbasics.doubles.dao

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO

class FakeCountryDAO(
    seed: List<Country> = emptyList()
) : CountryDAO {

    private val backing = seed.toMutableList()

    override suspend fun getAll(): List<Country> = backing

    override suspend fun insertAll(countries: List<Country>) {
        backing.addAll(countries)
    }

    suspend fun insert(country: Country) { backing += country }   // test helper

    fun clear() {
        backing.clear()
    }
}
