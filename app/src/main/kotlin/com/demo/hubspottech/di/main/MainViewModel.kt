package com.demo.hubspottech.di.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.hubspottech.di.api.ApiResponse
import com.demo.hubspottech.di.api.ApiResponse.Loading
import com.demo.hubspottech.di.api.ApiResponse.HttpErrors.*
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

    private val paymentResult: MutableLiveData<ApiResponse> = MutableLiveData()

    private val _activeNetworkState: MutableLiveData<Boolean> = MutableLiveData()
    val activeNetworkState: LiveData<Boolean>
        get() = _activeNetworkState

    init {
        confirmNetworkState()
    }

    fun getPartnersList(): MutableLiveData<ApiResponse> {

        disposable.add(
            repo.getData()
                .doOnSubscribe { paymentResult.postValue(Loading) }
                .subscribeOn(schedulerProvider.io())
                .subscribeWith(object : DisposableObserver<ApiResponse>() {

                    override fun onNext(response: ApiResponse) {
                        when (response) {
                            is ApiResponse.Success -> onPartnersListReceived(response)
                            is ApiResponse.Error -> handleError(response)

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
        return paymentResult
    }

    private fun onPartnersListReceived(response: ApiResponse)  = paymentResult.postValue(response)

    private fun handleError(response: ApiResponse) = when (response) {
        is Forbidden -> paymentResult.postValue(response)
        is ResourceNotFound -> paymentResult.postValue(response)
        is Unauthorised -> paymentResult.postValue(response)
        is java.lang.InternalError -> paymentResult.postValue(response)
        is BadRequest -> paymentResult.postValue(response)
        is BadGateway -> paymentResult.postValue(response)
        is ResourceMoved -> paymentResult.postValue(response)
        else -> paymentResult.postValue(response)
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