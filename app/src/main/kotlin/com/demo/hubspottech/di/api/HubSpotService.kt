package com.demo.hubspottech.di.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body

class HubSpotService(private val hubSpotApi: HubSpotApi) {

    fun getPartnersList(userKey: String)
            : Single<Response<PartnersResponse>> = hubSpotApi.getPartners(userKey)

    fun postDetails(
        userKey: String,
        @Body payLoad: String
    ): Single<Response<SubmissionResponse>> = hubSpotApi.submit(userKey)
}
