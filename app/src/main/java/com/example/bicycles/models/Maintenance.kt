package com.example.bicycles.models

import java.util.Date

data class Maintenance(
    val _id: String,
    val ID_Equipement: String,
    val Date_Maintenance: Date,
    val Type_Maintenance: String,
    val Description: String,
    val Coût_Maintenance: Double
)
