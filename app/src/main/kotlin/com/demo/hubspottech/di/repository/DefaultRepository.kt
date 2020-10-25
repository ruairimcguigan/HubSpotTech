package com.demo.hubspottech.di.repository

import com.demo.hubspottech.BuildConfig
import com.demo.hubspottech.di.api.ApiResponse
import com.demo.hubspottech.di.api.ApiResponse.Companion.BAD_GATEWAY
import com.demo.hubspottech.di.api.ApiResponse.Companion.BAD_REQUEST
import com.demo.hubspottech.di.api.ApiResponse.Companion.FORBIDDEN
import com.demo.hubspottech.di.api.ApiResponse.Companion.INTERNAL_ERROR
import com.demo.hubspottech.di.api.ApiResponse.Companion.MOVED
import com.demo.hubspottech.di.api.ApiResponse.Companion.NOT_FOUND
import com.demo.hubspottech.di.api.ApiResponse.Companion.UNAUTHORISED
import com.demo.hubspottech.di.api.ApiResponse.HttpErrors.*
import com.demo.hubspottech.di.api.ApiResponse.Success
import com.demo.hubspottech.di.api.HubSpotService
import com.demo.hubspottech.di.api.PartnersResponse
import com.demo.hubspottech.di.rx.DefaultSchedulerProvider
import com.demo.hubspottech.di.rx.RxDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.PublishSubject
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val service: HubSpotService,
    private val disposable: RxDisposable,
    private val schedulerProvider: DefaultSchedulerProvider,
    private val jsonProcessor: JsonProcessor
) : Repository {

    val apiResponse = PublishSubject.create<ApiResponse>()

    override fun getData(): PublishSubject<ApiResponse> {
        disposable.add(
            service.getPartnersList(BuildConfig.USER_KEY)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doFinally { disposeOnComplete() }
                .subscribeWith(object : DisposableSingleObserver<Response<PartnersResponse>>() {
                    override fun onSuccess(response: Response<PartnersResponse>) {

                        if (response.isSuccessful) {
                            apiResponse.onNext(Success(onSuccessResponse(response)))
                        } else {
                            when (response.code()) {
                                FORBIDDEN -> apiResponse.onNext(Forbidden(errorBody(response)))
                                NOT_FOUND -> apiResponse.onNext(ResourceNotFound(errorBody(response)))
                                UNAUTHORISED -> apiResponse.onNext(Unauthorised(errorBody(response)))
                                INTERNAL_ERROR -> apiResponse.onNext(InternalError(errorBody(response)))
                                BAD_REQUEST -> apiResponse.onNext(BadRequest(errorBody(response)))
                                BAD_GATEWAY -> apiResponse.onNext(BadGateway(errorBody(response)))
                                MOVED -> apiResponse.onNext(ResourceMoved(errorBody(response)))
                            }
                        }
                    }

                    override fun onError(e: Throwable) =
                        Timber.e("on partner request response error: %s", e.localizedMessage)
                })
        )
        return apiResponse
    }

//    override fun postData(
//        signedAuth: String,
//        orderPayLoad: HubSpotRequest
//    ): PublishSubject<ApiResponse> {
//
//        disposable.add(
//            service.submit(
//                authHeader = signedAuth,
//                payLoad = orderPayLoad
//            )
//                .doFinally { disposeOnComplete() }
//                .subscribeOn(schedulerProvider.io())
//                .subscribeWith(object : DisposableSingleObserver<Response<PartnersResponse>>() {
//                    override fun onSuccess(response: Response<PartnersResponse>) {
//
//                        if (response.isSuccessful) {
//                            apiResponse.onNext(Success(onSuccessResponse(response)))
//                        } else {
//                            when (response.code()) {
//                                FORBIDDEN -> apiResponse.onNext(Forbidden(errorBody(response)))
//                                NOT_FOUND -> apiResponse.onNext(ResourceNotFound(errorBody(response)))
//                                UNAUTHORISED -> apiResponse.onNext(Unauthorised(errorBody(response)))
//                                INTERNAL_ERROR -> apiResponse.onNext(InternalError(errorBody(response)))
//                                BAD_REQUEST -> apiResponse.onNext(BadRequest(errorBody(response)))
//                                BAD_GATEWAY -> apiResponse.onNext(BadGateway(errorBody(response)))
//                                MOVED -> apiResponse.onNext(ResourceMoved(errorBody(response)))
//                            }
//                        }
//                    }
//
//                    override fun onError(e: Throwable) =
//                        Timber.e("on payment response error: %s", e.localizedMessage)
//                })
//        )
//
//        return apiResponse
//    }

    private fun onSuccessResponse(response: Response<PartnersResponse>) =
        response.body()!!

    private fun errorBody(response: Response<PartnersResponse>) =
        response.errorBody().toString()

    private fun disposeOnComplete() {
        disposable.dispose()
    }
}