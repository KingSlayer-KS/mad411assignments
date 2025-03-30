package com.example.mad411assignments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import android.widget.TextView
import java.util.*
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var expenseListAdapter: ExpenseListAdapter
    private val expenseRecords = mutableListOf<ExpenseRecord>()
    private var selectedDate: String = "No date selected"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Lifecycle", "onCreate ")
        //header footer
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.headerContainer, HeaderFragment())
        transaction.replace(R.id.footerContainer, FooterFragment())
        transaction.commit()
        //variable declaration
        val fieldTitle = findViewById<TextInputEditText>(R.id.inputExpenseTitle)
        val fieldCost = findViewById<TextInputEditText>(R.id.inputCost)
        val buttonSubmit = findViewById<Button>(R.id.btnSubmitExpense)
        val buttonSelectDate = findViewById<Button>(R.id.btnSelectDate)
        val textSelectedDate = findViewById<TextView>(R.id.textSelectedDate)
        val recyclerView = findViewById<RecyclerView>(R.id.listExpenses)
        expenseRecords.addAll(File.loadExpense(this))
        //RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseListAdapter = ExpenseListAdapter(expenseRecords) {
            File.saveExpense(this, expenseRecords)
        }

        recyclerView.adapter = expenseListAdapter
        buttonSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, { _, year, month, day ->
                selectedDate = "$year-${month + 1}-$day"
                textSelectedDate.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        buttonSubmit.setOnClickListener {
            val title = fieldTitle.text.toString().trim()
            val cost = fieldCost.text.toString().toDoubleOrNull()

            if (title.isNotEmpty() && cost != null) {
                expenseRecords.add(ExpenseRecord(title, cost, selectedDate))
                expenseListAdapter.notifyItemInserted(expenseRecords.size - 1)
                fieldTitle.text?.clear()
                fieldCost.text?.clear()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy")
    }
}
