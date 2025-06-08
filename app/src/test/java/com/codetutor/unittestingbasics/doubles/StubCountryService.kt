package com.codetutor.unittestingbasics.doubles

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryService

class StubCountryService: CountryService {
    override suspend fun fetchAll(): List<Country> {
        return listOf(
            Country("Japan","left"),
            Country("Canada","right")
        )
    }
}