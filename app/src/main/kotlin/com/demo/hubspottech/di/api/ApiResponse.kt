package com.demo.hubspottech.di.api

sealed class ApiResponse {

    data class Success(val data: PartnersResponse) : ApiResponse()
    object Loading : ApiResponse()
    data class Error(val error: String) : ApiResponse()

    sealed class HttpErrors : ApiResponse() {
        data class Forbidden(val exception: String) : HttpErrors()
        data class Unauthorised(val exception: String) : HttpErrors()
        data class BadRequest(val exception: String) : HttpErrors()
        data class InternalError(val exception: String) : HttpErrors()
        data class BadGateway(val exception: String) : HttpErrors()
        data class ResourceMoved(val exception: String) : HttpErrors()
        data class ResourceNotFound(val exception: String) : HttpErrors()
    }

    companion object{
        const val MOVED = 301
        const val BAD_REQUEST = 400
        const val UNAUTHORISED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val INTERNAL_ERROR = 500
        const val BAD_GATEWAY = 502
    }
}