package com.codetutor.unittestingbasics

import com.codetutor.unittestingbasics.model.Country
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.Assert.*

@RunWith(Parameterized::class)
class CountryReferenceTests(
    private val shouldBeSame: Boolean,
    private val testName: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(true, "Same reference check"),
                arrayOf(false, "Different instances check")
            )
        }
    }

    @Test
    fun testReferenceScenarios() {
        val c1 = Country("India", "left")
        val c2 = if (shouldBeSame) c1 else Country("India", "left")

        if (shouldBeSame) {
            assertSame(c1, c2)
        } else {
            assertNotSame(c1, c2)
        }
    }
}