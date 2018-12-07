package com.fevieiraleite.frugalmindkotlinapp.Entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "transaction")
data class Transaction (@PrimaryKey(autoGenerate = true) var id: Long,
                        @ColumnInfo(name = "amount") var amount: Double,
                        @ColumnInfo(name = "date") var date: Date?,
                        @ColumnInfo(name = "type") var type: String?,
                        @ColumnInfo(name = "recurring") var recurring: Boolean,
                        @ColumnInfo(name = "recurring_period") var recurringPeriod: String?
){
    constructor():this(0, 0.0, null, null, false, null)

}