package com.peterlopusan.traveldiary.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.Toolbar
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors


@Composable
fun ForgottenPasswordScreen(navController: NavController) {
    val viewModel = MainActivity.authViewModel
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(LocalTravelDiaryColors.current.primaryBackground)
            .fillMaxSize()
    ) {
        Toolbar(
            navController = navController,
            title = stringResource(id = R.string.forgotten_password_screen_toolbar_title),
            showBackButton = true
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalTravelDiaryColors.current.primaryBackground)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
                    .background(LocalTravelDiaryColors.current.secondaryBackground)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 15.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Image(
                    painter = painterResource(id = R.drawable.travel_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    inputType = KeyboardType.Email,
                    hint = stringResource(id = R.string.forgotten_password_screen_email),
                    text = email,
                    onValueChange = {
                        email = it
                    },
                    icon = R.drawable.email_icon,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(25.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.forgotten_password_screen_send_button),
                    clickAction = {
                        viewModel.resetPassword(email)
                    }
                )
            }
        }
    }
}