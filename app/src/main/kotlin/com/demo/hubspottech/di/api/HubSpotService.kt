package com.demo.hubspottech.di.api

import io.reactivex.Single
import retrofit2.Response

class HubSpotService(private val hubSpotApi: HubSpotApi) {

    fun getPartnersList(userkey: String): Single<Response<PartnersResponse>> = hubSpotApi.getPartners(userkey)

//    fun submit(
//        authHeader: String,
//        payLoad: HubSpotRequest): Single<Response<PartnersResponse>> =
//        hubSpotApi.submit(
//            authHeader = authHeader
//        )
}
