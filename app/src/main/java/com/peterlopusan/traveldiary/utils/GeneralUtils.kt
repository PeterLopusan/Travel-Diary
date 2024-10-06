package com.peterlopusan.traveldiary.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.peterlopusan.traveldiary.MainActivity

fun showToast(text: String?) {
    Toast.makeText(MainActivity.instance, text ?: "", Toast.LENGTH_SHORT).show()
}

fun showLogs(text: Any?) {
    Log.d("LOG_TAG", text.toString())
}

fun formatNumberWithSpaces(number: Long?): String {
    var result = ""

    number?.let {
        val numberString = number.toString()
        val reversed = numberString.reversed()


        for ((index, char) in reversed.withIndex()) {
            result = char + result
            if ((index + 1) % 3 == 0 && index != reversed.length - 1) {
                result = " $result"
            }
        }
    }

    return result
}

fun removeSquareBrackets(list: List<String?>?): String {
    val translatedList = mutableListOf<String>()

    list?.forEach {
        translatedList.add(TranslateApiManager().translateContinent(it ?: ""))
    }

    return if (translatedList.isEmpty()) {
        ""
    } else {
        translatedList.toString().replace("[", "").replace("]", "")
    }
}

fun getStringFromList(list: List<Any?>?): String {
    var stringForReturn = ""
    list?.forEachIndexed { index, item ->
        if (index == 0) {
            stringForReturn = item?.toString() ?: ""
        } else {
            stringForReturn += ", ${item ?: ""}"
        }
    }

    return stringForReturn
}

fun openMap(latitude: Double, longitude: Double, zoomLevel: Int) {
    val intent = Intent(Intent.ACTION_VIEW)

    intent.data = Uri.parse("geo:${latitude},${longitude}?z=$zoomLevel")
    MainActivity.instance.startActivity(intent)
}

fun normalizeAndLowercase(string: String?): String {
    val allChar = mapOf(
        'á' to 'a', 'č' to 'c', 'ď' to 'd', 'é' to 'e', 'í' to 'i',
        'ľ' to 'l', 'ĺ' to 'l', 'ň' to 'n', 'ó' to 'o', 'ô' to 'o',
        'ŕ' to 'r', 'š' to 's', 'ť' to 't', 'ú' to 'u', 'ý' to 'y',
        'ž' to 'z', 'Á' to 'a', 'Č' to 'c', 'Ď' to 'd', 'É' to 'e',
        'Í' to 'i', 'Ľ' to 'l', 'Ĺ' to 'l', 'Ň' to 'n', 'Ó' to 'o',
        'Ô' to 'o', 'Ŕ' to 'r', 'Š' to 's', 'Ť' to 't', 'Ú' to 'u',
        'Ý' to 'y', 'Ž' to 'z'
    )

    val editedString = string?.map { allChar.getOrDefault(it, it) }?.joinToString("")
    return editedString?.lowercase() ?: ""
}

