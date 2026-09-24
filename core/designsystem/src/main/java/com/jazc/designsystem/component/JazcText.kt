package com.jazc.designsystem.component

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.jazc.designsystem.text.UiText
import com.jazc.designsystem.text.asAnnotatedString
import com.jazc.designsystem.theme.JazcTextSize

/** Configuración de layout del texto. Los atributos visuales viven en TextStyle. */
data class JazcTextLayout(
    val maxLines: Int = Int.MAX_VALUE,
    val minLines: Int = 1,
    val overflow: TextOverflow = TextOverflow.Clip,
    val softWrap: Boolean = true,
    val inlineContent: Map<String, InlineTextContent> = emptyMap(),
    val onTextLayout: (TextLayoutResult) -> Unit = {},
)

/**
 * Un único componente para String, recursos y rich text a través de UiText.
 * [size] null hereda el estilo del slot (p. ej. el label de un botón); en una pantalla el tema usa M **16/24 sp**.
 * Selecciona un tamaño explícito como [JazcTextSize.Xs12] (**12/16 sp**) para contenido independiente.
 * [style] se combina al final: permite color, peso, alineación y demás atributos sin duplicar parámetros.
 */
@Composable
fun JazcText(
    text: UiText,
    modifier: Modifier = Modifier,
    size: JazcTextSize? = null,
    style: TextStyle = TextStyle.Default,
    layout: JazcTextLayout = JazcTextLayout(),
) {
    val resolvedStyle = LocalTextStyle.current.merge(size?.style ?: TextStyle.Default).merge(style)
    Text(text = text.asAnnotatedString(), modifier = modifier, style = resolvedStyle,
        maxLines = layout.maxLines, minLines = layout.minLines, overflow = layout.overflow,
        softWrap = layout.softWrap, inlineContent = layout.inlineContent, onTextLayout = layout.onTextLayout)
}

/** Aplica el token sin envolver Text: conserva acceso directo a toda la API nativa. */
@Composable
fun ProvideJazcTextStyle(
    size: JazcTextSize,
    style: TextStyle = TextStyle.Default,
    content: @Composable () -> Unit,
) = ProvideTextStyle(size.style.merge(style), content)
