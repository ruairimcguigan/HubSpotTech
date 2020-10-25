package com.demo.hubspottech.di.repository

import com.demo.hubspottech.di.api.ApiResponse
import com.demo.hubspottech.di.api.HubSpotRequest
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface Repository {

    fun getData(): PublishSubject<ApiResponse>

//    fun postData(
//        signedAuth: String,
//        orderPayLoad: HubSpotRequest
//    ): PublishSubject<ApiResponse>
}