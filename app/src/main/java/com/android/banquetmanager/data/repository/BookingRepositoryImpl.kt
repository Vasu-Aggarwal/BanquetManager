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
            // Fetch document with a specific ID
            val documentSnapshot = firebaseFirestore.collection("event")
                .document("4pZigHtanBW1k7MFQG72") // Specify the document ID here
                .get()
                .await() // Use await to suspend until the result is available

            Log.d("Booking date I found:", "getBookingsByDate: ${documentSnapshot.data}")

            // If the document exists, convert it to an Event object
            if (documentSnapshot.exists()) {
                listOf(documentSnapshot.toObject(Event::class.java)!!) // Convert to Event and return as list
            } else {
                Log.d("Document not found", "No document exists with the given ID.")
                emptyList() // Return empty list if document doesn't exist
            }
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