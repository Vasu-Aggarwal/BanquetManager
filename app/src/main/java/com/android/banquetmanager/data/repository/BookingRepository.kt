package com.android.banquetmanager.data.repository

import android.media.metrics.Event
import com.google.firebase.Timestamp

interface BookingRepository {
    suspend fun getBookingsByDate(date: Timestamp): List<Event>
    suspend fun addBooking(event: Event)
}