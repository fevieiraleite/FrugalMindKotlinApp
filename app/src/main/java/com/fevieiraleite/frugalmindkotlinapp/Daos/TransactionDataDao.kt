package com.fevieiraleite.frugalmindkotlinapp.Daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.fevieiraleite.frugalmindkotlinapp.Entities.Transaction

@Dao
interface TransactionDataDao {
    @Query("SELECT * from `transaction` ORDER BY date DESC")
    fun getAll(): LiveData<List<Transaction>>

    @Insert(onConflict = REPLACE)
    fun insert(transaction: Transaction)

    @Query("DELETE from `transaction` WHERE id = :id")
    fun deleteTransaction(id: Int)
}