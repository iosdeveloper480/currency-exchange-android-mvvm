package com.waqasali.alfardan.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetpack.textfielddemo.ui.widgets.textField.PassWordTextField
import com.waqasali.alfardan.dashboard.DashboardView
import com.waqasali.alfardan.ui.theme.AlFardanTheme
import com.waqasali.alfardan.ui.widgets.BasicTextField

class LoginView: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlFardanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    NavHost(navigationController, startDestination = "userLogin") {
                        composable(route = "userLogin") {
                            InitLogin(context = applicationContext, onNavigateToDashboard = {

                            }, onNavigateToRegister = {
                                goTo(applicationContext, DashboardView::class.java)
                            })
                        }
                    }
                }
            }
        }
    }

    fun goTo(context: Context, clazz: Class<*>) {
        val i = Intent(context, clazz)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }
}

@Composable
fun InitLogin(
    context: Context,
    onNavigateToRegister: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    when (val state = viewModel.loginResponseState.collectAsState().value) {
        is LoginViewModel.LoginUiState.Loaded -> {
            UserLoginScreen(context,viewModel, onNavigateToRegister = {
                onNavigateToRegister()
            })
        }
        is LoginViewModel.LoginUiState.Success -> {
            viewModel.resetUIState()
            onNavigateToDashboard()
        }
        is LoginViewModel.LoginUiState.Error -> {
            UserLoginScreen(context,viewModel,  onNavigateToRegister = {
                onNavigateToRegister()
            })
            Toast.makeText(context,state.error,Toast.LENGTH_LONG).show()
        }
        else -> {
            UserLoginScreen(context,viewModel,  onNavigateToRegister = {
                onNavigateToRegister()
            })
        }
    }
}

@Composable
fun UserLoginScreen(context: Context, viewModel: LoginViewModel, onNavigateToRegister: () -> Unit) {
    var email  by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    val isInputValid by remember {
        derivedStateOf {
            email.isNotBlank() && password.isNotBlank() && password.length >= 5

        }
    }

//    Surface(
//           color = Color.White, modifier = Modifier.fillMaxSize()
//       ) {
    ConstraintLayout(
        constraintSet = setUserLoginConstraints2(),
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(10.dp)) {
        ConstraintLayout(constraintSet = setUserLoginConstraints(),
            modifier = Modifier
                .layoutId("FieldsLayout")
                .background(Color(0xFF32CC77), shape = RoundedCornerShape(5.dp))
                .padding(10.dp),
        ) {

            val focusManager = LocalFocusManager.current

            BasicTextField(
                text = email, onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .layoutId("userEmail"), label = { Text("Email") },
                placeHolderText = { Text("Enter Email") }, imeAction = ImeAction.Next,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email), keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                })
            )
            PassWordTextField(text = password, onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .layoutId("userPassword"),
                label = { Text("Password") }, placeHolderText = { Text("Enter Password") },
                imeAction = ImeAction.Next, keyboardType = KeyboardType.Password,
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                })
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("loginBtn"),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    viewModel.validateLogin(userEmail = email, userPassword = password)
                },
                enabled = isInputValid,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Green)
            ) {
                Text(text = "Login",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium

                )
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("registerBtn"),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    onNavigateToRegister()
                },
                enabled = true,
            ) {
                Text(text = "Register Now",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium

                )
            }
        }


    }
//     }
}

@Composable
fun setUserLoginConstraints():ConstraintSet {
    return ConstraintSet {
        val loginUserEmailConstraint = createRefFor("userEmail")
        val loginPasswordConstraint = createRefFor("userPassword")
        val loginBtnConstraint = createRefFor("loginBtn")
        val registerBtnConstraint = createRefFor("registerBtn")

        constrain(loginUserEmailConstraint){
            top.linkTo(parent.top, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(loginPasswordConstraint){
            top.linkTo(loginUserEmailConstraint.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(loginBtnConstraint){
            top.linkTo(loginPasswordConstraint.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.value(50.dp)
        }

        constrain(registerBtnConstraint){
            top.linkTo(loginBtnConstraint.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }
}

@Composable
fun setUserLoginConstraints2():ConstraintSet {
    return ConstraintSet {

        val fieldsLayoutConstraint = createRefFor("FieldsLayout")

        constrain(fieldsLayoutConstraint) {
            height = Dimension.wrapContent
            centerHorizontallyTo(parent)
            centerVerticallyTo(parent)
        }
    }
}