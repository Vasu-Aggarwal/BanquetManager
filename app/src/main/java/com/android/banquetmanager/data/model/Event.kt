package com.android.banquetmanager.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Event(
    val eventId: String = "",
    val banquetLocation: String = "",
    val cocktail: Boolean = false,
    val cocktailAmount: Double = 0.0,
    val createdOn: Timestamp = Timestamp.now(),
    val dj: Boolean = false,
    val fruit: Boolean = false,
    val fruitAmount: Double = 0.0,
    val threeSixty: Boolean = false,
    val threeSixtyAmount: Double = 0.0,
    val extraEvents: Boolean = false,
    val extraEventsAmount: Double = 0.0,
    val extraPlate: Long = 0,
    val flower: Boolean = false,
    val flowerAmount: Double = 0.0,
    val foodType: String = "",
    val functionType: String = "",
    val menu: String? = "",
    val packageAmount: Double = 0.0,
    val pax: Long = 0,
    val status: Long = 0,
    val dateBooked: String = "",
    val lunch: Boolean = false,
    val dinner: Boolean = false,
    val balance: Long = 0,
    val month: Long = 0,
    val year: Long = 0,
    val paymentDetail: List<String> = emptyList()
)
