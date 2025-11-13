package com.example.orphanapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.orphanapp.ui.theme.OrphanAppTheme

@Composable
fun InventoryScreen(navController: NavController) {
    // TODO: Implement the inventory screen
}

@Preview(showBackground = true)
@Composable
fun InventoryScreenPreview() {
    OrphanAppTheme {
        InventoryScreen(rememberNavController())
    }
}
