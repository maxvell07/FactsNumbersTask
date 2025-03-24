package com.example.factsnumberstask.numbers.presentation

interface Mapper<R, S> {

    fun map(Source:S):R

    interface Unit<S>:Mapper<kotlin.Unit,S>

}