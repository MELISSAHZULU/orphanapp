package com.example.orphanapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.InventoryItem
import com.example.orphanapp.repository.InventoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: InventoryRepository) : ViewModel() {

    val inventory: StateFlow<List<InventoryItem>> = repository.getInventory()
        .catch { exception ->
            Log.e("InventoryViewModel", "Error getting inventory", exception)
            emit(emptyList()) // Emit an empty list on error
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            try {
                repository.addInventoryItem(item)
            } catch (e: Exception) {
                Log.e("InventoryViewModel", "Failed to add inventory item", e)
            }
        }
    }

    fun updateInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            try {
                repository.updateInventoryItem(item)
            } catch (e: Exception) {
                Log.e("InventoryViewModel", "Failed to update inventory item", e)
            }
        }
    }
}

class InventoryViewModelFactory(private val repository: InventoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
