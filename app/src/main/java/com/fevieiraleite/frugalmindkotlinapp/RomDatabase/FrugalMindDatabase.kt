package com.fevieiraleite.frugalmindkotlinapp.RomDatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.fevieiraleite.frugalmindkotlinapp.Daos.TransactionDataDao
import com.fevieiraleite.frugalmindkotlinapp.Entities.Transaction
import com.fevieiraleite.frugalmindkotlinapp.Utilities.Converters

@Database(entities = arrayOf(Transaction::class), version = 3)
@TypeConverters(Converters::class)
abstract class FrugalMindDatabase : RoomDatabase(){
    abstract fun transactionDataDao(): TransactionDataDao

    companion object {
        private var INSTANCE: FrugalMindDatabase? = null

        fun getInstance(context: Context): FrugalMindDatabase? {
            if (INSTANCE == null) {
                synchronized(FrugalMindDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FrugalMindDatabase::class.java, "frugalmind.db").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}