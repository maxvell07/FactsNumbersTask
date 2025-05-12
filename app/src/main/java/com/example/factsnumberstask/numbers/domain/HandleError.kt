package com.example.factsnumberstask.numbers.domain

import com.example.factsnumberstask.R
import com.example.factsnumberstask.numbers.presentation.ManageResources


interface HandleError<T> {

    fun handle(e: Exception): T

    class Base(private val manageResources: ManageResources) : HandleError<String> {

        override fun handle(e: Exception) = manageResources.string(
            when (e) {
                is NoInternetConnectionExeption -> R.string.no_connection_message
                else -> R.string.service_is_unavailable
            }
        )
    }
}