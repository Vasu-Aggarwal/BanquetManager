package com.android.banquetmanager.data.repository

import android.media.metrics.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : BookingRepository {
     override suspend fun getBookingsByDate(date: Timestamp): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun addBooking(event: Event) {
        TODO("Not yet implemented")
    }
}