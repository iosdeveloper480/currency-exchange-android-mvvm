package com.waqasali.alfardan.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class DashboardViewModel: ViewModel() {
    var inputValue: String by mutableStateOf("")
    var selectedCurrency: String by mutableStateOf("PKR")

    val convertedValue: String
        get() {
            val inputValue = inputValue.toFloatOrNull() ?: 0f
            return when (selectedCurrency) {
                "PKR" -> (inputValue * 75).toString()
                "IND" -> (inputValue * 20).toString()
                "SRI" -> (inputValue * 35).toString()
                else -> "Invalid Currency"
            }
        }
}