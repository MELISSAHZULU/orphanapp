package com.example.orphanapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.Orphan
import com.example.orphanapp.repository.OrphanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EnrollmentViewModel(private val orphanRepository: OrphanRepository) : ViewModel() {

    private val _orphans = MutableStateFlow<List<Orphan>>(emptyList())
    val orphans: StateFlow<List<Orphan>> = _orphans.asStateFlow()

    private val _selectedOrphan = MutableStateFlow<Orphan?>(null)
    val selectedOrphan: StateFlow<Orphan?> = _selectedOrphan.asStateFlow()

    private val _newlyCreatedOrphanId = MutableStateFlow<String?>(null)
    val newlyCreatedOrphanId: StateFlow<String?> = _newlyCreatedOrphanId.asStateFlow()

    fun loadOrphansForOrganization(organizationId: String) {
        viewModelScope.launch {
            if (organizationId.isNotBlank()) {
                orphanRepository.getOrphans(organizationId)
                    .catch { e ->
                        Log.e("EnrollmentViewModel", "Error loading orphans", e)
                        // Optionally expose an error state to the UI
                    }
                    .collect { orphanList ->
                        _orphans.value = orphanList
                    }
            } else {
                _orphans.value = emptyList()
            }
        }
    }

    fun addOrphan(orphan: Orphan) {
        viewModelScope.launch {
            try {
                val newOrphanId = orphanRepository.addOrphan(orphan)
                _newlyCreatedOrphanId.value = newOrphanId
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Error adding orphan", e)
                // Optionally expose an error state to the UI
            }
        }
    }

    fun onEnrollmentComplete() {
        _newlyCreatedOrphanId.value = null
    }

    fun getOrphanById(orphanId: String) {
        viewModelScope.launch {
            try {
                _selectedOrphan.value = orphanRepository.getOrphanById(orphanId)
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Error fetching orphan by ID", e)
                _selectedOrphan.value = null // Reset on error
            }
        }
    }

    fun updateOrphan(orphan: Orphan) {
        viewModelScope.launch {
            try {
                orphanRepository.updateOrphan(orphan)
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Error updating orphan", e)
                // Optionally expose an error state to the UI
            }
        }
    }

    fun acceptOrphan(orphanId: String) {
        viewModelScope.launch {
            try {
                orphanRepository.updateOrphanStatus(orphanId, "Enrolled")
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Error accepting orphan", e)
                // Optionally expose an error state to the UI
            }
        }
    }

    fun declineOrphan(orphanId: String) {
        viewModelScope.launch {
            try {
                orphanRepository.updateOrphanStatus(orphanId, "Declined")
            } catch (e: Exception) {
                Log.e("EnrollmentViewModel", "Error declining orphan", e)
                // Optionally expose an error state to the UI
            }
        }
    }
}

class EnrollmentViewModelFactory(private val orphanRepository: OrphanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnrollmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnrollmentViewModel(orphanRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
