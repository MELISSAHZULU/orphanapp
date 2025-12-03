package com.example.orphanapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class ChartData(val label: String, val value: Float)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImpactReportingScreen(navController: NavController) {
    // In a real app, this data would come from various ViewModels.
    val kpiData = mapOf(
        "Orphans Supported" to "125",
        "Donations This Month" to "$8,500",
        "Volunteers Engaged" to "42"
    )
    val chartData = listOf(
        ChartData("Food & Water", 65f),
        ChartData("Healthcare", 15f),
        ChartData("Education", 10f),
        ChartData("Clothing", 5f),
        ChartData("Other", 5f)
    )
    val successStory = "\"Thanks to the support from this orphanage, I was able to attend school and now have a dream of becoming a doctor. I am forever grateful.\" - Jane Doe, former resident."

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Our Impact") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Key Metrics
            KpiSection(kpiData)
            Spacer(modifier = Modifier.height(24.dp))

            // Donation Chart
            Card(elevation = CardDefaults.cardElevation(4.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Donation Utilization", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    BarChart(chartData)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Success Story
            Card(elevation = CardDefaults.cardElevation(4.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Success Story", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(successStory, style = MaterialTheme.typography.bodyLarge, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                }
            }
        }
    }
}

@Composable
private fun KpiSection(kpiData: Map<String, String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        kpiData.forEach { (label, value) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(label, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun BarChart(data: List<ChartData>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        data.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(item.label, modifier = Modifier.width(120.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .fillMaxWidth(item.value / 100f)
                            .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${item.value}%", fontSize = 12.sp)
                }
            }
        }
    }
}
