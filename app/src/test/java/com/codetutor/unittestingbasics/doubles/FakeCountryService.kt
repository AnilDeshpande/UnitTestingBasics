package com.codetutor.unittestingbasics.doubles

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryService

class FakeCountryService(seed: List<Country> = emptyList<Country>()): CountryService {
    private val backing  = seed.toMutableList()
    override suspend fun fetchAll(): List<Country> {
        return backing
    }
    suspend fun enque(country: Country) {backing += country}
}