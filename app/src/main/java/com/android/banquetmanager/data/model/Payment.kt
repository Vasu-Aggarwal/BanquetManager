package com.android.banquetmanager.data.model

data class Payment(
    val payerName: String = "",
    val paymentMode: String = "",
    val amount: Double = 0.0,
    val paidToName: String = "", //for the case of UPI
    val paidToMobile: String = "", //for the case of UPI
    val paymentDescription: String = ""
)
