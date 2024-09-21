package com.android.banquetmanager.utils

enum class BanquetLocations(val displayName: String) {
    SK_EASTEND("SK Eastend"),
    RAJWADA("Rajwada");

    companion object {
        fun fromName(name: String): BanquetLocations? {
            return entries.find { it.name == name }
        }
    }
}
