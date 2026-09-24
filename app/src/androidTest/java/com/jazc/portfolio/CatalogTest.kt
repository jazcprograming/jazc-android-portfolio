package com.jazc.portfolio

import android.graphics.Bitmap
import android.content.res.Configuration
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.Locale
import com.jazc.designsystem.theme.JazcAccent
import com.jazc.designsystem.theme.JazcTheme
import com.jazc.portfolio.catalog.DesignSystemScreen
import com.jazc.portfolio.catalog.ThemeMode
import org.junit.Rule
import org.junit.Test

class CatalogTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun catalogNavigationAndCustomizationWorkTogether() {
        compose.onNodeWithTag("open_catalog").performClick()
        compose.onNodeWithTag("page_Playground").performClick()
        compose.onNodeWithTag("button_loading").performScrollTo().performClick()
        compose.onNodeWithTag("playground_button").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithTag("button_loading").performScrollTo().performClick()
        compose.onNodeWithTag("playground_button").performScrollTo().assertIsEnabled().performClick()
        compose.onNodeWithTag("click_count").assertTextContains("1", substring = true)
        compose.onNodeWithTag("button_enabled").performScrollTo().performClick()
        compose.onNodeWithTag("playground_button").performScrollTo().assertIsNotEnabled()
    }

    @Test fun captureCatalogScreensForVisualReview() {
        compose.onNodeWithTag("open_catalog").performClick()
        compose.onNodeWithTag("choice_Dark").performClick()
        capture("catalog-dark")
        compose.onNodeWithTag("choice_Light").performClick()
        capture("catalog-light")
        compose.onNodeWithTag("page_Type").performClick()
        capture("catalog-typography")
        compose.onNodeWithTag("page_Buttons").performClick()
        capture("catalog-buttons")
        compose.onNodeWithTag("page_Selection").performClick()
        capture("catalog-radios")
        // Exercise a narrow viewport and large Spanish text without changing device preferences.
        compose.runOnUiThread {
            val config = Configuration(compose.activity.resources.configuration).apply {
                setLocale(Locale.forLanguageTag("es"))
                fontScale = 1.6f
            }
            val localized = compose.activity.createConfigurationContext(config)
            compose.activity.setContent {
                val density = LocalDensity.current
                CompositionLocalProvider(LocalContext provides localized,
                    LocalConfiguration provides config,
                    LocalDensity provides Density(density.density, 1.6f)) {
                    JazcTheme(darkTheme = true) {
                        Surface { Box(Modifier.width(320.dp).fillMaxHeight()) {
                            DesignSystemScreen(ThemeMode.Dark, {}, JazcAccent.Mint, {})
                        } }
                    }
                }
            }
        }
        compose.onNodeWithText("Elige la apariencia").assertExists()
        capture("catalog-spanish-320dp-large-font")
    }

    private fun capture(name: String) {
        compose.waitForIdle()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val directory = File(context.getExternalFilesDir(null), "catalog-review").apply { mkdirs() }
        File(directory, "$name.png").outputStream().use {
            compose.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}
