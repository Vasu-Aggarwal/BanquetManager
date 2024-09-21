package com.android.banquetmanager.data.repository

import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.model.Payment
import com.google.firebase.Timestamp

interface BookingRepository {
    suspend fun getBookingsByDate(date: String): List<Event>
    suspend fun addBooking(event: Event, payments: List<Payment>)
    suspend fun getBookingByEventId(eventId: String): Event
    suspend fun getMonthlyBalancesByMonthYear(month: Int, year: Int): List<Event>
    suspend fun getEventsByMonthAndYear(month: Int, year: Int): List<Event>
    suspend fun getPaymentDetailsByEvent(paymentIds: List<String>): List<Payment>
}