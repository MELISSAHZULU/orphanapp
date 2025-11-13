package com.example.orphanapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.orphanapp.model.Orphan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EnrollmentViewModel : ViewModel() {

    private val _orphans = MutableStateFlow<List<Orphan>>(emptyList())
    val orphans: StateFlow<List<Orphan>> = _orphans

    fun addOrphan(orphan: Orphan) {
        _orphans.value = _orphans.value + orphan
    }
}
