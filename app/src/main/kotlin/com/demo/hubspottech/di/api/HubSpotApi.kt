package com.demo.hubspottech.di.api

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface HubSpotApi {

    @Headers("Content-Type: application/json")
    @GET("candidateTest/v3/problem/dataset")
    fun getPartners(@Query("userKey")userKey: String): Single<Response<PartnersResponse>>


    @Headers("Content-Type: application/json", "yasht: test")
    @POST("candidateTest/v3/problem/result")
    fun submit(@Query("userKey")userKey: String): Single<Response<SubmissionResponse>>
}