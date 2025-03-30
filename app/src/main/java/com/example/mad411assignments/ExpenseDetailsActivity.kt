package com.example.mad411assignments

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        val expenseName = intent.getStringExtra("EXTRA_NAME") ?: "N/A"
        val expenseAmount = intent.getDoubleExtra("EXTRA_AMOUNT", 0.0)
        val expenseDate = intent.getStringExtra("EXTRA_DATE") ?: "N/A"

        findViewById<TextView>(R.id.textExpenseDetails).text =
            "Name: $expenseName\nAmount: $expenseAmount\nDate: $expenseDate"
    }
}
