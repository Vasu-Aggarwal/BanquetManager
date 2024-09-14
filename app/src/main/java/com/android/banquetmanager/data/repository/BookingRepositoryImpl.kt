package com.android.banquetmanager.data.repository

import android.util.Log
import com.android.banquetmanager.data.model.Event
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : BookingRepository {
    override suspend fun getBookingsByDate(date: String): List<Event> {
        return try {
            // Fetch documents that match the specified date
            val querySnapshot = firebaseFirestore.collection("event")
                .whereEqualTo("dateBooked", date) // Adjust the field name if different
                .get()
                .await() // Use await to suspend until the result is available

            Log.d("Booking date found:", "getBookingsByDate: ${querySnapshot.documents}")

            // Convert the documents to Event objects
            val events = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Event::class.java) // Convert each document to Event
            }

            events // Return the list of events
        } catch (e: Exception) {
            Log.e("Firestore Error", "Error getting documents", e)
            emptyList() // Handle exceptions by returning an empty list
        }
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