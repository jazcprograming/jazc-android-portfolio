package com.jazc.portfolio

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import com.jazc.portfolio.navigation.AppRoute
import com.jazc.portfolio.pokemon.pokemonGraph
import com.jazc.portfolio.pokemon.PokemonRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var deepLinkId by mutableStateOf<Int?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) readDeepLink(intent)
        enableEdgeToEdge()
        setContent {
            PortfolioJAZCApp(deepLinkId = deepLinkId, onDeepLinkHandled = { deepLinkId = null })
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        readDeepLink(intent)
    }

    private fun readDeepLink(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW) return
        val uri = intent.data ?: return
        if (uri.scheme != "jazc" || uri.host != "pokemon" || uri.port != -1 || uri.userInfo != null) return
        if (uri.query != null || uri.fragment != null) return
        val id = uri.pathSegments.singleOrNull()?.toIntOrNull()?.takeIf { it > 0 } ?: return
        deepLinkId = id
    }
}

@Composable
fun PortfolioJAZCApp(deepLinkId: Int? = null, onDeepLinkHandled: () -> Unit = {}) {
    val navController = rememberNavController()
    var themeMode by rememberSaveable { mutableStateOf(ThemeMode.System) }
    var accent by rememberSaveable { mutableStateOf(JazcAccent.Mint) }
    val dark = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    JazcTheme(darkTheme = dark, accent = accent) {
        NavHost(navController = navController, startDestination = AppRoute.Home.route) {
            composable(AppRoute.Home.route) {
                MainNavigation(AppRoute.Home, navController) { modifier ->
                    PortfolioHome(
                        onOpenCatalog = { navController.navigate(AppRoute.DesignSystem.route) },
                        onOpenPokemon = { navController.navigate(AppRoute.Pokemon.route) },
                        modifier = modifier,
                    )
                }
            }
            composable(AppRoute.DesignSystem.route) {
                MainNavigation(AppRoute.DesignSystem, navController) { modifier ->
                    DesignSystemScreen(themeMode = themeMode, onThemeModeChange = { themeMode = it },
                        accent = accent, onAccentChange = { accent = it }, modifier = modifier)
                }
            }
            pokemonGraph(navController)
        }
        LaunchedEffect(deepLinkId) {
            deepLinkId?.let { id ->
                navController.navigate(PokemonRoute.Detail.createRoute(id))
                onDeepLinkHandled()
            }
        }
    }
}

@Composable
private fun MainNavigation(
    selected: AppRoute,
    navController: NavHostController,
    content: @Composable (Modifier) -> Unit,
) {
    NavigationSuiteScaffold(navigationSuiteItems = {
        item(selected = selected == AppRoute.Home,
            onClick = { navController.popBackStack(AppRoute.Home.route, inclusive = false) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_home)) })
        item(selected = selected == AppRoute.DesignSystem,
            onClick = { navController.navigate(AppRoute.DesignSystem.route) { launchSingleTop = true } },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text(stringResource(R.string.design_system)) })
    }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            content(Modifier.fillMaxSize().windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)))
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppPreview() = PortfolioJAZCApp()
