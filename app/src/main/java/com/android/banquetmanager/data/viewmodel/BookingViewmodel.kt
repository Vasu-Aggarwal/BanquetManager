package com.android.banquetmanager.data.viewmodel

import androidx.lifecycle.ViewModel
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.model.Payment
import com.android.banquetmanager.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookingViewmodel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {
    suspend fun getBookingsByDate(date: String): List<Event> {
        return bookingRepository.getBookingsByDate(date)
    }

    suspend fun getBookingByEventId(eventId: String): Event {
        return bookingRepository.getBookingByEventId(eventId)
    }

    suspend fun addBooking(event: Event, payments: List<Payment>) {
        return bookingRepository.addBooking(event, payments)
    }

    suspend fun getMonthlyBalancesByMonthYear(month: Int, year: Int): List<Event> {
        return bookingRepository.getMonthlyBalancesByMonthYear(month, year)
    }

    suspend fun getEventsByMonthAndYear(month: Int, year: Int): List<Event> {
        return bookingRepository.getEventsByMonthAndYear(month, year)
    }
}
