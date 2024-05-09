package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor

@Composable
fun CustomRadioButton(
    modifier: Modifier = Modifier,
    text: String = "",
    isSelect: MutableState<Boolean>,
    clickAction: () -> Unit
) {

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = isSelect.value,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primaryTextColor,
                unselectedColor  = MaterialTheme.colors.primaryTextColor
            ),
            onClick = {
                if (!isSelect.value) {
                    isSelect.value = true
                    clickAction()
                }
            }
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = fonts,
                fontWeight = if (isSelect.value) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = MaterialTheme.colors.primaryTextColor,
            modifier = Modifier.clickable {
                isSelect.value = !isSelect.value
                clickAction()
            }
        )
    }
}