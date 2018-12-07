package com.fevieiraleite.frugalmindkotlinapp.Utilities

import java.text.SimpleDateFormat
import java.util.*


fun Date.SimpleDateString(): String {
    val format = SimpleDateFormat("MM/dd/yyyy")
    return format.format(this)
}
