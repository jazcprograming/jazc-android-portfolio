package com.jazc.portfolio.navigation

sealed class AppRoute(val route: String) {
    data object Home : AppRoute("home")
    data object DesignSystem : AppRoute("design-system")
    data object Pokemon : AppRoute("pokemon")
}
