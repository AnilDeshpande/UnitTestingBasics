package com.codetutor.unittestingbasics.doubles.service

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.service.CountryService

class StubCountryService : CountryService {
    override suspend fun fetchAll() = listOf(
        Country("Japan","left"), Country("Canada","right")
    )
}