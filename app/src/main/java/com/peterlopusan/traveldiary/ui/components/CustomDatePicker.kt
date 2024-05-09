package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.theme.CancelColor
import com.peterlopusan.traveldiary.ui.theme.ConfirmColor
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor
import com.peterlopusan.traveldiary.utils.showLogs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    modifier: Modifier = Modifier,
    closePicker: (Long?) -> Unit,
) {
    val dateState = rememberDatePickerState()

    Box(
        Modifier
            .background(Color.Transparent)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Dialog(
            onDismissRequest = { closePicker(null) }
        ) {
            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.primaryBackground)
            ) {
                DatePicker(
                    state = dateState,
                    colors = DatePickerDefaults.colors(
                        titleContentColor = MaterialTheme.colors.primaryTextColor,
                        containerColor = MaterialTheme.colors.secondaryBackground,
                        currentYearContentColor = MaterialTheme.colors.primaryTextColor,
                        selectedYearContainerColor = ConfirmColor,
                        dayContentColor = MaterialTheme.colors.primaryTextColor,
                        todayDateBorderColor = ConfirmColor,
                        headlineContentColor = MaterialTheme.colors.primaryTextColor,
                        selectedDayContainerColor = ConfirmColor,
                        selectedDayContentColor = MaterialTheme.colors.primaryTextColor,
                        selectedYearContentColor = MaterialTheme.colors.primaryTextColor,
                        //dayInSelectionRangeContainerColor = Color.Red,
                        //disabledDayContentColor = Color.Red,
                        //dayInSelectionRangeContentColor = Color.Red,
                        //disabledSelectedDayContainerColor =  Color.Red,
                        //subheadContentColor = Color.Red,
                        todayContentColor = MaterialTheme.colors.primaryTextColor,


                        disabledSelectedDayContentColor = Color.Green,
                        weekdayContentColor = MaterialTheme.colors.primaryTextColor,
                        //yearContentColor = Color.Red,
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {

                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = stringResource(id = R.string.cancel),
                        textColor = CancelColor,
                        clickAction = {
                            closePicker(null)
                        }
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = stringResource(id = R.string.confirm),
                        textColor = ConfirmColor,
                        clickAction = {
                            closePicker(dateState.selectedDateMillis)
                        }
                    )
                }
            }
        }
    }
}