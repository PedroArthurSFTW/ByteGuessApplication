package com.github.byteguessapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.byteguessapplication.presentation.screens.CardListScreen
import com.github.byteguessapplication.presentation.screens.CreateCardScreen
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
                            is CardViewModel.NavigationEvent.NavigateToCreateCard -> {
                                navController.navigate(
                                    Screen.CreateCard.withArgs(event.mode.name)
                                )
                            }
                            is CardViewModel.NavigationEvent.NavigateToPlay -> {
                                navController.navigate(
                                    Screen.Play.withArgs(event.mode.name)
                                )
                            }
                            is CardViewModel.NavigationEvent.NavigateToEdit -> {
                                navController.navigate(
                                    Screen.Edit.withArgs(event.mode.name)
                                )
                            }
                        }
                    }
                )
            }
        }

        composable(
            route = "createCard/{mode}",
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { backStackEntry ->
            val modeArg = backStackEntry.arguments?.getString("mode")
            val mode = if (modeArg != null) CardViewModel.CardMode.valueOf(modeArg) else CardViewModel.CardMode.LIGHT
            CreateCardScreen(
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.popBackStack()
                    viewModel.loadCards()
                },
                createCardMode = mode
            )
        }

        composable(
            route = Screen.Play.routeWithArgs,
            arguments = Screen.Play.arguments
        ) { backStackEntry ->
            // Implementar tela de jogo
        }

        composable(
            route = Screen.Edit.routeWithArgs,
            arguments = Screen.Edit.arguments
        ) { backStackEntry ->
            // Implementar tela de edição
        }
    }
}