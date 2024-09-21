package com.android.banquetmanager.utils

enum class Status (val value: Int) {
    BOOKED(2),
    AVAILABLE(1),
    HOLD(3),
    CANCELLED(0);
    companion object {
        fun fromName(name: String): Status? {
            return Status.entries.find { it.name == name }
        }
    }
}