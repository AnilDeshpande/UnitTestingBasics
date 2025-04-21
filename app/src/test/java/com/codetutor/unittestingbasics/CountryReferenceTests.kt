package com.codetutor.unittestingbasics

import com.codetutor.unittestingbasics.model.Country
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

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
    fun testReferenceScenarios(){
        println("Running Test:  $testName")
        val c1 = Country("India","left")
        val c2 = if (shouldBeSame) c1 else Country("India", "left")

        if(shouldBeSame){
            Assert.assertSame(c1, c2)
        }else {
            Assert.assertNotSame(c1, c2)
        }
    }
}