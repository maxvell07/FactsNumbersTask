package com.example.factsnumberstask.numbers.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

abstract class BaseTest {

    protected class TestNumbersCommunications : NumbersCommunications {

        val progressCalledList = mutableListOf<Int>()
        val stateCalledList = mutableListOf<UiState>()
        var timesShowList = 0
        val numbersList = mutableListOf<NumberUi>()

        override fun showProgress(show: Int) {
            progressCalledList.add(show)
        }

        override fun showState(uiState: UiState) {
            stateCalledList.add(uiState)
        }

        override fun showList(list: List<NumberUi>) {
            timesShowList++
            numbersList.addAll(list)
        }

        override fun observeProgress(owner: LifecycleOwner, observer: Observer<Int>) = Unit
        override fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) = Unit
        override fun observeList(owner: LifecycleOwner, observer: Observer<List<NumberUi>>) = Unit
    }
}