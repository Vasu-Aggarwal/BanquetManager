package com.android.banquetmanager.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

object UtilityMethods {
    fun formatAmount(amount: Double): String{
        return NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(amount)
    }

    class PinVisualTransformation(private val mask: Char = '•') : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val maskedText = AnnotatedString(text.text.map { mask }.joinToString(""))
            return TransformedText(maskedText, OffsetMapping.Identity)
        }
    }
}