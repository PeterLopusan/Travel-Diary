package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor
import com.peterlopusan.traveldiary.ui.theme.secondaryTextColor
import com.peterlopusan.traveldiary.utils.showLogs

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    activePage: Int,
    itemClick: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryBackground)
            .shadow(elevation = 1.dp)
            .padding(horizontal = 10.dp)
            .padding(top = 5.dp)

    ) {
        BottomNavigationBarItem(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    itemClick(0)
                },
            isActive = activePage == 0,
            iconId = R.drawable.flights_icon,
            textId = R.string.bottom_navigation_bar_flights
        )

        BottomNavigationBarItem(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    itemClick(1)
                },
            isActive = activePage == 1,
            iconId = R.drawable.places_icon,
            textId = R.string.bottom_navigation_bar_places
        )

        BottomNavigationBarItem(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    itemClick(2)
                },
            isActive = activePage == 2,
            iconId = R.drawable.countries_icon,
            textId = R.string.bottom_navigation_bar_countries
        )

        BottomNavigationBarItem(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    itemClick(3)
                },
            isActive = activePage == 3,
            iconId = R.drawable.map_icon,
            textId = R.string.bottom_navigation_bar_map
        )
    }
}

@Composable
fun BottomNavigationBarItem(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    iconId: Int,
    textId: Int
) {
    Column(
        modifier = modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(id = textId),
            color = MaterialTheme.colors.primaryTextColor,
            style = TextStyle(
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                fontFamily = fonts,
            )
        )

        Spacer(modifier = Modifier.height(3.dp))

        Divider(
            color = if (isActive) MaterialTheme.colors.primaryTextColor else Color.Transparent,
            modifier = Modifier
                .height(1.dp)
                //.padding(horizontal = 5.dp)
        )
    }
}