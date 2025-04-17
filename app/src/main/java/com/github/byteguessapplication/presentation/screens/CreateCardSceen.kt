package com.github.byteguessapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.github.byteguessapplication.data.local.CategoryEntity

import androidx.hilt.navigation.compose.hiltViewModel
import com.github.byteguessapplication.presentation.viewmodel.CardViewModel
import com.github.byteguessapplication.presentation.viewmodel.CreateCardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCardScreen(
    viewModel: CreateCardViewModel = hiltViewModel<CreateCardViewModel>(),
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    createCardMode: CardViewModel.CardMode
) {
    val answer by viewModel.answer.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val availableCategories by viewModel.availableCategories.collectAsState()
    val tips by viewModel.tips.collectAsState()
    val isLoadingCategories by viewModel.isLoadingCategories.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val isSaveEnabled by viewModel.isSaveEnabled.collectAsState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = viewModel.saveResult) {
        viewModel.saveResult.collect { success ->
            if (success) {
                Toast.makeText(context, "Card saved successfully!", Toast.LENGTH_SHORT).show()
                onSaveSuccess()
            } else {
                if(errorState.generalError == null) {
                    Toast.makeText(context, "Failed to save card.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Card") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.saveCard() }, enabled = isSaveEnabled) {
                        Icon(Icons.Filled.Check, contentDescription = "Save Card")
                    }
                }
            )
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = answer,
                onValueChange = { viewModel.onAnswerChanged(it) },
                label = { Text("Answer") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorState.answerError != null,
                singleLine = true,
                supportingText = { if (errorState.answerError != null) Text(errorState.answerError!!) }
            )

            CategorySelector(
                selectedCategory = selectedCategory,
                availableCategories = availableCategories,
                onCategorySelected = { viewModel.onCategorySelected(it) },
                isLoading = isLoadingCategories,
                errorText = errorState.categoryError
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Use Light Mode for New Category")
            }

            Text("Tips", style = MaterialTheme.typography.titleMedium)

            if (errorState.tipsError != null) {
                Text(errorState.tipsError!!, color = MaterialTheme.colorScheme.error)
            }

            tips.forEachIndexed { index, tipText ->
                TipInputItem(
                    tipNumber = index + 1,
                    value = tipText,
                    onValueChange = { viewModel.onTipChanged(index, it) },
                    onRemoveClick = { viewModel.removeTipField(index) },
                    canRemove = tips.size > 1
                )
            }

            Button(
                onClick = { viewModel.addTipField() },
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Add Tip")
            }

            if (errorState.generalError != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    errorState.generalError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(64.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    selectedCategory: CategoryEntity?,
    availableCategories: List<CategoryEntity>,
    onCategorySelected: (CategoryEntity) -> Unit,
    isLoading: Boolean,
    errorText: String?
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCategory?.name ?: "",
            onValueChange = { },
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            isError = errorText != null,
            supportingText = { if (errorText != null) Text(errorText) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (isLoading) {
                DropdownMenuItem(
                    text = { Text("Loading...") },
                    onClick = { },
                    enabled = false
                )
            } else if (availableCategories.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No categories found") },
                    onClick = { },
                    enabled = false
                )
            } else {
                availableCategories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TipInputItem(
    tipNumber: Int,
    value: String,
    onValueChange: (String) -> Unit,
    onRemoveClick: () -> Unit,
    canRemove: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Tip #$tipNumber") },
            modifier = Modifier.weight(1f),
        )
        if (canRemove) {
            IconButton(onClick = onRemoveClick) {
                Icon(Icons.Filled.Close, contentDescription = "Remove Tip $tipNumber")
            }
        } else {
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}