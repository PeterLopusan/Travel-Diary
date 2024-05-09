package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryBackground
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor
import com.peterlopusan.traveldiary.utils.openMap


@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    drawableImage: Int? = null,
    urlImage: String? = null,
    title: String = "",
    text: String = "",
    latlng: MutableList<Double>? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.primaryBackground)
            .padding(15.dp)
    ) {
        if (drawableImage != null) {
            Image(
                painter = painterResource(drawableImage),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically)
            )
        } else if (urlImage != null) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colors.secondaryBackground)
                    .align(Alignment.CenterVertically)
            ) {
                AsyncImage(
                    model = urlImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {
            Text(
                text = title,
                color = MaterialTheme.colors.primaryTextColor,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fonts,
                )
            )

            Text(
                text = text,
                color = MaterialTheme.colors.secondaryTextColor,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fonts,
                )
            )
        }

        if (latlng != null) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.navigate_to_map_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        if (latlng.size >= 2) {
                            val latitude = latlng[0]
                            val longitude = latlng[1]
                            openMap(latitude, longitude, 12)
                        }
                    }
            )
        }
    }
}