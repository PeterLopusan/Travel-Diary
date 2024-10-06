package com.peterlopusan.traveldiary.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterlopusan.traveldiary.ui.theme.LocalTravelDiaryColors
import com.peterlopusan.traveldiary.ui.theme.TravelDiaryTheme
import com.peterlopusan.traveldiary.ui.theme.fonts

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String = "",
    buttonColor: Color = LocalTravelDiaryColors.current.primaryBackground,
    textColor: Color = LocalTravelDiaryColors.current.primaryTextColor,
    iconId: Int? = null,
    clickAction: () -> Unit
) {

    Button(
        onClick = { clickAction() },
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .border(1.dp, LocalTravelDiaryColors.current.secondaryTextColor, shape = RoundedCornerShape(12.dp))
    ) {
        iconId?.let {
            Image(
                painterResource(iconId),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }

        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(5.dp),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fonts,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview() {
    TravelDiaryTheme {
        CustomButton(
            text = "Button Text",
            clickAction = {}
        )
    }
}