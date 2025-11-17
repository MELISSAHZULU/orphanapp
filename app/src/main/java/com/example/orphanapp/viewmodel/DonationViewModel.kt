package com.example.orphanapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.Donation
import com.example.orphanapp.repository.DonationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DonationViewModel(private val repository: DonationRepository) : ViewModel() {

    val donations: StateFlow<List<Donation>> = repository.getDonations()
        .catch { exception ->
            Log.e("DonationViewModel", "Error getting donations", exception)
            emit(emptyList()) // Emit an empty list on error
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addDonation(donation: Donation) {
        viewModelScope.launch {
            try {
                repository.addDonation(donation)
            } catch (e: Exception) {
                Log.e("DonationViewModel", "Failed to add donation", e)
            }
        }
    }
}

class DonationViewModelFactory(private val repository: DonationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DonationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DonationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
