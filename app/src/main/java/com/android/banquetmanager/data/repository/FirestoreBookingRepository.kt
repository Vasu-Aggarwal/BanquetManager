package com.android.banquetmanager.data.repository

import android.media.metrics.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreBookingRepository: BookingRepository {
    private val db = FirebaseFirestore.getInstance()
    override suspend fun getBookingsByDate(date: Timestamp): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun addBooking(event: Event) {
        TODO("Not yet implemented")
    }
}