package com.codetutor.unittestingbasics.doubles.dao

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO

class StubCountryDAO : CountryDAO {
    private val countries = mutableListOf(
        Country("India", "left"),
        Country("USA", "right"),
        Country("UK", "left")
    )

    override suspend fun getAll(): List<Country> = countries

    override suspend fun insertAll(countries: List<Country>) {
        this.countries.addAll(countries)
    }

    fun clear() {
        countries.clear()
    }
}