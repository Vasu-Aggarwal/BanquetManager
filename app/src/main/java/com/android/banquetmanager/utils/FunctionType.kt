package com.android.banquetmanager.utils

enum class FunctionType(val displayName: String) {
    WEDDING("Wedding"),
    RING_CEREMONY("Ring Ceremony"),
    BIRTHDAY("Birthday"),
    GET_TOGETHER("Get Together");

    companion object {
        fun fromName(name: String): FunctionType? {
            return FunctionType.entries.find { it.name == name }
        }
    }
}