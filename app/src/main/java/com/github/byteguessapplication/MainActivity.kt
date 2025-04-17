package com.github.byteguessapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.byteguessapplication.navigation.AppNavigation
import com.github.byteguessapplication.presentation.viewmodel.CardViewModel
import com.github.byteguessapplication.ui.theme.ByteGuessApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val cardViewModel: CardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ByteGuessApplicationTheme {
                AppNavigation(viewModel = cardViewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ByteGuessApplicationTheme {
        val context = LocalContext.current
        val fakeViewModel = CardViewModel()

        AppNavigation(viewModel = fakeViewModel)
    }
}