package com.example.bicycles.models

data class Equipment(
    val _id: String,
    val name: String,
    val image: String?,
    val categorie: String,
    val description: String,
    val etat: String,
    val userId: String
)
