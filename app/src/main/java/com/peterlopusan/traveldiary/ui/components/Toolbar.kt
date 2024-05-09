package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterlopusan.traveldiary.MainActivity
import com.peterlopusan.traveldiary.R
import com.peterlopusan.traveldiary.ui.TravelDiaryRoutes
import com.peterlopusan.traveldiary.ui.theme.fonts
import com.peterlopusan.traveldiary.ui.theme.primaryBackground
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: String,
    showSettingsButton: Boolean = false,
    showBackButton: Boolean = false,
    addButtonClick: (() -> Unit)? = null,
    filterButtonClick: (() -> Unit)? = null,
    sortButtonClick: (() -> Unit)? = null,
    editButtonClick: (() -> Unit)? = null,
    deleteButtonClick: (() -> Unit)? = null
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryBackground)
            .shadow(elevation = 1.dp)
            .padding(vertical = 15.dp)
            .padding(start = 25.dp)

    ) {
        if (showBackButton) {
            Image(
                painterResource(R.drawable.back_button_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        MainActivity.navController.popBackStack()
                    }
            )
        } else {
            Image(
                painterResource(R.drawable.travel_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
            )
        }

        Spacer(Modifier.width(25.dp))

        Text(
            text = title,
            color = MaterialTheme.colors.primaryTextColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fonts
            )
        )
        Spacer(Modifier.weight(1f))

        if (addButtonClick != null) {
            Image(
                painterResource(R.drawable.add_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        addButtonClick()
                    }
            )
            Spacer(Modifier.width(15.dp))
        }

        if (sortButtonClick != null) {
            Image(
                painterResource(R.drawable.sort_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        sortButtonClick()
                    }
            )
            Spacer(Modifier.width(15.dp))
        }

        if (filterButtonClick != null) {
            Image(
                painter = painterResource(R.drawable.filter_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        filterButtonClick()
                    }
            )
            Spacer(Modifier.width(15.dp))
        }

        if (showSettingsButton) {
            Image(
                painterResource(R.drawable.settings_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        MainActivity.navController.navigate(TravelDiaryRoutes.SettingsScreen.name)
                    }
            )
            Spacer(Modifier.width(15.dp))
        }

        if (editButtonClick != null) {
            Image(
                painterResource(R.drawable.edit_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        editButtonClick()
                    }
            )
            Spacer(Modifier.width(15.dp))
        }

        if (deleteButtonClick != null) {
            Image(
                painterResource(R.drawable.delete_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        deleteButtonClick()
                    }
            )
            Spacer(Modifier.width(15.dp))
        }
    }
}