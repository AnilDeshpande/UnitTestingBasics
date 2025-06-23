package com.codetutor.unittestingbasics.database

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO

class InMemoryCountryDao: CountryDAO {

    private val countriesMap = mutableMapOf<String, Country>()

    override suspend fun getAll(): List<Country> {
        return countriesMap.values.toList()
    }

    override suspend fun insertAll(countries: List<Country>) {
        countries.forEach {
            country ->
                countriesMap[country.name] = country
        }
    }
}