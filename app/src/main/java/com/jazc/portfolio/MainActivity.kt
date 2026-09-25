package com.jazc.portfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jazc.designsystem.theme.JazcAccent
import com.jazc.designsystem.theme.JazcTheme
import com.jazc.portfolio.catalog.DesignSystemScreen
import com.jazc.portfolio.catalog.PortfolioHome
import com.jazc.portfolio.catalog.ThemeMode
import com.jazc.portfolio.pokemon.PokemonListScreen

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PortfolioJAZCApp() }
    }
}

private enum class Destination { Home, DesignSystem, Pokemon }

@Composable
fun PortfolioJAZCApp() {
    var destination by rememberSaveable { mutableStateOf(Destination.Home) }
    var themeMode by rememberSaveable { mutableStateOf(ThemeMode.System) }
    var accent by rememberSaveable { mutableStateOf(JazcAccent.Mint) }
    val dark = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    BackHandler(destination != Destination.Home) { destination = Destination.Home }
    JazcTheme(darkTheme = dark, accent = accent) {
        if (destination == Destination.Pokemon) {
            PokemonListScreen(onBack = { destination = Destination.Home })
        } else NavigationSuiteScaffold(navigationSuiteItems = {
            item(selected = destination == Destination.Home,
                onClick = { destination = Destination.Home },
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text(stringResource(R.string.nav_home)) })
            item(selected = destination == Destination.DesignSystem,
                onClick = { destination = Destination.DesignSystem },
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                label = { Text(stringResource(R.string.design_system)) })
        }) {
            Surface(modifier = Modifier.fillMaxSize()) {
                val contentModifier = Modifier.fillMaxSize().windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
                when (destination) {
                    Destination.Home -> PortfolioHome(
                        onOpenCatalog = { destination = Destination.DesignSystem },
                        onOpenPokemon = { destination = Destination.Pokemon },
                        modifier = contentModifier,
                    )
                    Destination.Pokemon -> Unit
                    Destination.DesignSystem -> DesignSystemScreen(themeMode = themeMode,
                        onThemeModeChange = { themeMode = it }, accent = accent,
                        onAccentChange = { accent = it }, modifier = contentModifier)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppPreview() = PortfolioJAZCApp()
