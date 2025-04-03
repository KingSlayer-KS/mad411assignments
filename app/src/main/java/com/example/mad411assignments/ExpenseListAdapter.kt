package com.example.mad411assignments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ExpenseListAdapter(
    private val records: MutableList<ExpenseRecord>,
    private val onUpdate: () -> Unit,
    private val onShowDetails: (ExpenseRecord) -> Unit
) : RecyclerView.Adapter<ExpenseListAdapter.RecordViewHolder>() {

    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtExpenseTitle)
        val txtCost: TextView = itemView.findViewById(R.id.txtExpenseCost)
        val txtDate: TextView = itemView.findViewById(R.id.txtExpenseDate)
        val btnRemove: Button = itemView.findViewById(R.id.btnDeleteExpense)
        val btnShowDetails: Button = itemView.findViewById(R.id.btnShowDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val expense = records[position]
        holder.txtTitle.text = expense.title
        holder.txtCost.text = "$${expense.price}"
        holder.txtDate.text = expense.date
        Log.d("ghvvtugutugtv",expense.convertedCost.toString())

        holder.btnRemove.setOnClickListener {
            records.removeAt(position)
            notifyItemRemoved(position)
            onUpdate()
        }

        holder.btnShowDetails.setOnClickListener {
            onShowDetails(expense)
        }
    }

    override fun getItemCount() = records.size
}
