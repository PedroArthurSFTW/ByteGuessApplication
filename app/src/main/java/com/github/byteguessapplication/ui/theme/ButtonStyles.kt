package com.github.byteguessapplication.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object AppButtonDefaults {
    val buttonPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    val defaultShape = RoundedCornerShape(8.dp)
    @Composable
    fun buttonShape() = MaterialTheme.shapes.medium // Agora seguro
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = AppButtonDefaults.buttonPadding
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = AppButtonDefaults.buttonShape(),
        enabled = enabled,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}