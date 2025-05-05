package com.example.factsnumberstask.numbers.data

import com.example.factsnumberstask.numbers.domain.HandleError
import com.example.factsnumberstask.numbers.domain.NoInternetConnectionExeption
import com.example.factsnumberstask.numbers.domain.ServiceUnavailableException
import java.net.UnknownHostException


class HandleDomainError : HandleError<Exception> {

    override fun handle(e: Exception) = when (e) {
        is UnknownHostException -> NoInternetConnectionExeption()
        else -> ServiceUnavailableException()
    }
}