package com.fevieiraleite.frugalmindkotlinapp.Adapters

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fevieiraleite.frugalmindkotlinapp.Entities.Transaction
import com.fevieiraleite.frugalmindkotlinapp.R
import com.fevieiraleite.frugalmindkotlinapp.Utilities.SimpleDateString
import org.jetbrains.anko.backgroundColor
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class TransactionListAdapter : RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder>() {

    //class TransactionViewHolder(val recyclerItemTextView: View) : RecyclerView.ViewHolder(recyclerItemTextView.findViewById(R.id.recycler_textView))

    class TransactionViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionAmount: TextView
        val transactionDate: TextView
        val transactionType: TextView
        val constraintLayout: ConstraintLayout

        init {
            transactionAmount = itemView.findViewById(R.id.recycler_transactionAmount)
            transactionDate = itemView.findViewById(R.id.recycler_transactionDate)
            transactionType = itemView.findViewById(R.id.recycler_transactionType)
            constraintLayout = itemView.findViewById(R.id.recycler_constraintLayout)
        }
    }
    private var transactionList : List<Transaction>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val recyclerItemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false) as View

        //TODO set view's properties

        return TransactionViewHolder(recyclerItemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        if (transactionList != null) {
            val currentTransaction = transactionList!![position]
            holder.transactionAmount.text = "$" + currentTransaction.amount.toString()
            holder.transactionDate.text = currentTransaction.date!!.SimpleDateString()
            holder.transactionType.text = currentTransaction.type

            if (currentTransaction.amount > 0) {
                holder.constraintLayout.backgroundColor = Color.parseColor("#64ff66")
            } else {
                holder.constraintLayout.backgroundColor = Color.parseColor("#7ffc3636")
            }
        }
    }

    override fun getItemCount(): Int {
        return (if (transactionList != null) transactionList!!.size else 0)
    }

    fun setTransactions(transactions: List<Transaction>) {
        transactionList = transactions
        notifyDataSetChanged()
    }

}