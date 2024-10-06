package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
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
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors

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
                    .background(LocalTravelDiaryColors.current.primaryBackground)
            ) {
                DatePicker(
                    state = dateState,
                    colors = DatePickerDefaults.colors(
                        titleContentColor = LocalTravelDiaryColors.current.primaryTextColor,
                        containerColor = LocalTravelDiaryColors.current.secondaryBackground,
                        currentYearContentColor = LocalTravelDiaryColors.current.primaryTextColor,
                        selectedYearContainerColor = ConfirmColor,
                        dayContentColor = LocalTravelDiaryColors.current.primaryTextColor,
                        todayDateBorderColor = ConfirmColor,
                        headlineContentColor = LocalTravelDiaryColors.current.primaryTextColor,
                        selectedDayContainerColor = ConfirmColor,
                        selectedDayContentColor = LocalTravelDiaryColors.current.primaryTextColor,
                        selectedYearContentColor = LocalTravelDiaryColors.current.primaryTextColor,
                        //dayInSelectionRangeContainerColor = Color.Red,
                        //disabledDayContentColor = Color.Red,
                        //dayInSelectionRangeContentColor = Color.Red,
                        //disabledSelectedDayContainerColor =  Color.Red,
                        //subheadContentColor = Color.Red,
                        todayContentColor = LocalTravelDiaryColors.current.primaryTextColor,


                        disabledSelectedDayContentColor = Color.Green,
                        weekdayContentColor = LocalTravelDiaryColors.current.primaryTextColor,
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