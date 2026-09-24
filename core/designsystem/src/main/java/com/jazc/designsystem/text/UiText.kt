package com.jazc.designsystem.text

import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.style.*
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

/**
 * Una sola entrada de texto para todos los componentes. Crear con [uiText].
 * Los recursos se resuelven en composición: no guardan un Context ni congelan el idioma.
 * Para nuevo texto enriquecido se recomienda AnnotatedString; Legacy es un puente limitado a spans comunes.
 */
sealed interface UiText {
    data class Plain(val value: String) : UiText
    data class Resource(@param:StringRes val id: Int, val arguments: List<Any>) : UiText
    data class Rich(val value: AnnotatedString) : UiText
    data class Legacy(val value: SpannedString) : UiText
}

/** String o Spanned/Spannable. Se toma una instantánea: cambios posteriores requieren un nuevo UiText. */
fun uiText(value: CharSequence): UiText = when (value) {
    is AnnotatedString -> UiText.Rich(value)
    is Spanned -> UiText.Legacy(SpannedString(value))
    else -> UiText.Plain(value.toString())
}

/** Texto enriquecido nativo de Compose, sin conversión ni pérdida de anotaciones. */
fun uiText(value: AnnotatedString): UiText = UiText.Rich(value)

/** Recurso localizado; argumentos opcionales como stringResource(id, args). */
fun uiText(@StringRes id: Int, vararg arguments: Any): UiText = UiText.Resource(id, arguments.toList())

/** Resuelve el mismo modelo para JazcText o para Text nativo con toda su API disponible. */
@Composable
fun UiText.asAnnotatedString(): AnnotatedString {
    val resources = LocalResources.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    return when (this) {
        is UiText.Plain -> remember(value) { AnnotatedString(value) }
        is UiText.Rich -> value
        is UiText.Legacy -> remember(value, density.density, density.fontScale) { value.toComposeText(density) }
        is UiText.Resource -> if (arguments.isNotEmpty()) {
            AnnotatedString(stringResource(id, *arguments.toTypedArray()))
        } else remember(id, configuration, resources, density.density, density.fontScale) {
            when (val text = resources.getText(id)) {
                is Spanned -> text.toComposeText(density)
                else -> AnnotatedString(text.toString())
            }
        }
    }
}

/**
 * Puente legacy: bold/italic, subrayado/tachado, colores, tamaños, familias genéricas y URL.
 * ImageSpan, BulletSpan y spans personalizados no se convierten. Usa slots/AnnotatedString en esos casos.
 */
internal fun Spanned.toComposeText(density: Density): AnnotatedString {
    val builder = AnnotatedString.Builder(toString())
    getSpans(0, length, Any::class.java).forEach { span ->
        val start = getSpanStart(span).coerceIn(0, length)
        val end = getSpanEnd(span).coerceIn(start, length)
        if (start == end) return@forEach
        if (span is URLSpan) {
            builder.addLink(LinkAnnotation.Url(span.url), start, end)
            return@forEach
        }
        val style = when (span) {
            is StyleSpan -> SpanStyle(
                fontWeight = if (span.style and Typeface.BOLD != 0) FontWeight.Bold else null,
                fontStyle = if (span.style and Typeface.ITALIC != 0) FontStyle.Italic else null,
            )
            is UnderlineSpan -> SpanStyle(textDecoration = TextDecoration.Underline)
            is StrikethroughSpan -> SpanStyle(textDecoration = TextDecoration.LineThrough)
            is ForegroundColorSpan -> SpanStyle(color = Color(span.foregroundColor))
            is BackgroundColorSpan -> SpanStyle(background = Color(span.backgroundColor))
            is RelativeSizeSpan -> SpanStyle(fontSize = span.sizeChange.em)
            is AbsoluteSizeSpan -> SpanStyle(fontSize = with(density) {
                if (span.dip) span.size.dp.toSp() else span.size.toSp()
            })
            is TypefaceSpan -> SpanStyle(fontFamily = when (span.family) {
                "monospace" -> FontFamily.Monospace
                "serif" -> FontFamily.Serif
                "sans-serif" -> FontFamily.SansSerif
                else -> FontFamily.Default
            })
            else -> null
        }
        style?.let { builder.addStyle(it, start, end) }
    }
    return builder.toAnnotatedString()
}
