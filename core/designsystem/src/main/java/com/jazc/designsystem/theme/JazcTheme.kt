package com.jazc.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class JazcAccent { Mint, Blue }

/** Tema compartido por wrappers JAZC y componentes Material nativos. No depende de la app. */
@Composable
fun JazcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accent: JazcAccent = JazcAccent.Mint,
    content: @Composable () -> Unit,
) {
    val mint = accent == JazcAccent.Mint
    val colors = if (darkTheme) darkColorScheme(
        primary = Color(if (mint) 0xFF9CE9C7 else 0xFFB4C8FF),
        onPrimary = Color(if (mint) 0xFF003827 else 0xFF002B73),
        primaryContainer = Color(if (mint) 0xFF12503B else 0xFF25457F),
        onPrimaryContainer = Color(if (mint) 0xFFB8F5D9 else 0xFFD8E2FF),
        secondary = Color(0xFFBACDC3), onSecondary = Color(0xFF25352D),
        secondaryContainer = Color(0xFF354A40), onSecondaryContainer = Color(0xFFD9E9DF),
        tertiary = Color(0xFFE4C28F), onTertiary = Color(0xFF402D0A),
        background = Color(0xFF0C1110), onBackground = Color(0xFFE3EAE5),
        surface = Color(0xFF0C1110), onSurface = Color(0xFFE3EAE5),
        surfaceVariant = Color(0xFF26322C), onSurfaceVariant = Color(0xFFB9C8BE),
        surfaceContainerLowest = Color(0xFF080D0A), surfaceContainerLow = Color(0xFF121B16),
        surfaceContainer = Color(0xFF18221C), surfaceContainerHigh = Color(0xFF222E26),
        surfaceContainerHighest = Color(0xFF2D3931), outline = Color(0xFF87988C),
        outlineVariant = Color(0xFF3E4D43),
    ) else lightColorScheme(
        primary = Color(if (mint) 0xFF006C4C else 0xFF315DA8),
        onPrimary = Color.White,
        primaryContainer = Color(if (mint) 0xFFB8F5D9 else 0xFFD8E2FF),
        onPrimaryContainer = Color(if (mint) 0xFF004D36 else 0xFF123C7B),
        secondary = Color(0xFF4C6457), onSecondary = Color.White,
        secondaryContainer = Color(0xFFCFE9D9), onSecondaryContainer = Color(0xFF304D3D),
        tertiary = Color(0xFF74551F), onTertiary = Color.White,
        background = Color(0xFFF5F9F3), onBackground = Color(0xFF18211A),
        surface = Color(0xFFF5F9F3), onSurface = Color(0xFF18211A),
        surfaceVariant = Color(0xFFDCE7DC), onSurfaceVariant = Color(0xFF425447),
        surfaceContainerLowest = Color.White, surfaceContainerLow = Color(0xFFEFF5ED),
        surfaceContainer = Color(0xFFE8F0E5), surfaceContainerHigh = Color(0xFFE1EADC),
        surfaceContainerHighest = Color(0xFFDBE4D6), outline = Color(0xFF708273),
        outlineVariant = Color(0xFFBFCDBF),
    )
    MaterialTheme(colorScheme = colors, typography = JazcTypography,
        shapes = Shapes(small = RoundedCornerShape(12.dp), medium = RoundedCornerShape(20.dp),
            large = RoundedCornerShape(28.dp)), content = content)
}
