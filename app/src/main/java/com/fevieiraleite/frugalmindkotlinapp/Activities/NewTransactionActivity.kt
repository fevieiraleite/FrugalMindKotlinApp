package com.fevieiraleite.frugalmindkotlinapp.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.fevieiraleite.frugalmindkotlinapp.R
import kotlinx.android.synthetic.main.activity_new_transaction.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.lang.Exception
import java.util.*
import android.widget.ArrayAdapter
import android.widget.CompoundButton






class NewTransactionActivity : AppCompatActivity() {
    var date = Date()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)
        setupButtonClicks()
        setupDatePicker()
        setupSpinner()
        setupRecurringSwitch()

        radio_expense.isChecked = true

    }

    fun setupButtonClicks() {
        saveBtn.onClick {
            var amount = etAmount.text.toString().toDoubleOrNull()
            val type = etType.text.toString()

            if (amount != null) {
                if (radio_expense.isChecked) amount *= -1
                val replyIntent = Intent()
                replyIntent.putExtra("TRANSACTION_REPLY_AMOUNT", amount)
                replyIntent.putExtra("TRANSACTION_REPLY_TYPE", type)
                replyIntent.putExtra("TRANSACTION_REPLY_DATE", date.time)
                replyIntent.putExtra("TRANSACTION_REPLY_RECURRING", new_transaction_recurring_switch.isChecked)
                replyIntent.putExtra("TRANSACTION_REPLY_RECURRING_TYPE", new_transaction_spinner.selectedItem.toString())

                setResult(RESULT_OK, replyIntent)
                finish()
            }
            else {
                longToast("Invalid Amount")
            }
        }
    }

    fun setupDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        etDate.setText("" + month + "/" + day + "/" + year)
        date = c.time


        etDate.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                etDate.setText("" + mMonth + "/" + mDay + "/" + mYear)
                c.set(mYear, mMonth, mDay)
                date = c.time
            }, year, month, day)

            dpd.show()
        }
    }

    fun setupSpinner() {
        new_transaction_spinner.visibility = View.INVISIBLE

        val items = arrayOf("Monthly", "Bi-Weekly", "Weekly", "Daily")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        new_transaction_spinner.adapter = adapter
    }

    fun setupRecurringSwitch() {
        new_transaction_recurring_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) new_transaction_spinner.visibility = View.VISIBLE else new_transaction_spinner.visibility = View.INVISIBLE
        }
    }
}
