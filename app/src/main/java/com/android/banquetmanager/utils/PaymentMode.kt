package com.android.banquetmanager.utils

enum class PaymentMode(val displayName: String){
    CASH("Cash"),
    CHEQUE("Cheque"),
    UPI("UPI"),
    NEFT("NEFT");

    companion object {
        fun fromName(name: String): PaymentMode? {
            return PaymentMode.entries.find { it.name == name }
        }
    }
}
