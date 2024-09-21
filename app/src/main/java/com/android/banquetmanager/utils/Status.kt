package com.android.banquetmanager.utils

enum class Status {
    BOOKED,
    AVAILABLE,
    HOLD,
    CANCELLED;
    companion object {
        fun fromName(name: String): Status? {
            return Status.entries.find { it.name == name }
        }
    }
}