package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.theme.CancelColor
import com.peterlopusan.traveldiary.ui.theme.ConfirmColor
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor

@Composable
fun ConfirmAlertDialog(
    modifier: Modifier = Modifier,
    confirmClick: () -> Unit,
    cancelClick: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {

    Box(
        Modifier
            .background(Color.Transparent)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Dialog(
            onDismissRequest = { cancelClick() }
        ) {
            Column(
                modifier = modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.secondaryBackground)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = dialogTitle,
                    color = MaterialTheme.colors.primaryTextColor,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = dialogText,
                    color = MaterialTheme.colors.secondaryTextColor,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fonts
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    Spacer(modifier = Modifier.weight(1f))

                    CustomButton(
                        clickAction = {
                            cancelClick()
                        },
                        text = stringResource(id = R.string.cancel),
                        textColor = CancelColor,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CustomButton(
                        clickAction = {
                            confirmClick()
                        },
                        text = stringResource(id = R.string.confirm),
                        textColor = ConfirmColor,
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}