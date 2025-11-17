package com.example.orphanapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.StaffMember
import com.example.orphanapp.repository.StaffRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StaffViewModel(private val repository: StaffRepository) : ViewModel() {

    val staff: StateFlow<List<StaffMember>> = repository.getStaff()
        .catch { exception ->
            Log.e("StaffViewModel", "Error getting staff", exception)
            emit(emptyList()) // Emit an empty list on error
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addStaff(staffMember: StaffMember) {
        viewModelScope.launch {
            try {
                repository.addStaff(staffMember)
            } catch (e: Exception) {
                Log.e("StaffViewModel", "Failed to add staff member", e)
            }
        }
    }

    fun updateStaff(staffMember: StaffMember) {
        viewModelScope.launch {
            try {
                repository.updateStaff(staffMember)
            } catch (e: Exception) {
                Log.e("StaffViewModel", "Failed to update staff member", e)
            }
        }
    }
}

class StaffViewModelFactory(private val repository: StaffRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StaffViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StaffViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
