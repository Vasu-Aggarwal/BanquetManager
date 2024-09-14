package com.android.banquetmanager.data.viewmodel

import androidx.lifecycle.ViewModel
import com.android.banquetmanager.data.model.Event
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
}
