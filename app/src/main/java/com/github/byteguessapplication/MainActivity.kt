package com.github.byteguessapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.github.byteguessapplication.data.local.AppDatabase
import com.github.byteguessapplication.data.local.CardRepository
import com.github.byteguessapplication.presentation.screens.CardListScreen
import com.github.byteguessapplication.presentation.viewmodel.CardViewModel
import com.github.byteguessapplication.presentation.viewmodel.CardViewModelFactory
import com.github.byteguessapplication.ui.theme.ByteGuessApplicationTheme

class MainActivity : ComponentActivity() {
    // Inicializa o ViewModel com a factory
    private val viewModel: CardViewModel by viewModels {
        CardViewModelFactory(
            (application as ByteGuessApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ByteGuessApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CardListScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ByteGuessApplicationTheme {
        val context = LocalContext.current
        val fakeViewModel = remember {
            CardViewModel(
                repository = CardRepository(
                    Room.inMemoryDatabaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java
                    ).build().cardDao(),
                )
            )
        }
        CardListScreen(viewModel = fakeViewModel)
    }
}