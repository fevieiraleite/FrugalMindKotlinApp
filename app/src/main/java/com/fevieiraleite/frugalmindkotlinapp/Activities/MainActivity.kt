package com.fevieiraleite.frugalmindkotlinapp.Activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.fevieiraleite.frugalmindkotlinapp.Adapters.TransactionListAdapter
import com.fevieiraleite.frugalmindkotlinapp.Entities.Transaction
import com.fevieiraleite.frugalmindkotlinapp.R
import com.fevieiraleite.frugalmindkotlinapp.ViewModels.TransactionViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivityForResult
import android.widget.Toast
import android.content.Intent
import android.graphics.Color
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val NEW_TRANSACTION_ACTIVITY_REQUEST_CODE = 1
    lateinit var transactionViewModel: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.onClick {
            startActivityForResult<NewTransactionActivity>(NEW_TRANSACTION_ACTIVITY_REQUEST_CODE)
        }





        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)

        val adapter = TransactionListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        val transactionObserver = Observer<List<Transaction>> { newTransactionsList ->
            if (newTransactionsList != null) {
                val updatedList: List<Transaction> = updateTransactionListForRecurring(newTransactionsList)
                adapter.setTransactions(updatedList)
            }
        }
        transactionViewModel.allTransactions.observe(this, transactionObserver)
    }

    private fun updateTransactionListForRecurring(transactionList: List<Transaction>) : List<Transaction> {
        var updatedList = ArrayList<Transaction>()
        updatedList.addAll(transactionList)

        val recurringTransactions = transactionList.filter { it.recurring }
        recurringTransactions.forEach { transaction ->
            val transactionsUntilDate: List<Transaction> = getTransactionsUntilDateForRecurring(transaction)

            updatedList.addAll(transactionsUntilDate)
        }

        updateTotalBalance(updatedList)
        return updatedList.sortedByDescending { transaction -> transaction.date }
    }

    private fun updateTotalBalance(transactions : ArrayList<Transaction>) {
        var totalBalance = 0.0
        transactions.forEach {
            totalBalance += it.amount
        }

        if (totalBalance > 0) {
            total_balance_tv.textColor = Color.parseColor("#009118")
        } else {
            total_balance_tv.textColor = Color.parseColor("#bf0000")
        }

        total_balance_tv.text = "$" + "%.2f".format(totalBalance)
    }

    private fun getTransactionsUntilDateForRecurring(transaction: Transaction) : List<Transaction> {
        var listOfTransactionsUntilDate = ArrayList<Transaction>()

        var currentTransactionDate = transaction.date!!
        val currentDate = Calendar.getInstance().time

        currentTransactionDate = addRecurringPeriodToDate(currentTransactionDate, transaction.recurringPeriod!!)
        while (currentTransactionDate < currentDate) {
            val newTransaction = Transaction(0, transaction.amount, currentTransactionDate, transaction.type, transaction.recurring, transaction.recurringPeriod)
            listOfTransactionsUntilDate.add(newTransaction)

            currentTransactionDate = addRecurringPeriodToDate(currentTransactionDate, transaction.recurringPeriod!!)
        }

        return listOfTransactionsUntilDate
    }

    private fun addRecurringPeriodToDate(date: Date, recurringPeriod: String) : Date {
        when (recurringPeriod) {
            "Monthly" -> {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.MONTH, +1)
                return cal.time
            }
            "Bi-Weekly" -> {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.DATE, +14)
                return cal.time
            }
            "Weekly" -> {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.DATE, +7)
                return cal.time
            }
            "Daily" -> {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.DATE, +1)
                return cal.time
            }
            else -> {
                return Calendar.getInstance().time
            }
        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_TRANSACTION_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val amount = data.getDoubleExtra("TRANSACTION_REPLY_AMOUNT", 0.0)
            val type = data.getStringExtra("TRANSACTION_REPLY_TYPE")
            val date = Date()
            date.time = data.getLongExtra("TRANSACTION_REPLY_DATE", -1)
            val recurring = data.getBooleanExtra("TRANSACTION_REPLY_RECURRING", false)
            val recurringType = data.getStringExtra("TRANSACTION_REPLY_RECURRING_TYPE")


            val transaction = Transaction(0, amount, date, type, recurring, recurringType)
            transactionViewModel.insert(transaction)

        } else {
            toast("Something wrong happened.")
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
