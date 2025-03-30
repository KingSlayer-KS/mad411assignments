package com.example.mad411assignments

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object File {
    private const val FILE = "expense.json"

    fun saveExpense(context: Context, expenses: List<ExpenseRecord>) {
        val json = Gson().toJson(expenses)
        context.openFileOutput(FILE, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    fun loadExpense(context: Context): MutableList<ExpenseRecord> {
        return try {
            val json = context.openFileInput(FILE).bufferedReader().use { it.readText() }
            val type = object : TypeToken<MutableList<ExpenseRecord>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
//            mutableListOf()
            mutableListOf()
        }
    }
}
