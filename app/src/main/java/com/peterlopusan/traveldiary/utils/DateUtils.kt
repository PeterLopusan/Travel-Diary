package com.peterlopusan.traveldiary.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.runtime.MutableState
import com.peterlopusan.traveldiary.MainActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getDate(): String {
    return android.text.format.DateFormat.getDateFormat(MainActivity.instance).format(System.currentTimeMillis())
}

@SuppressLint("SimpleDateFormat")
fun formatDate(date: String?, format: String): Date {
    return try {
        val formatter = SimpleDateFormat(format)
        formatter.parse(date!!)!!
    } catch (e: Exception) {
        Date()
    }
}

fun showDatePicker(calendar: Calendar = Calendar.getInstance(), function: (String, String, String)-> Unit) {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePicker = DatePickerDialog(MainActivity.instance, { _, pickedYear, pickedMonth, pickedDay ->
        val editedMonth = if (pickedMonth < 9) {
            "0${pickedMonth + 1}"
        } else {
            (pickedMonth + 1).toString()
        }

        val editedDay = if (pickedDay < 10) {
            "0$pickedDay"
        } else {
            pickedDay.toString()
        }
        function(editedDay, editedMonth, pickedYear.toString())
    }, year, month, day)

    datePicker.show()
}

fun getCalendarFromString(date: String): Calendar {
    val calendar = Calendar.getInstance()
    return try {
        val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(date)
        calendar.time = sdfDate!!
        calendar
    } catch (_: Exception) {
        calendar
    }
}

fun checkDates(from: Boolean, dateFrom: MutableState<String>, dateTo: MutableState<String>) {
    if (dateFrom.value.isNotBlank() && dateTo.value.isNotBlank()) {
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val date1 = LocalDate.parse(dateFrom.value, format)
        val date2 = LocalDate.parse(dateTo.value, format)

        if (from) {
            if (date1.isAfter(date2)) {
                dateTo.value = dateFrom.value
            }
        } else {
            if (date1.isAfter(date2)) {
                dateFrom.value = dateTo.value
            }
        }
    }
}
