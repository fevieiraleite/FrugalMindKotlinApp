package com.fevieiraleite.frugalmindkotlinapp.Repositories

import android.arch.lifecycle.LiveData
import android.content.Context
import android.os.AsyncTask
import com.fevieiraleite.frugalmindkotlinapp.Daos.TransactionDataDao
import com.fevieiraleite.frugalmindkotlinapp.Entities.Transaction
import com.fevieiraleite.frugalmindkotlinapp.RomDatabase.FrugalMindDatabase

class TransactionRepository (val _context: Context) {
    private lateinit var transactionDao: TransactionDataDao
    private lateinit var allTransactions: LiveData<List<Transaction>>

    init {
        val context = _context
        val db = FrugalMindDatabase.getInstance(context)
        if (db != null) {
            this.transactionDao = db.transactionDataDao()
            this.allTransactions = transactionDao.getAll()
        }

    }


    fun getAllTransactions() : LiveData<List<Transaction>> {
        return allTransactions
    }

    fun insert(transaction: Transaction) {
        InsertAsyncTask(transactionDao).execute(transaction)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: TransactionDataDao) :
        AsyncTask<Transaction, Void, Void>() {

        override fun doInBackground(vararg params: Transaction): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }
}