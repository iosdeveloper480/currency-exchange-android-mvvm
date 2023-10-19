package com.waqasali.alfardan.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginResponseState = MutableStateFlow<LoginUiState>(LoginUiState.Loaded)
    val loginResponseState: StateFlow<LoginUiState> = _loginResponseState

    fun validateLogin(userEmail:String, userPassword:String) {
        viewModelScope.launch {
            _loginResponseState.value = LoginUiState.InProgress
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail)
                    .matches() && userPassword.length >=5
            ) {
                _loginResponseState.value = LoginUiState.Success("Login Succesful")
            } else {
                _loginResponseState.value = LoginUiState.Error("Login Failure")
            }
        }
    }

    fun resetUIState(){
        _loginResponseState.value = LoginUiState.Loaded
    }
    sealed class LoginUiState {
        object Loaded : LoginUiState()
        object InProgress : LoginUiState()
        class Success(val message: String) : LoginUiState()
        class Error(val error: String) : LoginUiState()

    }
}