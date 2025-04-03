package com.example.mad411assignments

data class ExpenseRecord(
    val title: String,
    val price: Double,
    val date: String,
    val currency: String = "CAD",
    val convertedCost: Double = price
)
