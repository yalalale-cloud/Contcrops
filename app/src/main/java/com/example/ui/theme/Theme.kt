package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ContGreen,
    onPrimary = Color.White,
    primaryContainer = ContGreenLight,
    onPrimaryContainer = ContGreenDark,
    secondary = ContBlack,
    onSecondary = Color.White,
    secondaryContainer = ContBorder,
    onSecondaryContainer = ContTextPrimary,
    tertiary = ContGold,
    onTertiary = Color.White,
    tertiaryContainer = ContGoldBg,
    onTertiaryContainer = ContGoldDark,
    background = ContCreamBg,
    onBackground = ContTextPrimary,
    surface = ContCardBg,
    onSurface = ContTextPrimary,
    surfaceVariant = ContBorder,
    onSurfaceVariant = ContTextSecondary,
    outline = ContBorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = ContGreen,
    onPrimary = Color.White,
    primaryContainer = ContGreenDark,
    onPrimaryContainer = ContGreenLight,
    secondary = Color.White,
    onSecondary = ContBlack,
    background = ContBlack,
    onBackground = Color.White,
    surface = Color(0xFF1E1B17),
    onSurface = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
