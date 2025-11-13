package com.example.orphanapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.orphanapp.ui.theme.OrphanAppTheme

@Composable
fun ImpactReportingScreen(navController: NavController) {
    // TODO: Implement the impact reporting screen
}

@Preview(showBackground = true)
@Composable
fun ImpactReportingScreenPreview() {
    OrphanAppTheme {
        ImpactReportingScreen(rememberNavController())
    }
}
