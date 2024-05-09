package com.peterlopusan.traveldiary.ui.screens.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.components.CustomButton
import com.peterlopusan.traveldiary.ui.components.CustomTextField
import com.peterlopusan.traveldiary.ui.components.LoadingIndicator
import com.peterlopusan.traveldiary.ui.theme.TravelDiaryTheme
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showLoading by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primaryBackground)
            .padding(35.dp)
    ) {
        Column(
            modifier = Modifier
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.secondaryBackground)
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.travel_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 10.dp),


                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colors.primaryTextColor,
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            CustomTextField(
                hint = stringResource(id = R.string.login_screen_email),
                text = email,
                onValueChange = {
                    email = it
                },
                startIcon = R.drawable.email_icon,
                modifier = Modifier.fillMaxWidth(),
                inputType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                hint = stringResource(id = R.string.login_screen_password),
                text = password,
                onValueChange = {
                    password = it
                },
                startIcon = R.drawable.password_icon,
                passwordInput = true,
                modifier = Modifier.fillMaxWidth(),
                inputType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.login_screen_create_account),
                    color = MaterialTheme.colors.secondaryTextColor,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .clickable {
                            MainActivity.navController.navigate(TravelDiaryRoutes.CreateAccount.name)
                        }
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(id = R.string.login_screen_forgotten_password),
                    color = MaterialTheme.colors.secondaryTextColor,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .clickable {
                            MainActivity.navController.navigate(TravelDiaryRoutes.ForgottenPasswordScreen.name)
                        }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.login_screen_login_button),
                clickAction = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        showLoading = true
                        MainActivity.authViewModel.login(email, password).observe(MainActivity.instance) {
                            if (it) {
                                MainActivity.navController.navigate(TravelDiaryRoutes.MainScreen.name) {
                                    popUpTo(TravelDiaryRoutes.Login.name) { inclusive = true }
                                }
                            }

                            showLoading = false
                        }
                    }
                }
            )
        }

        LoadingIndicator(showLoading = showLoading)
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TravelDiaryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoginScreen()
        }
    }
}