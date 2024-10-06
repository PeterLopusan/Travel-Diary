package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.fonts


@Composable
fun CountrySelector (
    modifier: Modifier = Modifier,
    hint: String = "",
    text: String = "",
    flag: String? = null,
    backgroundColor: Color = LocalTravelDiaryColors.current.primaryBackground,
    onValueChange: (String) -> Unit
) {

    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp)
    ) {
        if (flag == null) {
            Image(
                painterResource(R.drawable.location_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
        } else {
            AsyncImage(
                model = flag,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        TextField(
            enabled = false,
            value = text,
            onValueChange = {
                onValueChange(it)
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
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f)
        )
    }
}