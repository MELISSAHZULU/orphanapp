package com.example.orphanapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A shared composable for displaying a labeled piece of information.
 */
@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(text = "$label:", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
        Text(text = value)
    }
}
