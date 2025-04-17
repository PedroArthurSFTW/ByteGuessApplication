package com.github.byteguessapplication.navigation

sealed class Screen(
    val route: String,
) {
    object Main : Screen("main")

    object CreateCard : Screen(
        route = "createCard",
    )

    object Play : Screen(
        route = "play",
    )

    object Edit : Screen(
        route = "edit",
    )
}