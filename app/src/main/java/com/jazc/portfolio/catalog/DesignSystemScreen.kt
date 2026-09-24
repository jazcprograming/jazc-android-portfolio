package com.jazc.portfolio.catalog

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jazc.designsystem.component.*
import com.jazc.designsystem.text.uiText
import com.jazc.designsystem.theme.*
import com.jazc.portfolio.R
import kotlinx.coroutines.launch

enum class ThemeMode(@param:StringRes val label: Int) {
    System(R.string.theme_system), Light(R.string.theme_light), Dark(R.string.theme_dark)
}

private enum class CatalogPage(@param:StringRes val label: Int) {
    Overview(R.string.tab_overview), Type(R.string.tab_type), Buttons(R.string.tab_buttons),
    Selection(R.string.tab_selection), Playground(R.string.tab_playground)
}

@Composable
fun PortfolioHome(onOpenCatalog: () -> Unit, modifier: Modifier = Modifier) {
    CatalogLayout(modifier) {
        item {
            Column(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(
                MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.surface)),
                RoundedCornerShape(28.dp)).padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Eyebrow("JAZC / ANDROID")
                JazcText(uiText(R.string.home_title), size = JazcTextSize.Display40)
                Body(R.string.home_description)
                JazcButton(onClick = onOpenCatalog, modifier = Modifier.testTag("open_catalog")) {
                    JazcText(uiText(R.string.open_catalog))
                }
            }
        }
        item {
            DemoCard(R.string.home_card_title, R.string.home_card_description) {
                TokenRow("01", stringResource(R.string.home_feature_one))
                TokenRow("02", stringResource(R.string.home_feature_two))
                TokenRow("03", stringResource(R.string.home_feature_three))
            }
        }
    }
}

@Composable
fun DesignSystemScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    accent: JazcAccent,
    onAccentChange: (JazcAccent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var page by rememberSaveable { mutableStateOf(CatalogPage.Overview) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    Box(modifier, contentAlignment = Alignment.TopCenter) {
        LazyColumn(state = listState, modifier = Modifier.widthIn(max = 840.dp).fillMaxSize()
            .testTag("catalog_list"), contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Eyebrow("JAZC / COMPONENT LIBRARY")
                    JazcText(uiText(R.string.design_system), size = JazcTextSize.Xxl32)
                    Body(R.string.catalog_description)
                }
            }
            item {
                Choices(CatalogPage.entries, page, {
                    page = it
                    scope.launch { listState.scrollToItem(0) }
                }, label = { stringResource(it.label) }, tag = { "page_${it.name}" })
            }
            when (page) {
                CatalogPage.Overview -> {
                    item { Appearance(themeMode, onThemeModeChange, accent, onAccentChange) }
                    item { DemoCard(R.string.overview_title, R.string.overview_description) {
                        TokenRow("Aa", stringResource(R.string.overview_typography))
                        TokenRow("48", stringResource(R.string.overview_touch))
                        TokenRow("{ }", stringResource(R.string.overview_slots))
                    } }
                    item { CodeBlock("JazcTheme {\n    JazcButton(onClick = ::save) {\n        JazcText(uiText(R.string.save))\n    }\n}") }
                }
                CatalogPage.Type -> {
                    item { DemoCard(R.string.type_title, R.string.type_description) {
                        JazcTextSize.entries.forEach { token ->
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                JazcText(uiText(token.name), size = token)
                                Text("${token.fontSize.value.toInt()} sp / ${token.lineHeight.value.toInt()} sp · ${token.weight.weight}",
                                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (token != JazcTextSize.entries.last()) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    } }
                    item { TextSources() }
                    item { CodeBlock("JazcText(\n    text = uiText(R.string.description),\n    size = JazcTextSize.Xs12, // 12 / 16 sp\n    style = TextStyle(fontWeight = FontWeight.Medium),\n)") }
                }
                CatalogPage.Buttons -> {
                    item { ButtonVariants() }
                    item { DemoCard(R.string.sizes_title, R.string.sizes_description) {
                        JazcButtonSize.entries.forEach { size ->
                            JazcButton(onClick = {}, style = JazcButtonDefaults.style(size = size)) {
                                JazcText(uiText("${size.name} · ${size.minHeight.value.toInt()} dp"))
                            }
                        }
                    } }
                    item { ButtonStates() }
                    item { CodeBlock("JazcButton(\n    onClick = ::save,\n    style = JazcButtonDefaults.style(\n        variant = JazcButtonVariant.Outlined,\n    ).copy(shape = RoundedCornerShape(24.dp)),\n) {\n    Icon(Icons.Default.Check, contentDescription = null)\n    JazcText(uiText(R.string.save))\n}") }
                }
                CatalogPage.Selection -> {
                    item { RadioExamples() }
                    item { CodeBlock("JazcRadioGroup(\n    options = plans,\n    selectedOption = selectedPlan,\n    onOptionSelected = { selectedPlan = it },\n    style = JazcRadioDefaults.style(JazcRadioVariant.Card),\n) { plan ->\n    JazcText(uiText(plan.title))\n    JazcText(uiText(plan.description), size = JazcTextSize.Sm14)\n}") }
                }
                CatalogPage.Playground -> {
                    item { Appearance(themeMode, onThemeModeChange, accent, onAccentChange) }
                    item { ButtonPlayground() }
                    item { TextPlayground() }
                    item { RadioPlayground() }
                }
            }
            item { Text(stringResource(R.string.catalog_footer), style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    }
}

@Composable
private fun Appearance(mode: ThemeMode, onMode: (ThemeMode) -> Unit, accent: JazcAccent, onAccent: (JazcAccent) -> Unit) {
    DemoCard(R.string.appearance_title, R.string.appearance_description) {
        Choices(ThemeMode.entries, mode, onMode, label = { stringResource(it.label) })
        Choices(JazcAccent.entries, accent, onAccent, label = {
            stringResource(if (it == JazcAccent.Mint) R.string.accent_mint else R.string.accent_blue)
        })
    }
}

@Composable
private fun TextSources() {
    val emphasis = stringResource(R.string.text_emphasis)
    val rich = buildAnnotatedString {
        append(stringResource(R.string.text_rich_prefix))
        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) { append(emphasis) }
    }
    val legacy = remember(emphasis) { SpannableString(emphasis).apply {
        setSpan(StyleSpan(Typeface.BOLD_ITALIC), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    } }
    DemoCard(R.string.text_sources_title, R.string.text_sources_description) {
        SourceExample("String") { JazcText(uiText("Hello, Compose")) }
        SourceExample("@StringRes") { JazcText(uiText(R.string.text_resource_sample, "JAZC")) }
        SourceExample("AnnotatedString") { JazcText(uiText(rich)) }
        SourceExample("Spannable") { JazcText(uiText(legacy)) }
    }
}

@Composable
private fun SourceExample(name: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(name, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
        content()
    }
}

@Composable
private fun ButtonVariants() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    DemoCard(R.string.button_variants_title, R.string.button_variants_description) {
        JazcButtonVariant.entries.forEach { variant ->
            JazcButton(onClick = { count++ }, modifier = Modifier.fillMaxWidth(),
                style = JazcButtonDefaults.style(variant)) { JazcText(uiText(variant.name)) }
        }
        Text(stringResource(R.string.click_count, count), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ButtonStates() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    DemoCard(R.string.states_title, R.string.states_description) {
        JazcButton(onClick = { count++ }, enabled = false) { JazcText(uiText(R.string.state_disabled)) }
        JazcButton(onClick = { count++ }, loading = true) { JazcText(uiText(R.string.action_save)) }
        JazcButton(onClick = { count++ }) {
            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
            JazcText(uiText(R.string.action_save))
        }
        Text(stringResource(R.string.click_count, count), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun RadioExamples() {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf(0, 1, 2)
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        JazcRadioVariant.entries.forEach { variant ->
            DemoCard(if (variant == JazcRadioVariant.Card) R.string.radio_card_title else R.string.radio_plain_title,
                R.string.radio_description) {
                JazcRadioGroup(options = options, selectedOption = selected, onOptionSelected = { selected = it },
                    optionEnabled = { it != 2 }, style = JazcRadioDefaults.style(variant)) { option ->
                    JazcText(uiText(stringResource(R.string.option_number, option + 1)))
                    JazcText(uiText(if (option == 2) R.string.state_disabled else R.string.radio_support), size = JazcTextSize.Sm14)
                }
            }
        }
        DemoCard(R.string.radio_standalone_title, R.string.radio_standalone_description) {
            val description = stringResource(R.string.radio_standalone_label)
            JazcRadioButton(selected = selected == 0, onClick = { selected = 0 },
                modifier = Modifier.semantics { contentDescription = description })
        }
    }
}

@Composable
private fun ButtonPlayground() {
    var variant by rememberSaveable { mutableStateOf(JazcButtonVariant.Primary) }
    var size by rememberSaveable { mutableStateOf(JazcButtonSize.Md56) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var loading by rememberSaveable { mutableStateOf(false) }
    var rounded by rememberSaveable { mutableStateOf(false) }
    var count by rememberSaveable { mutableIntStateOf(0) }
    DemoCard(R.string.button_play_title, R.string.button_play_description) {
        Choices(JazcButtonVariant.entries, variant, { variant = it }, label = { it.name })
        Choices(JazcButtonSize.entries, size, { size = it }, label = { it.name })
        Toggle(R.string.control_enabled, enabled, { enabled = it }, "button_enabled")
        Toggle(R.string.control_loading, loading, { loading = it }, "button_loading")
        Toggle(R.string.control_rounded, rounded, { rounded = it }, "button_rounded")
        PreviewArea {
            val base = JazcButtonDefaults.style(variant, size)
            JazcButton(onClick = { count++ }, enabled = enabled, loading = loading,
                modifier = Modifier.testTag("playground_button"),
                style = base.copy(shape = if (rounded) RoundedCornerShape(50) else base.shape)) {
                JazcText(uiText(R.string.action_try))
            }
            Text(stringResource(R.string.click_count, count), modifier = Modifier.testTag("click_count"),
                style = MaterialTheme.typography.bodySmall)
        }
        CodeBlock("JazcButtonDefaults.style(\n    variant = JazcButtonVariant.${variant.name},\n    size = JazcButtonSize.${size.name},\n)${if (rounded) ".copy(shape = RoundedCornerShape(50))" else ""}\n// enabled = $enabled, loading = $loading")
    }
}

@Composable
private fun TextPlayground() {
    var size by rememberSaveable { mutableStateOf(JazcTextSize.Md16) }
    var bold by rememberSaveable { mutableStateOf(false) }
    var accent by rememberSaveable { mutableStateOf(false) }
    DemoCard(R.string.text_play_title, R.string.text_play_description) {
        Choices(JazcTextSize.entries, size, { size = it }, label = { "${it.fontSize.value.toInt()} sp" })
        Toggle(R.string.control_bold, bold, { bold = it })
        Toggle(R.string.control_accent, accent, { accent = it })
        PreviewArea {
            JazcText(uiText(R.string.text_preview), size = size, style = TextStyle(
                fontWeight = if (bold) FontWeight.Bold else size.weight,
                color = if (accent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface))
            Text("${size.name} · ${size.fontSize.value.toInt()} / ${size.lineHeight.value.toInt()} sp",
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun RadioPlayground() {
    var variant by rememberSaveable { mutableStateOf(JazcRadioVariant.Card) }
    var layout by rememberSaveable { mutableStateOf(JazcRadioLayout.Vertical) }
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    DemoCard(R.string.radio_play_title, R.string.radio_play_description) {
        Choices(JazcRadioVariant.entries, variant, { variant = it }, label = { it.name })
        Choices(JazcRadioLayout.entries, layout, { layout = it }, label = { it.name })
        Toggle(R.string.control_enabled, enabled, { enabled = it })
        JazcRadioGroup(options = listOf(0, 1, 2), selectedOption = selected, onOptionSelected = { selected = it },
            style = JazcRadioDefaults.style(variant), layout = layout, enabled = enabled) {
            JazcText(uiText(stringResource(R.string.option_number, it + 1)))
        }
        Text(stringResource(R.string.radio_current, selected + 1), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun CatalogLayout(modifier: Modifier, content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    Box(modifier, contentAlignment = Alignment.TopCenter) {
        LazyColumn(Modifier.widthIn(max = 840.dp).fillMaxSize(), contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp), content = content)
    }
}

@Composable
private fun DemoCard(@StringRes title: Int, @StringRes description: Int, content: @Composable ColumnScope.() -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerLow, shape = MaterialTheme.shapes.medium) {
        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            JazcText(uiText(title), size = JazcTextSize.Lg20)
            Body(description)
            content()
        }
    }
}

@Composable
private fun Body(@StringRes resource: Int) = JazcText(uiText(resource), size = JazcTextSize.Sm14,
    style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant))

@Composable
private fun Eyebrow(text: String) = Text(text, style = MaterialTheme.typography.labelMedium,
    color = MaterialTheme.colorScheme.primary)

@Composable
private fun TokenRow(token: String, description: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(token, modifier = Modifier.widthIn(min = 32.dp), style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary)
        Text(description, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> Choices(options: List<T>, selected: T, onSelect: (T) -> Unit,
    label: @Composable (T) -> String, tag: (T) -> String = { "choice_$it" }) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            FilterChip(selected = option == selected, onClick = { onSelect(option) },
                label = { Text(label(option)) }, modifier = Modifier.testTag(tag(option)))
        }
    }
}

@Composable
private fun Toggle(@StringRes label: Int, checked: Boolean, onChecked: (Boolean) -> Unit, tag: String = "toggle_$label") {
    Row(Modifier.fillMaxWidth().testTag(tag).toggleable(value = checked, role = Role.Switch, onValueChange = onChecked)
        .heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(label), modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Composable
private fun PreviewArea(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerHighest,
        MaterialTheme.shapes.small).animateContentSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally, content = content)
}

@Composable
private fun CodeBlock(code: String) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = MaterialTheme.shapes.small) {
        Text(code, modifier = Modifier.fillMaxWidth().padding(16.dp), style = JazcTextSize.Xs12.style.copy(fontFamily = FontFamily.Monospace))
    }
}

@Preview(name = "Catalog / light", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Catalog / large font", showBackground = true, widthDp = 360, heightDp = 800, fontScale = 1.6f)
@Composable
private fun CatalogPreview() = JazcTheme(darkTheme = false) {
    DesignSystemScreen(ThemeMode.Light, {}, JazcAccent.Mint, {})
}

@Preview(name = "Catalog / dark tablet", showBackground = true, widthDp = 900, heightDp = 700)
@Composable
private fun CatalogDarkPreview() = JazcTheme(darkTheme = true) {
    DesignSystemScreen(ThemeMode.Dark, {}, JazcAccent.Mint, {})
}
