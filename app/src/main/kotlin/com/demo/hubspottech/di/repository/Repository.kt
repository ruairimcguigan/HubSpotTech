package com.demo.hubspottech.di.repository

import com.demo.hubspottech.di.api.ApiResponse
import com.demo.hubspottech.di.model.Partner
import io.reactivex.subjects.PublishSubject

interface Repository {

    fun getData(): PublishSubject<ApiResponse>
    fun processData(partners: ArrayList<Partner>): PublishSubject<ApiResponse>
}