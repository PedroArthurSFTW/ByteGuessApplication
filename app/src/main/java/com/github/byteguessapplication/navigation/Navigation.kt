package com.github.byteguessapplication.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.byteguessapplication.presentation.screens.CardListScreen
import com.github.byteguessapplication.presentation.screens.CreateCardScreen
import com.github.byteguessapplication.presentation.screens.GameScreen
import com.github.byteguessapplication.presentation.viewmodel.CardViewModel
import com.github.byteguessapplication.ui.theme.ByteGuessApplicationTheme

@Composable
fun AppNavigation(viewModel: CardViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            ByteGuessApplicationTheme {
                CardListScreen(
                    viewModel = viewModel,
                    onNavigate = { event ->
                        when (event) {
                            is CardViewModel.NavigationEvent.NavigateToCreateCardScreen -> {
                                navController.navigate(Screen.CreateCard.route)
                            }
                            is CardViewModel.NavigationEvent.NavigateToPlayScreen -> {
                                navController.navigate(Screen.Play.route)
                            }
                            is CardViewModel.NavigationEvent.NavigateToEditScreen -> {
                                navController.navigate(Screen.Edit.route)
                            }
                        }
                    }
                )
            }
        }

        composable(
            route = Screen.CreateCard.route
        ) {
            ByteGuessApplicationTheme {
                CreateCardScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = Screen.Play.route
        ) {
            ByteGuessApplicationTheme {
                GameScreen()
            }
        }

        composable(
            route = Screen.Edit.route
        ) {
            ByteGuessApplicationTheme {
                CreateCardScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}