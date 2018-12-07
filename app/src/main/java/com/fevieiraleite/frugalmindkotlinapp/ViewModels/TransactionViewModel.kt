package com.fevieiraleite.frugalmindkotlinapp.ViewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.fevieiraleite.frugalmindkotlinapp.Entities.Transaction
import com.fevieiraleite.frugalmindkotlinapp.Repositories.TransactionRepository

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private var mRepository: TransactionRepository

    var allTransactions: LiveData<List<Transaction>>

    init {
        mRepository = TransactionRepository(application)
        allTransactions = mRepository.getAllTransactions()
    }

    fun insert(transaction: Transaction) {
        mRepository.insert(transaction)
    }
}