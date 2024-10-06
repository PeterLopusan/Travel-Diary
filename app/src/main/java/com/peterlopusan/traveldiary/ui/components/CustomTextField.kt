package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    hint: String,
    text: String = "",
    backgroundColor: Color = LocalTravelDiaryColors.current.primaryBackground,
    borderColor: Color = Color.Transparent,
    icon: Int? = null,
    passwordInput: Boolean = false,
    inputType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    clickAction: (() -> Unit)? = null
) {
    var passwordIsVisible by remember { mutableStateOf(!passwordInput) }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable {
                if (clickAction != null) {
                    clickAction()
                }
            }
            .padding(horizontal = 10.dp)

    ) {
        icon?.let { imageId ->
            Image(
                painterResource(imageId),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        TextField(
            enabled = clickAction == null,
            value = text,
            onValueChange = {
                if (it.length <= 255) {
                    if (inputType == KeyboardType.Email) {
                        onValueChange(it.lowercase())
                    } else if (inputType == KeyboardType.Number) {
                        if (it.isDigitsOnly()) {
                            onValueChange(it)
                        }
                    } else {
                        onValueChange(it)
                    }
                }
            },
            placeholder = {
                Text(
                    text = text,
                    style = TextStyle(fontSize = 22.sp)
                )
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Normal,
                fontFamily = fonts
            ),

            label = {
                Text(
                    text = hint,
                    color = LocalTravelDiaryColors.current.secondaryTextColor,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontFamily = fonts
                    )
                )
            },
            colors = TextFieldDefaults.colors(
                cursorColor = LocalTravelDiaryColors.current.primaryTextColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor
            ),
            visualTransformation = if (passwordIsVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = inputType),
            modifier = Modifier.weight(1f),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        if (passwordInput) {
            val image = if (passwordIsVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            Image(
                imageVector = image,
                contentDescription = null,
                colorFilter = ColorFilter.tint(LocalTravelDiaryColors.current.secondaryTextColor),
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        passwordIsVisible = !passwordIsVisible
                    }
            )
        }
    }
}