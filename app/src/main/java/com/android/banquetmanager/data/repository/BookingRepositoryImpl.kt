package com.android.banquetmanager.data.repository

import com.android.banquetmanager.data.model.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : BookingRepository {
     override suspend fun getBookingsByDate(date: String): List<Event> {
        TODO("Not yet implemented")
    }

    override suspend fun addBooking(event: Event) {
        try {
            firebaseFirestore.collection("event")
                .add(event)
                .await()
        } catch (e: Exception){

        }
    }
}