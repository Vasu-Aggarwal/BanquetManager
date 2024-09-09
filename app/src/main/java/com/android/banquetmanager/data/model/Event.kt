package com.android.banquetmanager.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Event(
    val banquetLocation: String = "",
    val cocktail: Boolean = false,
    val cocktailAmount: Double = 0.0,
    val createdOn: Timestamp = Timestamp.now(),
    val dj: Boolean = false,
    val djAmount: Double = 0.0,
    val extraPlate: Int = 0,
    val flower: Boolean = false,
    val flowerAmount: Double = 0.0,
    val foodType: String = "",
    val functionType: String = "",
    val menu: String? = "",
    val packageAmount: Double = 0.0,
    val pax: Int = 0,
    val status: Int = 0,
    val dateBooked: String = ""
)
