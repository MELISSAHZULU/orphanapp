package com.example.orphanapp.model

data class Orphan(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val birthCertificate: Boolean,
    var status: String = "Active"
)
