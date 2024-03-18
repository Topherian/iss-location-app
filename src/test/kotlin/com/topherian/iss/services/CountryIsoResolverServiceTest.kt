package com.topherian.iss.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.topherian.iss.external.feign.CountryIsoClient
import com.topherian.iss.external.models.worldbank.WorldBankResults
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.rmi.UnexpectedException

@DisplayName("Given a CountryIsoResolverService")
class CountryIsoResolverServiceTest {

    private val mapper = jacksonObjectMapper()

    @Test
    fun `when a valid ISO country code is passed in, then a valid country name is returned`() {

        val countryIsoClient = mockk<CountryIsoClient>()
        // read an actual object given the complexity of the domain object
        val worldBankResults = mapper.readValue(getValidResults(), WorldBankResults::class.java)

        every { countryIsoClient.getCountryCode(any()) } returns worldBankResults
        val countryIsoService = CountryIsoResolverService(countryIsoClient = countryIsoClient)
        val countryName = countryIsoService.resolveCountryIso("PH")

        verify(exactly = 1) { countryIsoClient.getCountryCode(any()) }
        countryName shouldBeEqual "Philippines"
    }

    @Test
    fun `when a valid ISO country code is passed in, and an invalid payload is returned then an exception is thrown`() {

        val countryIsoClient = mockk<CountryIsoClient>()
        // read an actual object given the complexity of the domain object
        val worldBankResults = mapper.readValue(getInvalidResults1(), WorldBankResults::class.java)

        every { countryIsoClient.getCountryCode(any()) } returns worldBankResults
        val countryIsoService = CountryIsoResolverService(countryIsoClient = countryIsoClient)

        val exception = shouldThrow<UnexpectedException> {
            countryIsoService.resolveCountryIso("PH")
        }
        verify(exactly = 1) { countryIsoClient.getCountryCode(any()) }
        exception.message shouldContain "Payload is not valid or unrecognized"
    }

    @Test
    fun `when a valid ISO country code is passed in, and an invalid payload without an array element is returned then an exception is thrown`() {

        val countryIsoClient = mockk<CountryIsoClient>()
        // read an actual object given the complexity of the domain object
        val worldBankResults = mapper.readValue(getInvalidResults2(), WorldBankResults::class.java)

        every { countryIsoClient.getCountryCode(any()) } returns worldBankResults
        val countryIsoService = CountryIsoResolverService(countryIsoClient = countryIsoClient)
        val countryName = countryIsoService.resolveCountryIso("PH")

        verify(exactly = 1) { countryIsoClient.getCountryCode(any()) }
        countryName shouldBeEqual "UNKNOWN"

    }

    @Test
    fun `when a valid ISO country code is passed in, and an invalid payload without a country name section is returned then an UNKNOWN string is returned`() {

        val countryIsoClient = mockk<CountryIsoClient>()
        // read an actual object given the complexity of the domain object
        val worldBankResults = mapper.readValue(getValidResultsWithoutCountryName(), WorldBankResults::class.java)

        every { countryIsoClient.getCountryCode(any()) } returns worldBankResults
        val countryIsoService = CountryIsoResolverService(countryIsoClient = countryIsoClient)
        val countryName = countryIsoService.resolveCountryIso("PH")

        verify(exactly = 1) { countryIsoClient.getCountryCode(any()) }
        countryName shouldBeEqual "UNKNOWN"
    }

    fun getValidResults() = """[
        {
            "page": 1,
            "pages": 1,
            "per_page": "50",
            "total": 1
        },
        [
            {
                "id": "PHL",
                "iso2Code": "PH",
                "name": "Philippines",
                "region": {
                "id": "EAS",
                "iso2code": "Z4",
                "value": "East Asia & Pacific"
            },
                "adminregion": {
                "id": "EAP",
                "iso2code": "4E",
                "value": "East Asia & Pacific (excluding high income)"
            },
                "incomeLevel": {
                "id": "LMC",
                "iso2code": "XN",
                "value": "Lower middle income"
            },
                "lendingType": {
                "id": "IBD",
                "iso2code": "XF",
                "value": "IBRD"
            },
                "capitalCity": "Manila",
                "longitude": "121.035",
                "latitude": "14.5515"
            }
        ]
    ]""".trim()

    fun getInvalidResults1() = """[
        {
            "page": 1,
            "pages": 1,
            "per_page": "50",
            "total": 0
        }
    ]""".trim()

    fun getInvalidResults2() = """[
        {
            "page": 1,
            "pages": 1,
            "per_page": "50",
            "total": 1
        },
        []
    ]""".trim()

    fun getValidResultsWithoutCountryName() = """[
        {
            "page": 1,
            "pages": 1,
            "per_page": "50",
            "total": 1
        },
        [
            {
                "id": "PHL",
                "iso2Code": "PH",
                "region": {
                "id": "EAS",
                "iso2code": "Z4",
                "value": "East Asia & Pacific"
            },
                "adminregion": {
                "id": "EAP",
                "iso2code": "4E",
                "value": "East Asia & Pacific (excluding high income)"
            },
                "incomeLevel": {
                "id": "LMC",
                "iso2code": "XN",
                "value": "Lower middle income"
            },
                "lendingType": {
                "id": "IBD",
                "iso2code": "XF",
                "value": "IBRD"
            },
                "capitalCity": "Manila",
                "longitude": "121.035",
                "latitude": "14.5515"
            }
        ]
    ]""".trim()

}