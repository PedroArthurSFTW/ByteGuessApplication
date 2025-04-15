package com.github.byteguessapplication.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val routeWithArgs: String = route
) {
    object Main : Screen("main")

    object CreateCard : Screen(
        route = "createCard",
        routeWithArgs = "createCard/{mode}"
    ) {
        fun withArgs(mode: String) = "createCard/$mode"
        val arguments = listOf(
            navArgument("mode") { type = NavType.StringType }
        )
    }

    object Play : Screen(
        route = "play",
        routeWithArgs = "play/{mode}"
    ) {
        fun withArgs(mode: String) = "play/$mode"
        val arguments = listOf(
            navArgument("mode") { type = NavType.StringType }
        )
    }

    object Edit : Screen(
        route = "edit",
        routeWithArgs = "edit/{mode}"
    ) {
        fun withArgs(mode: String) = "edit/$mode"
        val arguments = listOf(
            navArgument("mode") { type = NavType.StringType }
        )
    }
}