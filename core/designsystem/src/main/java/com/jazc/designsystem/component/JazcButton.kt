package com.jazc.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jazc.designsystem.R

enum class JazcButtonVariant { Primary, Secondary, Outlined, Ghost }

enum class JazcButtonSize(val minHeight: Dp, val horizontalPadding: Dp) {
    /** S: altura mínima **48 dp**, padding horizontal **16 dp**, label **14/20 sp**. */
    Sm48(48.dp, 16.dp),
    /** M: altura mínima **56 dp**, padding horizontal **24 dp**, label **14/20 sp**. */
    Md56(56.dp, 24.dp),
    /** L: altura mínima **64 dp**, padding horizontal **32 dp**, label **14/20 sp**. */
    Lg64(64.dp, 32.dp),
}

/** Apariencia del botón. Obtén los valores del tema con defaults.style() y personaliza con copy(). */
data class JazcButtonStyle(
    val colors: ButtonColors,
    val shape: Shape,
    val border: BorderStroke?,
    val elevation: ButtonElevation?,
    val contentPadding: PaddingValues,
    val minHeight: Dp,
    val textStyle: TextStyle,
)

object JazcButtonDefaults {
    /** Primary / M por defecto. Todos los tamaños mantienen un objetivo táctil mínimo de 48 dp. */
    @Composable
    fun style(
        variant: JazcButtonVariant = JazcButtonVariant.Primary,
        size: JazcButtonSize = JazcButtonSize.Md56,
    ): JazcButtonStyle {
        val colors = MaterialTheme.colorScheme
        return JazcButtonStyle(
            colors = when (variant) {
                JazcButtonVariant.Primary -> ButtonDefaults.buttonColors()
                JazcButtonVariant.Secondary -> ButtonDefaults.filledTonalButtonColors()
                JazcButtonVariant.Outlined, JazcButtonVariant.Ghost -> ButtonDefaults.textButtonColors()
            },
            shape = MaterialTheme.shapes.small,
            border = if (variant == JazcButtonVariant.Outlined) BorderStroke(1.dp, colors.outline) else null,
            elevation = null,
            contentPadding = PaddingValues(horizontal = size.horizontalPadding, vertical = 12.dp),
            minHeight = size.minHeight,
            textStyle = MaterialTheme.typography.labelLarge,
        )
    }
}

/**
 * Botón con un slot RowScope: usa texto, iconos o contenido propio sin crear overloads.
 * [loading] bloquea clics, mantiene el ancho del contenido y anuncia el estado a accesibilidad.
 * [modifier] admite onFocusChanged, testTag, tamaño, etc.; [interactionSource] permite observar interacciones.
 * El alto es mínimo, no fijo: el texto puede crecer con la configuración de accesibilidad.
 */
@Composable
fun JazcButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: JazcButtonStyle = JazcButtonDefaults.style(),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val loadingLabel = stringResource(R.string.jazc_loading)
    Button(onClick = onClick,
        modifier = modifier.heightIn(min = style.minHeight).semantics {
            if (loading) stateDescription = loadingLabel
        },
        enabled = enabled && !loading, colors = style.colors, shape = style.shape,
        border = style.border, elevation = style.elevation, contentPadding = style.contentPadding,
        interactionSource = interactionSource,
    ) {
        ProvideTextStyle(style.textStyle) {
            Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                Row(modifier = Modifier.alpha(if (loading) 0f else 1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, content = content)
                if (loading) CircularProgressIndicator(
                    modifier = Modifier.size(20.dp).clearAndSetSemantics {},
                    color = LocalContentColor.current, strokeWidth = 2.dp,
                )
            }
        }
    }
}
