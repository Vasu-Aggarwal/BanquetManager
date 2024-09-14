package com.android.banquetmanager.data.repository

import com.android.banquetmanager.data.model.Event
import com.google.firebase.Timestamp

interface BookingRepository {
    suspend fun getBookingsByDate(date: String): List<Event>
    suspend fun addBooking(event: Event)
    suspend fun getBookingByEventId(eventId: String): Event
}