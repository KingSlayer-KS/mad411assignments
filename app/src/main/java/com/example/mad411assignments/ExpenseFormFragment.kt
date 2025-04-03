package com.example.mad411assignments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ExpenseFormFragment : Fragment() {
    private lateinit var inputTitle: EditText
    private lateinit var inputCost: EditText
    private lateinit var btnSelectDate: Button
    private lateinit var btnSubmit: Button
    private lateinit var selectedDateText: TextView
    private lateinit var expenseList: RecyclerView

    private lateinit var expenseListAdapter: ExpenseListAdapter
    private val expenseRecords = mutableListOf<ExpenseRecord>()
    private var selectedDate: String = "No date selected"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_form, container, false)

        inputTitle = view.findViewById(R.id.inputExpenseTitle)
        inputCost = view.findViewById(R.id.inputCost)
        btnSelectDate = view.findViewById(R.id.btnSelectDate)
        btnSubmit = view.findViewById(R.id.btnSubmitExpense)
        selectedDateText = view.findViewById(R.id.textSelectedDate)
        expenseList = view.findViewById(R.id.listExpenses)

        expenseRecords.addAll(File.loadExpense(requireContext()))

        expenseListAdapter = ExpenseListAdapter(expenseRecords, onUpdate = {
            File.saveExpense(requireContext(), expenseRecords)
        }, onShowDetails = { expense ->
            val bundle = Bundle().apply {
                putString("name", expense.title)
                putString("amount", expense.price.toString())
                putString("date", expense.date)
            }
            findNavController().navigate(R.id.expenseDetailsFragment, bundle)
        })

        expenseList.layoutManager = LinearLayoutManager(requireContext())
        expenseList.adapter = expenseListAdapter

        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                selectedDate = "$year-${month + 1}-$day"
                selectedDateText.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnSubmit.setOnClickListener {
            val title = inputTitle.text.toString().trim()
            val cost = inputCost.text.toString().toDoubleOrNull()

            if (title.isNotEmpty() && cost != null) {
                val record = ExpenseRecord(title, cost, selectedDate)
                expenseRecords.add(record)
                File.saveExpense(requireContext(), expenseRecords)
                expenseListAdapter.notifyItemInserted(expenseRecords.size - 1)
                inputTitle.text.clear()
                inputCost.text.clear()
            }
        }

        return view
    }
}
