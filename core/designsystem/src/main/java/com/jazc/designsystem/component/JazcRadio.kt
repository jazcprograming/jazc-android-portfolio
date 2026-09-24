package com.jazc.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

enum class JazcRadioVariant { Plain, Card }
enum class JazcRadioLayout { Vertical, Flow }

/** Estilo de la fila seleccionable; el indicador conserva colores Material y estados disabled. */
data class JazcRadioStyle(
    val indicatorColors: RadioButtonColors,
    val selectedContainer: Color,
    val container: Color,
    val selectedBorder: BorderStroke?,
    val border: BorderStroke?,
    val shape: Shape,
    val contentPadding: PaddingValues,
    val labelStyle: TextStyle,
)

object JazcRadioDefaults {
    @Composable
    fun style(variant: JazcRadioVariant = JazcRadioVariant.Plain): JazcRadioStyle {
        val card = variant == JazcRadioVariant.Card
        val colors = MaterialTheme.colorScheme
        return JazcRadioStyle(
            indicatorColors = RadioButtonDefaults.colors(),
            selectedContainer = if (card) colors.primaryContainer else Color.Transparent,
            container = if (card) colors.surfaceContainerLow else Color.Transparent,
            selectedBorder = if (card) BorderStroke(1.dp, colors.primary) else null,
            border = if (card) BorderStroke(1.dp, colors.outlineVariant) else null,
            shape = MaterialTheme.shapes.small,
            contentPadding = PaddingValues(horizontal = if (card) 16.dp else 4.dp, vertical = 12.dp),
            labelStyle = MaterialTheme.typography.bodyLarge,
        )
    }
}

/**
 * Radio individual. Con [label], toda la fila es un único control seleccionable de al menos **48 dp**.
 * El slot permite título y descripción. Sin label, añade contentDescription mediante modifier.
 * Para crear grupos usa [JazcRadioGroup], que incluye semántica selectableGroup.
 */
@Composable
fun JazcRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: JazcRadioStyle = JazcRadioDefaults.style(),
    interactionSource: MutableInteractionSource? = null,
    label: (@Composable ColumnScope.() -> Unit)? = null,
) {
    if (label == null) {
        RadioButton(selected = selected, onClick = onClick, modifier = modifier,
            enabled = enabled, colors = style.indicatorColors, interactionSource = interactionSource)
        return
    }
    Surface(modifier = modifier, shape = style.shape,
        color = if (selected) style.selectedContainer else style.container,
        border = if (selected) style.selectedBorder else style.border,
    ) {
        Row(modifier = Modifier.selectable(selected = selected, enabled = enabled,
            role = Role.RadioButton, interactionSource = interactionSource, indication = ripple(), onClick = onClick)
            .heightIn(min = 48.dp).padding(style.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            RadioButton(selected = selected, onClick = null, enabled = enabled, colors = style.indicatorColors)
            CompositionLocalProvider(LocalContentColor provides
                MaterialTheme.colorScheme.onSurface.copy(alpha = if (enabled) 1f else 0.38f)) {
                ProvideTextStyle(style.labelStyle) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp), content = label)
                }
            }
        }
    }
}

/**
 * Selección exclusiva con estado controlado por el caller. [options] debe contener valores únicos y estables.
 * Vertical ocupa el ancho disponible; Flow distribuye opciones y pasa a la siguiente línea si no caben.
 * [optionEnabled] desactiva opciones sin perder su descripción. El grupo no elige un valor por su cuenta.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T : Any> JazcRadioGroup(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: JazcRadioStyle = JazcRadioDefaults.style(),
    layout: JazcRadioLayout = JazcRadioLayout.Vertical,
    optionEnabled: (T) -> Boolean = { true },
    label: @Composable ColumnScope.(T) -> Unit,
) {
    val items: @Composable () -> Unit = {
        options.forEach { option ->
            key(option) {
                JazcRadioButton(selected = option == selectedOption,
                    onClick = { onOptionSelected(option) }, enabled = enabled && optionEnabled(option),
                    modifier = if (layout == JazcRadioLayout.Vertical) Modifier.fillMaxWidth() else Modifier.widthIn(min = 120.dp, max = 240.dp),
                    style = style, label = { label(option) })
            }
        }
    }
    if (layout == JazcRadioLayout.Vertical) {
        Column(modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) { items() }
    } else {
        FlowRow(modifier.selectableGroup(), horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) { items() }
    }
}
