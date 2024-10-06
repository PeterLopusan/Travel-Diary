package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts

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
                selectedColor = LocalTravelDiaryColors.current.primaryTextColor,
                unselectedColor  = LocalTravelDiaryColors.current.primaryTextColor
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
            color = LocalTravelDiaryColors.current.primaryTextColor,
            modifier = Modifier.clickable {
                isSelect.value = !isSelect.value
                clickAction()
            }
        )
    }
}