package com.example.orphanapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.Orphan
import com.example.orphanapp.repository.OrphanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EnrollmentViewModel(private val repository: OrphanRepository) : ViewModel() {

    private val _newlyCreatedOrphanId = MutableStateFlow<String?>(null)
    val newlyCreatedOrphanId: StateFlow<String?> = _newlyCreatedOrphanId.asStateFlow()

    val orphans: StateFlow<List<Orphan>> = repository.getOrphans()
        .catch { exception ->
            Log.e("EnrollmentViewModel", "Error getting orphans", exception)
            emit(emptyList()) // Emit an empty list on error to prevent crash
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addOrphan(orphan: Orphan) {
        viewModelScope.launch {
            try {
                val newId = repository.addOrphan(orphan)
                _newlyCreatedOrphanId.value = newId
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Failed to add orphan", e)
                // Consider exposing an error state to the UI
            }
        }
    }

    fun onEnrollmentComplete() {
        _newlyCreatedOrphanId.value = null
    }

    fun updateOrphan(orphan: Orphan) {
        viewModelScope.launch {
            try {
                repository.updateOrphan(orphan)
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Failed to update orphan", e)
            }
        }
    }

    fun acceptOrphan(orphanId: String) {
        viewModelScope.launch {
            try {
                repository.updateOrphanStatus(orphanId, "Active")
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Failed to accept orphan", e)
            }
        }
    }

    fun declineOrphan(orphanId: String) {
        viewModelScope.launch {
            try {
                repository.updateOrphanStatus(orphanId, "Declined")
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Failed to decline orphan", e)
            }
        }
    }
}

class EnrollmentViewModelFactory(private val repository: OrphanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnrollmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnrollmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
