package com.waqasaliw.alfardan.users

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId

@Composable fun UserList() {
    ConstraintLayout(SetUserListConstraints(), modifier = Modifier.fillMaxSize()) {
        Text("Text", Modifier.layoutId("infoText"), color = Color.Green)
    }

}

@Composable fun SetUserListConstraints(): ConstraintSet {
    return ConstraintSet {
        val userListInfoTextConstraint = createRefFor("infoText")
        constrain(userListInfoTextConstraint) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }
    }
}

