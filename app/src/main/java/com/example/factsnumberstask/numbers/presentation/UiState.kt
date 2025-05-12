package com.example.factsnumberstask.numbers.presentation

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

sealed class UiState {

    abstract fun apply(inputLayout: TextInputLayout, inputEditText: TextInputEditText)

    class Success : UiState() {
        override fun apply(
            inputLayout: TextInputLayout,
            inputEditText: TextInputEditText
        ) =
            inputEditText.setText("")

    }

    abstract class AbstractError(
        private val message: String,
        private val errorEnabled: Boolean
    ) : UiState() {

        override fun apply(inputLayout: TextInputLayout, inputEditText: TextInputEditText) {
            inputLayout.isErrorEnabled = errorEnabled
            inputLayout.error = message
        }
    }

    data class Error(private val text: String) : AbstractError(text, true)

    class ClearError : AbstractError("", false)
}
