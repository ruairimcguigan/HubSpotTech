package com.demo.hubspottech.di.model

import java.time.LocalDate

data class Partner(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val country: String = "",
    val dates: ArrayList<LocalDate> = ArrayList()
)