package com.example.albertsonscodingchallenge.util

import java.text.NumberFormat
import java.util.Locale

fun Double.toFormattedPrice(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(this)
}