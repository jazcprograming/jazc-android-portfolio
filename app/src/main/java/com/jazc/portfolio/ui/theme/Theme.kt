package com.jazc.portfolio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.jazc.designsystem.theme.JazcTheme

/** Punto de entrada compatible con la plantilla; la definición del tema vive en :core:designsystem. */
@Composable
fun PortfolioJAZCTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    JazcTheme(darkTheme = darkTheme, content = content)
}
