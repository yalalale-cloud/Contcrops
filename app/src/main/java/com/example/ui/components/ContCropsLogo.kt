package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ContBlack
import com.example.ui.theme.ContGreen

@Composable
fun ContCropsLogo(
    modifier: Modifier = Modifier,
    size: Dp = 34.dp,
    textSize: TextUnit = 18.sp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape((size.value * 0.33f).dp))
                .background(ContBlack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size((size.value * 0.55f).dp)
            )

            // Leaf accent dot
            Box(
                modifier = Modifier
                    .size((size.value * 0.42f).dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
                    .clip(CircleShape)
                    .background(ContGreen)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((size.value * 0.26f).dp)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Cont",
                color = ContBlack,
                fontSize = textSize,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Crops",
                color = ContGreen,
                fontSize = textSize,
                fontWeight = FontWeight.Black
            )
        }
    }
}
