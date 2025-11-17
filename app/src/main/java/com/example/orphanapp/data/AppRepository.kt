package com.example.orphanapp.data

import androidx.compose.runtime.mutableStateListOf

data class Donation(val id: Int, val donorName: String, val amount: String, val date: String)
data class StaffMember(val id: Int, val name: String, val role: String, var isActive: Boolean)

object AppRepository {
    val donations = mutableStateListOf<Donation>()
    val staff = mutableStateListOf<StaffMember>()

    // Function to add a new donation
    fun addDonation(donorName: String, amount: String, date: String) {
        donations.add(Donation(id = (donations.size + 1), donorName = donorName, amount = amount, date = date))
    }

    // Function to add a new staff member
    fun addStaff(name: String, role: String) {
        staff.add(StaffMember(id = (staff.size + 1), name = name, role = role, isActive = true))
    }
    
    // Function to get a staff member by ID
    fun getStaffMember(id: Int): StaffMember? {
        return staff.find { it.id == id }
    }

    // Function to update a staff member
    fun updateStaffMember(id: Int, newName: String, newRole: String, newStatus: Boolean) {
        val index = staff.indexOfFirst { it.id == id }
        if (index != -1) {
            staff[index] = staff[index].copy(name = newName, role = newRole, isActive = newStatus)
        }
    }
}
