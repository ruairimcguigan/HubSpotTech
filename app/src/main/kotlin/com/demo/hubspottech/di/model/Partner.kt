package com.demo.hubspottech.di.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Partner(
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("country") val country: String = "",
    @SerializedName("availableDates") var dates: ArrayList<LocalDate> = ArrayList()
)