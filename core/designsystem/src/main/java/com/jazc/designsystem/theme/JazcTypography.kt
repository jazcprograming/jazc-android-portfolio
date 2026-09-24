package com.jazc.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Escala tipográfica: el número del nombre es el tamaño en **sp**, nunca dp.
 * Los sp respetan el tamaño de fuente del dispositivo. Consulta cada entrada en Quick Documentation.
 * Usa [style] con Text nativo o con JazcText; copy() permite ajustes locales explícitos.
 */
enum class JazcTextSize(val fontSize: TextUnit, val lineHeight: TextUnit, val weight: FontWeight) {
    /** XS: **12 sp**, interlineado **16 sp**, Regular (400). Metadatos y notas breves. */
    Xs12(12.sp, 16.sp, FontWeight.Normal),
    /** S: **14 sp**, interlineado **20 sp**, Regular (400). Texto secundario y descripciones. */
    Sm14(14.sp, 20.sp, FontWeight.Normal),
    /** M: **16 sp**, interlineado **24 sp**, Regular (400). Texto principal por defecto. */
    Md16(16.sp, 24.sp, FontWeight.Normal),
    /** L: **20 sp**, interlineado **28 sp**, SemiBold (600). Títulos de secciones pequeñas. */
    Lg20(20.sp, 28.sp, FontWeight.SemiBold),
    /** XL: **24 sp**, interlineado **32 sp**, SemiBold (600). Títulos de secciones. */
    Xl24(24.sp, 32.sp, FontWeight.SemiBold),
    /** XXL: **32 sp**, interlineado **40 sp**, SemiBold (600). Títulos de pantalla. */
    Xxl32(32.sp, 40.sp, FontWeight.SemiBold),
    /** Display: **40 sp**, interlineado **48 sp**, SemiBold (600). Encabezados destacados. */
    Display40(40.sp, 48.sp, FontWeight.SemiBold);

    val style: TextStyle
        get() = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = fontSize,
            lineHeight = lineHeight, fontWeight = weight)
}

internal val JazcTypography = Typography(
    displayLarge = JazcTextSize.Display40.style,
    displayMedium = JazcTextSize.Display40.style,
    displaySmall = JazcTextSize.Xxl32.style,
    headlineLarge = JazcTextSize.Xxl32.style,
    headlineMedium = JazcTextSize.Xl24.style,
    headlineSmall = JazcTextSize.Lg20.style,
    titleLarge = JazcTextSize.Lg20.style,
    titleMedium = JazcTextSize.Md16.style.copy(fontWeight = FontWeight.SemiBold),
    titleSmall = JazcTextSize.Sm14.style.copy(fontWeight = FontWeight.SemiBold),
    bodyLarge = JazcTextSize.Md16.style,
    bodyMedium = JazcTextSize.Sm14.style,
    bodySmall = JazcTextSize.Xs12.style,
    labelLarge = JazcTextSize.Sm14.style.copy(fontWeight = FontWeight.SemiBold),
    labelMedium = JazcTextSize.Xs12.style.copy(fontWeight = FontWeight.SemiBold),
    labelSmall = JazcTextSize.Xs12.style.copy(fontWeight = FontWeight.Medium),
)
