package com.demo.hubspottech.di.main

import com.demo.hubspottech.di.model.Conference
import com.demo.hubspottech.di.model.Partner
import java.time.LocalDate

class BookingProcessor {

    private val conferences: ArrayList<Conference?> = ArrayList()

    fun processBooking(partners: ArrayList<Partner>): ArrayList<Conference?> {

        val countriesMap: HashMap<String, HashSet<Partner>> = HashMap()
        val holdData1: ArrayList<LocalDate> = ArrayList()
        var bestSuitedConferences: ArrayList<Conference?> = ArrayList()

        for (partner in partners){
            val country = partner.country
            val availDates = partner.dates

            for (j in 0 until availDates.size) {
                val n =
                    LocalDate.parse(availDates[j].toString())
                holdData1.add(n)
            }

            if (countriesMap.containsKey(country)) {
                countriesMap[country]?.add(
                    partner.copy(
                        firstName = partner.firstName,
                        lastName = partner.lastName,
                        email = partner.country,
                        country = partner.email,
                        dates = holdData1
                    )
                )
            } else {
                val partnerCountry = HashSet<Partner>()
                partnerCountry.add(
                    partner.copy(
                        firstName = partner.firstName,
                        lastName = partner.lastName,
                        email = partner.country,
                        country = partner.email,
                        dates = holdData1
                    )
                )
                countriesMap[country] = partnerCountry
            }

            bestSuitedConferences = determineBestDates(countriesMap, partner)
        }
        return bestSuitedConferences
    }

    private fun determineBestDates(
        countriesMap: HashMap<String, HashSet<Partner>>,
        partner: Partner
    ): ArrayList<Conference?> {
        var maxAttendees = 0
        var best: LocalDate? = null

        for (country in countriesMap.keys) {

            val countryList: HashMap<LocalDate, ArrayList<String>> = HashMap()

            countriesMap[country]?.forEach { ptr ->
                val availableDates: ArrayList<LocalDate> = getPossibleDates(partner)
                for (date in availableDates) {
                    if (countryList.containsKey(date)) {
                        countryList[date]?.add(ptr.email)
                    } else {
                        val emails: ArrayList<String> = ArrayList()
                        emails.add(ptr.email)
                        countryList[date] = emails
                    }
                }
            }


            for (countryKey in countryList.keys) {
                if (countryList[countryKey]!!.size > maxAttendees) {
                    maxAttendees = countryList[countryKey]!!.size
                    best = countryKey
                } else if (countryList[countryKey]?.size == maxAttendees) {
                    if (countryKey.isBefore(best)) {
                        best = countryKey
                    }
                }
            }
            if (best != null) {

                countryList[best]?.let {
                    val conference = Conference(
                        attendeeCount = maxAttendees,
                        attendees = it,
                        name = country,
                        startDate = best.toString()
                    )
                    conferences.add(conference)
                }
            } else {
                conferences.add(null)
            }
        }
        return conferences
    }

    private fun getPossibleDates(partner: Partner): ArrayList<LocalDate> {
        val dates: ArrayList<LocalDate> = ArrayList()
        for (dateOne in partner.dates) {
            for (dateTwo in partner.dates) {
                if (dateOne.plusDays(1) == dateTwo) {
                    dates.add(dateOne)
                }
            }
        }
        return dates
    }
}