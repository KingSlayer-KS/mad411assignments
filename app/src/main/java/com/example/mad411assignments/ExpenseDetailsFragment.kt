package com.example.mad411assignments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ExpenseDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_details, container, false)
        val txtbox = view.findViewById<TextView>(R.id.textExpenseDetails)
        val name = arguments?.getString("name")
        val amount = arguments?.getString("amount")
        val date = arguments?.getString("date")
        val cc = arguments?.getString("cc")
        Log.d("CurrencyDebug", "Converted cost: $cc")

        txtbox.text = "Name: $name\nAmount: $amount\nDate: $date  \ncc: $cc"
        return view
    }
}