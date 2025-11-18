package com.example.orphanapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.data.InventoryItem
import com.example.orphanapp.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditInventoryItemScreen(navController: NavController, inventoryViewModel: InventoryViewModel, itemId: String?) {
    val inventoryList by inventoryViewModel.inventory.collectAsState()
    val isEditing = itemId != null
    val item = if (isEditing) inventoryList.find { it.id == itemId } else null

    var itemName by remember { mutableStateOf(item?.name ?: "") }
    var itemQuantity by remember { mutableStateOf(item?.quantity ?: "") }

    LaunchedEffect(item) {
        if (item != null) {
            itemName = item.name
            itemQuantity = item.quantity
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Item" else "Add Item") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Item Name") },
                singleLine = true,
                enabled = !isEditing // Disable editing of the name for existing items
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = itemQuantity,
                onValueChange = { itemQuantity = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                    val updatedItem = item?.copy(
                        quantity = itemQuantity
                    ) ?: InventoryItem(
                        name = itemName,
                        quantity = itemQuantity
                    )

                    if (isEditing) {
                        inventoryViewModel.updateInventoryItem(updatedItem)
                    } else {
                        inventoryViewModel.addInventoryItem(updatedItem)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = itemName.isNotBlank() && itemQuantity.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }
}
