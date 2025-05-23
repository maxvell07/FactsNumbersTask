package com.example.factsnumberstask.numbers.data

import com.example.factsnumberstask.numbers.domain.NumberFact

data class NumberData(
    private val id: String,
    private val fact: String
) {

    interface Mapper<T> {
        fun map(id: String, fact: String): T

        class Matches(private val id: String) : Mapper<Boolean> {
            override fun map(id: String, fact: String) = this.id == id
        }
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, fact)
}