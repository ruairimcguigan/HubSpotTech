package com.demo.hubspottech.di.model

data class Conference(
    val attendeeCount: Int,
    val attendees: ArrayList<String>,
    val name: String,
    val startDate: String
)