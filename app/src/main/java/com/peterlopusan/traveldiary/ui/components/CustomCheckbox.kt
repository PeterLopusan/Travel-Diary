package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor

@Composable
fun CustomCheckbox(
    modifier: Modifier = Modifier,
    text: String = "",
    isChecked: MutableState<Boolean>,
    onCheckedChange: (Boolean) -> Unit
) {


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = {
                isChecked.value = it
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.primaryTextColor,
                uncheckedColor = MaterialTheme.colors.primaryTextColor,
                checkmarkColor = MaterialTheme.colors.primaryBackground
            )
        )

        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = fonts,
                fontWeight = if (isChecked.value) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = MaterialTheme.colors.primaryTextColor,
            modifier = Modifier.clickable {
                isChecked.value = !isChecked.value
                onCheckedChange(isChecked.value)
            }
        )
    }
}