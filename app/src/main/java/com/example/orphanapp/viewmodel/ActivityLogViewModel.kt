package com.example.orphanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.ActivityLog
import com.example.orphanapp.repository.ActivityLogRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ActivityLogViewModel(private val repository: ActivityLogRepository) : ViewModel() {

    val activityLogs: StateFlow<List<ActivityLog>> = repository.getActivityLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addActivityLog(description: String) {
        viewModelScope.launch {
            repository.addActivityLog(description)
        }
    }
}

class ActivityLogViewModelFactory(private val repository: ActivityLogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityLogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
