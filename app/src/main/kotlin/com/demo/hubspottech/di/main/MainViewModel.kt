package com.demo.hubspottech.di.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.hubspottech.di.api.ApiResponse
import com.demo.hubspottech.di.api.ApiResponse.*
import com.demo.hubspottech.di.api.ApiResponse.HttpErrors.*
import com.demo.hubspottech.di.api.PartnersResponse
import com.demo.hubspottech.di.network.NetworkState
import com.demo.hubspottech.di.repository.Repository
import com.demo.hubspottech.di.rx.DefaultSchedulerProvider
import com.demo.hubspottech.di.rx.RxDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: Repository,
    private val schedulerProvider: DefaultSchedulerProvider,
    private val disposable: RxDisposable,
    private val networkState: NetworkState
) : ViewModel() {

    private val partnersResult: MutableLiveData<ApiResponse> = MutableLiveData()

    private val _activeNetworkState: MutableLiveData<Boolean> = MutableLiveData()
    val activeNetworkState: LiveData<Boolean>
        get() = _activeNetworkState

    init {
        confirmNetworkState()
    }

    fun getPartnersList(): MutableLiveData<ApiResponse> {

        disposable.add(
            repo.getData()
                .doOnSubscribe { partnersResult.postValue(Loading) }
                .subscribeOn(schedulerProvider.io())
                .subscribeWith(object : DisposableObserver<ApiResponse>() {

                    override fun onNext(response: ApiResponse) {
                        when (response) {
                            is GetSuccess -> onPartnersListReceived(Pair(response, response.data))
                            is Error -> handleError(response)

                            is Unauthorised -> handleError(response)
                            is Forbidden -> handleError(response)
                            is BadRequest -> handleError(response)
                            is InternalError -> handleError(response)
                            is BadGateway -> handleError(response)
                            is ResourceMoved -> handleError(response)
                            is ResourceNotFound -> handleError(response)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Timber.e("Error retrieving response: %s", e.localizedMessage)
                    }

                    override fun onComplete() {
                        disposable.dispose()
                    }
                }
                ))

        return partnersResult
    }

    private fun onPartnersListReceived(response: Pair<ApiResponse, PartnersResponse>) {

        partnersResult.postValue(response.first)
        val partnerList = response.second

        processBookingDetails(partnerList)

    }

    private fun processBookingDetails(partnerList: PartnersResponse) {
        partnerList as PartnersResponse
        repo.processData(partnerList.partners)
    }

    private fun handleError(response: ApiResponse) = when (response) {
        is Forbidden -> partnersResult.postValue(response)
        is ResourceNotFound -> partnersResult.postValue(response)
        is Unauthorised -> partnersResult.postValue(response)
        is java.lang.InternalError -> partnersResult.postValue(response)
        is BadRequest -> partnersResult.postValue(response)
        is BadGateway -> partnersResult.postValue(response)
        is ResourceMoved -> partnersResult.postValue(response)
        else -> partnersResult.postValue(response)
    }

    private fun confirmNetworkState() {
        if (networkState.isAvailable()) {
            _activeNetworkState.value = true
        } else {
            _activeNetworkState.value = false
            Timber.i("No active network detected, check your connection")
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}