package com.android.banquetmanager.utils

enum class SlotTime(val displayName: String) {
    LUNCH("Lunch"),
    DINNER("Dinner");
    companion object {
        fun fromName(name: String): SlotTime? {
            return SlotTime.entries.find { it.name == name }
        }
    }
}