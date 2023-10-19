package com.waqasali.alfardan.dashboard

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.waqasali.alfardan.ui.theme.AlFardanTheme
import com.waqasali.alfardan.ui.widgets.BasicTextField
import com.waqasali.alfardan.country.BottomSheet
import com.waqasali.alfardan.login.setUserLoginConstraints
import com.waqasali.alfardan.login.setUserLoginConstraints2

class DashboardView: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var sendingAmountTextField  = TextFieldState()
            var receivingAmountTextField = TextFieldState()
            var showSheet by remember { mutableStateOf(false) }
            if (showSheet) {
                BottomSheet(onDismiss = {
                    showSheet = false
                }, onSelectCurrency = { currency ->
                    sendingAmountTextField.text = currency
                })
            }
            AlFardanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    NavHost(navigationController, startDestination = "DashboardView") {
                        composable(route = "DashboardView") {
                            InitDashboard(context = applicationContext,
                                sendingAmountTextField = sendingAmountTextField,
                                receivingAmountTextField = receivingAmountTextField, onNavigateToSelect = {
                                    showSheet = true
                                })
                        }
                    }
                }
            }
        }
    }
}

class TextFieldState() {
    var text: String by mutableStateOf("")
}

@Composable
fun InitDashboard(context: Context,
                  sendingAmountTextField: TextFieldState = remember { TextFieldState() },
                  receivingAmountTextField: TextFieldState = remember { TextFieldState() },
                  onNavigateToSelect: () -> Unit) {

    val firstTextFieldText = remember { mutableStateOf("") }


    ConstraintLayout(
        constraintSet = setUserLoginConstraints2(),
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(10.dp)) {
        ConstraintLayout(
            constraintSet = setUserLoginConstraints(),
            modifier = Modifier
                .layoutId("FieldsLayout")
                .background(Color(0xFF32CC77), shape = RoundedCornerShape(5.dp))
                .padding(10.dp),
        ) {

            val viewModel: DashboardViewModel = viewModel()
            var isDropdownExpanded by remember { mutableStateOf(false) }

            val currencyOptions = listOf("PKR", "IND", "SRI")

            Column {

                val focusManager = LocalFocusManager.current

                Text(modifier = Modifier.layoutId("TitleLabel"), text = "Currency Converter",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.White)
                Text(modifier = Modifier.layoutId("DescriptionLabel"),
                    text = "Use our convenient converter tool to see how much your money is worth in another currency.",
                    fontSize = 15.sp,
                    color = Color.White)
                Text(modifier = Modifier.layoutId("SenderLabel"),
                    text = "AMOUNT YOU WILL SEND",
                    fontSize = 12.sp,
                    color = Color.White)

                BasicTextField(
                    text = viewModel.inputValue,
                    onValueChange = {
                        viewModel.inputValue = it
                    },
                    label = { Text("Enter Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { /* Handle done action if needed */ })
                )

                Box(
                    modifier =  Modifier.fillMaxWidth().clickable {
                        isDropdownExpanded = !isDropdownExpanded
                    }
                ) {
                    BasicTextField(
                        text = " ${viewModel.selectedCurrency} ${viewModel.convertedValue}" ,
                        onValueChange = { viewModel.selectedCurrency = it },
                        label = { Text("Select Currency") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isDropdownExpanded = !isDropdownExpanded
                            }
                        )   ,
                        readOnly = true,
                    )

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        currencyOptions.forEach { currency ->
                            DropdownMenuItem(onClick = {
                                viewModel.selectedCurrency = currency
                                isDropdownExpanded = false
                            }) {
                                Text(text = currency )
                            }
                        }
                    }
                }

                Text("Converted Value: ${viewModel.convertedValue}")
            }
        }
    }
}

@Composable
fun setUserLoginConstraints(): ConstraintSet {
    return ConstraintSet {
        val titleLabel = createRefFor("TitleLabel")
        val DescriptionLabel = createRefFor("DescriptionLabel")
        val SenderLabel = createRefFor("SenderLabel")
        val AEDLabel = createRefFor("AEDLabel")
        val loginUserEmailConstraint = createRefFor("userEmail")
        val ReceiverLabel = createRefFor("ReceiverLabel")
        val loginPasswordConstraint = createRefFor("userPassword")
        val SelectButton = createRefFor("SelectButton")

        constrain(titleLabel) {
            top.linkTo(parent.top, margin = 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(DescriptionLabel) {
            top.linkTo(titleLabel.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(SenderLabel) {
            top.linkTo(DescriptionLabel.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(loginUserEmailConstraint){
            top.linkTo(SenderLabel.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(AEDLabel){
            start.linkTo(loginUserEmailConstraint.end)
            end.linkTo(parent.end, margin = 50.dp)
            centerVerticallyTo(loginUserEmailConstraint)
        }

        constrain(ReceiverLabel){
            top.linkTo(loginUserEmailConstraint.bottom, margin = 15.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(loginPasswordConstraint){
            top.linkTo(ReceiverLabel.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom, margin = 20.dp)
        }

        constrain(SelectButton){
            start.linkTo(loginPasswordConstraint.end)
            end.linkTo(parent.end, margin = 70.dp)
            centerVerticallyTo(loginPasswordConstraint)
        }
    }
}

@Composable
fun setUserLoginConstraints2(): ConstraintSet {
    return ConstraintSet {

        val fieldsLayoutConstraint = createRefFor("FieldsLayout")

        constrain(fieldsLayoutConstraint) {
            height = Dimension.wrapContent
            centerHorizontallyTo(parent)
            centerVerticallyTo(parent)
        }
    }
}