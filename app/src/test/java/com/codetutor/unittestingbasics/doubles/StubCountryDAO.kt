package com.codetutor.unittestingbasics.doubles

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO

class StubCountryDAO: CountryDAO {
    private val countries = mutableListOf(
        Country("India", "left"),
        Country("USA", "right"),
        Country("UK", "left")
    )

    override suspend fun getAll(): List<Country> {
        return countries
    }

    override suspend fun insertAll(countries: List<Country>) {
        this.countries.addAll(countries)
    }

    fun clear() {
        countries.clear()
    }
}