package com.android.banquetmanager.utils

import java.text.NumberFormat
import java.util.Locale

object UtilityMethods {
    fun formatAmount(amount: Double): String{
        return NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(amount)
    }
}