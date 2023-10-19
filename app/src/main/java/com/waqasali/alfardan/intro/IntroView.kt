package com.waqasali.alfardan.intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.waqasali.alfardan.ui.theme.AlFardanTheme

import com.waqasali.alfardan.R
import com.waqasali.alfardan.dashboard.DashboardView
import com.waqasali.alfardan.dashboard.InitDashboard
import com.waqasali.alfardan.login.InitLogin
import com.waqasali.alfardan.login.LoginView

class IntroView: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlFardanTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    NavHost(navController = navigationController, startDestination = "IntroView") {
                        composable(route = "IntroView") {
                            InitIntroView(onNavigationToLoginView = {
                                goTo(applicationContext, LoginView::class.java)
                            })
                        }
                    }
                }
            }
        }
    }

    private fun goTo(context: Context, clazz: Class<*>) {
        val i = Intent(context, clazz)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InitIntroView(onNavigationToLoginView: () -> Unit) {
    val list = intArrayOf(R.drawable._1, R.drawable._2, R.drawable._3, R.drawable._4)
    ConstraintLayout(constraintSet = setConstraintSet(), modifier = Modifier.layoutId("PageView")) {
        val pagerState = rememberPagerState(pageCount = {
            list.size
        })
        HorizontalPager(state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White
                )) { row ->
            val isButtonVisible by remember { mutableStateOf(row == 3) }
            ConstraintLayout(constraintSet = setConstraintSet2(), modifier = Modifier
                .layoutId("PageCardView")
                .fillMaxSize()
                .background(Color.White)) {
                Image(modifier = Modifier
                    .fillMaxWidth()
                    .layoutId("PageCardViewImageView"),
                    painter = painterResource(id = list[row]),
                    contentDescription = "$row")
                Text(
                    text = "Page: $row",
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("PageCardViewTitleText"),
                            textAlign = TextAlign.Center
                )
                if (isButtonVisible) {
                    Button(onClick = {
                        onNavigationToLoginView()
                    }, modifier = Modifier
                        .layoutId("PageCardViewButton")
                        .background(Color.White),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.White)) {
                        Text(text = "Skip")
                    }
                }
            }
        }
    }
}

@Composable
fun setConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val pageConstraint = createRefFor("PageView")
        constrain(pageConstraint) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }
}

@Composable
fun setConstraintSet2(): ConstraintSet {
    return ConstraintSet {

        val pageCardViewImageView = createRefFor("PageCardViewImageView")
        val pageCardViewTitleText = createRefFor("PageCardViewTitleText")
        val pageCardViewButton = createRefFor("PageCardViewButton")

        constrain(pageCardViewImageView) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
        }

        constrain(pageCardViewTitleText) {
            bottom.linkTo(parent.bottom, margin = 30.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(pageCardViewButton) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }
    }
}