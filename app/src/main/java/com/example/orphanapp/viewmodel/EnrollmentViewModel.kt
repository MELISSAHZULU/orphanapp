package com.example.orphanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.model.Orphan
import com.example.orphanapp.repository.OrphanRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EnrollmentViewModel(private val repository: OrphanRepository) : ViewModel() {

    val orphans: StateFlow<List<Orphan>> = repository.getOrphans()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addOrphan(orphan: Orphan) {
        viewModelScope.launch {
            repository.addOrphan(orphan)
        }
    }

    fun updateOrphan(orphan: Orphan) {
        viewModelScope.launch {
            repository.updateOrphan(orphan)
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
