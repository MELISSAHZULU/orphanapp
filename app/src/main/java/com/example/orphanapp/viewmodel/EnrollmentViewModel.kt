package com.example.orphanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.Orphan
import com.example.orphanapp.repository.OrphanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EnrollmentViewModel(private val orphanRepository: OrphanRepository) : ViewModel() {

    private val _orphans = MutableStateFlow<List<Orphan>>(emptyList())
    val orphans: StateFlow<List<Orphan>> = _orphans.asStateFlow()

    private val _selectedOrphan = MutableStateFlow<Orphan?>(null)
    val selectedOrphan: StateFlow<Orphan?> = _selectedOrphan.asStateFlow()

    private val _newlyCreatedOrphanId = MutableStateFlow<String?>(null)
    val newlyCreatedOrphanId: StateFlow<String?> = _newlyCreatedOrphanId.asStateFlow()

    init {
        viewModelScope.launch {
            orphanRepository.getOrphans().collect { orphanList ->
                _orphans.value = orphanList
            }
        }
    }

    fun addOrphan(orphan: Orphan) {
        viewModelScope.launch {
            val newOrphanId = orphanRepository.addOrphan(orphan)
            _newlyCreatedOrphanId.value = newOrphanId
        }
    }

    fun onEnrollmentComplete() {
        _newlyCreatedOrphanId.value = null
    }

    fun getOrphanById(orphanId: String) {
        viewModelScope.launch {
            _selectedOrphan.value = orphanRepository.getOrphanById(orphanId)
        }
    }

    fun updateOrphan(orphan: Orphan) {
        viewModelScope.launch {
            orphanRepository.updateOrphan(orphan)
        }
    }

    fun acceptOrphan(orphanId: String) {
        viewModelScope.launch {
            orphanRepository.updateOrphanStatus(orphanId, "Enrolled")
        }
    }

    fun declineOrphan(orphanId: String) {
        viewModelScope.launch {
            orphanRepository.updateOrphanStatus(orphanId, "Declined")
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