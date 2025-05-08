package com.codetutor.unittestingbasics.doubles.service

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService


class FakeCountryService(seed: List<Country> = emptyList()) : CountryService {
    private val backing = seed.toMutableList()
    override suspend fun fetchAll() = backing
    suspend fun enqueue(country: Country) { backing += country }
}