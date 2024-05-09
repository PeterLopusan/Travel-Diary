package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.peterlopusan.traveldiary.ui.theme.primaryTextColor

@Composable
fun LoadingIndicator(showLoading: Boolean, backgroundColor: Color = Color.Transparent) {
    if (showLoading) {
        Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primaryTextColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}