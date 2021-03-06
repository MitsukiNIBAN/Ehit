package com.mitsuki.ehit.being.network

sealed class RequestResult<T> {
    class SuccessResult<T>(val data: T) : RequestResult<T>()

    class FailResult<T>(val throwable: Throwable) : RequestResult<T>()
}