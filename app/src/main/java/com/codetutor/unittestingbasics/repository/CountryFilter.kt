package com.codetutor.unittestingbasics.repository

import com.codetutor.unittestingbasics.model.Country

object CountryFilter {
    fun filterByDriveSide(countries: List<Country>, side: String): List<Country> {
        require(side == "left" || side == "right") { "Invalid drive side: $side" }
        return countries.filter { it.driveSide == side }
    }
}