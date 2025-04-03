package com.example.mad411assignments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad411assignments.network.RetrofitClient
import kotlinx.coroutines.launch
import android.util.Log
import java.util.*

class ExpenseFormFragment : Fragment() {
    private lateinit var inputTitle: EditText
    private lateinit var inputCost: EditText
    private lateinit var btnSelectDate: Button
    private lateinit var btnSubmit: Button
    private lateinit var selectedDateText: TextView
    private lateinit var expenseList: RecyclerView

    private lateinit var switchConversion: Switch
    private lateinit var spinnerCurrency: Spinner
    private lateinit var textConvertedCost: TextView

    private lateinit var expenseListAdapter: ExpenseListAdapter
    private val expenseRecords = mutableListOf<ExpenseRecord>()
    private var selectedDate: String = "No date selected"

    private var selectedCurrency: String = "cad"

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

        switchConversion = view.findViewById(R.id.switchConversion)
        spinnerCurrency = view.findViewById(R.id.spinnerCurrency)
        textConvertedCost = view.findViewById(R.id.textConvertedCost)

        setupCurrencySpinner()

        expenseRecords.clear()
        expenseRecords.addAll(File.loadExpense(requireContext()))
        expenseListAdapter = ExpenseListAdapter(expenseRecords, onUpdate = {
            File.saveExpense(requireContext(), expenseRecords)
        }, onShowDetails = { expense ->
            val bundle = Bundle().apply {
                putString("name", expense.title)
                putString("amount", expense.price.toString())
                putString("date", expense.date)
                putString("cc",expense.convertedCost.toString())
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
            val conversionNeeded = switchConversion.isChecked


            if (title.isNotEmpty() && cost != null) {
                if (conversionNeeded && selectedCurrency != "cad") {
                    lifecycleScope.launch {
                        try {
                            val api = RetrofitClient.api
                            Log.d("CurrencyDebug", "API instance created: $api")

                            val response = api.getRates("cad")
                            Log.d("CurrencyDebug", "API response received: $response")

                            val rate = response.cad[selectedCurrency.lowercase()]
                            Log.d("CurrencyDebug", "API response received: $rate")
                            Log.d("CurrencyDebug", "Rate for ${selectedCurrency.lowercase()}: $rate")

                            val convertedCost = cost * (rate ?: 1.0)
                            Log.d("CurrencyDebug", "Converted cost: $convertedCost")

                            val record = ExpenseRecord(title, cost, selectedDate, selectedCurrency, convertedCost)
                            expenseRecords.add(record)
                            File.saveExpense(requireContext(), expenseRecords)
                            expenseListAdapter.notifyItemInserted(expenseRecords.size - 1)
                            textConvertedCost.text =convertedCost.toString()

                        } catch (e: Exception) {
                            Log.e("CurrencyError", "Conversion failed: ${e.message}", e)
                            Toast.makeText(requireContext(), "Conversion failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val record = ExpenseRecord(title, cost, selectedDate, "cad", cost)
                    expenseRecords.add(record)
                    File.saveExpense(requireContext(), expenseRecords)
                    expenseListAdapter.notifyItemInserted(expenseRecords.size - 1)
                    textConvertedCost.text = String.format("%.2f", cost)
                }

                inputTitle.text.clear()
                inputCost.text.clear()
            }
        }

        return view
    }

    private fun setupCurrencySpinner() {
        val currencies = listOf("cad", "usd", "eur", "inr")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = adapter
        spinnerCurrency.setSelection(0)

        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCurrency = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
