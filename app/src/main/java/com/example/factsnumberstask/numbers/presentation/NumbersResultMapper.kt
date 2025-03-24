package com.example.factsnumberstask.numbers.presentation

import com.example.factsnumberstask.numbers.domain.NumberFact
import com.example.factsnumberstask.numbers.domain.NumbersResult

class NumbersResultMapper(
    private val communications: NumbersCommunications,
    private val mapper: NumberFact.Mapper<NumberUi>
) : NumbersResult.Mapper<Unit> {

    override fun map(list: List<NumberFact>, errorMessage: String) =
        communications.showState(
            if (errorMessage.isEmpty()) {
                if (list.isNotEmpty()) {
                    val numbersList = list.map { it.map(mapper) }
                    communications.showList(numbersList)
                }
                UiState.Success()
            } else UiState.Error(errorMessage)
        )


}