package com.android.banquetmanager.data.repository

import android.util.Log
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.model.Payment
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

    override suspend fun addBooking(event: Event, payments: List<Payment>) {
        try {
            // Step 1: Add the payments to the 'payment' collection and collect document IDs
            val paymentIds = mutableListOf<String>()
            for (payment in payments) {
                val paymentDocumentReference = firebaseFirestore.collection("payment")
                    .add(payment)
                    .await()

                val paymentDocumentId = paymentDocumentReference.id
                paymentIds.add(paymentDocumentId) // Collect the payment document IDs
            }

            // Step 2: Add the event to the 'event' collection
            val documentReference = firebaseFirestore.collection("event")
                .add(event)
                .await()

            val documentId = documentReference.id  // Get the document ID

            // Step 3: Update the event with the document ID and payment IDs
            firebaseFirestore.collection("event")
                .document(documentId)
                .update(
                    mapOf(
                        "eventId" to documentId,
                        "paymentDetail" to paymentIds // Add the payment document IDs as an array
                    )
                )
                .await()

            println("Document added with ID: $documentId and updated with payment details")

        } catch (e: Exception) {
            e.printStackTrace()  // Handle the exception appropriately
        }
    }

    override suspend fun getBookingByEventId(eventId: String): Event {
        return try {
            // Fetch document with the specific ID
            val documentSnapshot = firebaseFirestore.collection("event")
                .document(eventId) // Use the eventId passed to the method
                .get()
                .await() // Use await to suspend until the result is available

            Log.d("Booking event details:", "getBookingByEventId: ${documentSnapshot.data}")

            // If the document exists, convert it to an Event object
            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(Event::class.java) ?: throw NoSuchElementException("Event not found")
            } else {
                throw NoSuchElementException("No document exists with the given ID.")
            }
        } catch (e: Exception) {
            Log.e("Firestore Error", "Error getting document", e)
            // Handle exceptions by throwing a custom exception or returning a default event
            throw e // You may replace this with a custom exception if needed
        }
    }

}