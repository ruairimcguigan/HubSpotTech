package com.demo.hubspottech.di.main

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import com.demo.hubspottech.R
import com.demo.hubspottech.R.string.check_connection_message
import com.demo.hubspottech.di.api.ApiResponse.*
import com.demo.hubspottech.di.api.PartnersResponse
import com.demo.hubspottech.di.ext.gone
import com.demo.hubspottech.di.ext.snack
import com.demo.hubspottech.di.ext.toast
import com.demo.hubspottech.di.ext.visible
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()

        getPartnersButton.setOnClickListener { getPartners() }
        postDetailsButton.setOnClickListener {  }
    }

    private fun observeViewModel() {

        viewModel.activeNetworkState.observe(
            this,
            Observer { isActive ->
                run {
                    if (!isActive) {
                        showNoConnectionSnack(isActive)
                    }
                }
            })
    }

    private fun getPartners() {
        viewModel.getPartnersList().observe(
            this,
            Observer {
                when (it) {
                    is Loading -> progressBar.visible()
                    is Success -> preparePostDetailsFunction(it.data)
                    is Error -> showError(it.error)

                    is HttpErrors.Forbidden -> showForbiddenNetworkError()
                    is HttpErrors.ResourceNotFound -> showResourceNotFoundError()
                    is HttpErrors.BadRequest -> showBadRequestError()
                    is HttpErrors.Unauthorised -> showUnauthorisedError()
                    is HttpErrors.BadGateway -> showBadGatewayError()
                    is HttpErrors.InternalError -> showInternalError()
                    is HttpErrors.ResourceMoved -> showResourceMovedError()
                }
            })
    }

    private fun preparePostDetailsFunction(data: PartnersResponse) {
        progressBar.gone()
        toast(
            String.format(
                getString(R.string.partners_successfully_retrieved),
                data.partners.size
            )
        )
        postDetailsButton.isEnabled = true
    }

    private fun showError(message: String) {
        progressBar.gone()
        toast(message)
    }

    private fun showBadGatewayError() = setErrorViewState(R.string.bad_gateway_message)
    private fun showInternalError() = setErrorViewState(R.string.internal_error_message)
    private fun showBadRequestError() = setErrorViewState(R.string.bad_request_message)
    private fun showResourceNotFoundError() = setErrorViewState(R.string.not_found_error_message)
    private fun showUnauthorisedError() = setErrorViewState(R.string.unauthorized_error_message)
    private fun showResourceMovedError() = setErrorViewState(R.string.resource_moved_error_message)
    private fun showForbiddenNetworkError() = setErrorViewState(R.string.resource_forbidden_error_message)

    private fun setErrorViewState(@StringRes message: Int) {
        progressBar.gone()
        toast(String.format(getString(message)))
    }

    private fun showNoConnectionSnack(isNetworkAvailable: Boolean) {
        if (!isNetworkAvailable) {
            progressBar.gone()
            getString(check_connection_message).let { root.snack(it) }
        }
    }
}