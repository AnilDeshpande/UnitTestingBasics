package com.codetutor.unittestingbasics.doubles

import com.codetutor.unittestingbasics.model.Country
import com.codetutor.unittestingbasics.repository.CountryDAO

class StubCountryDAO: CountryDAO {
    override suspend fun getAll(): List<Country> = listOf(
        Country("India", "left"),
        Country("USA",  "right"),
        Country("UK",  "left")
    )
}