package com.example.factsnumberstask.numbers.domain

import com.example.factsnumberstask.numbers.presentation.NumberUi

class NumberUiMapper: NumberFact.Mapper<NumberUi> {
    override fun map(id: String, fact: String): NumberUi = NumberUi(id, fact)
}