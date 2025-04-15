package com.github.byteguessapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.github.byteguessapplication.data.local.AppDatabase
import com.github.byteguessapplication.data.local.CardRepository
import com.github.byteguessapplication.navigation.AppNavigation
import com.github.byteguessapplication.presentation.viewmodel.CardViewModel
import com.github.byteguessapplication.presentation.viewmodel.CardViewModelFactory
import com.github.byteguessapplication.ui.theme.ByteGuessApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CardViewModel by viewModels {
        CardViewModelFactory(
            (application as ByteGuessApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ByteGuessApplicationTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ByteGuessApplicationTheme {
        val context = LocalContext.current
        val db = Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            AppDatabase::class.java
        ).build()

        val fakeViewModel = CardViewModel(
            repository = CardRepository(db.cardDao())
        )

        AppNavigation(viewModel = fakeViewModel)
    }
}