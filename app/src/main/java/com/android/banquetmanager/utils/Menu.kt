package com.android.banquetmanager.utils

enum class Menu(val displayName: String) {
    Gold("GOLD"),
    SILVER("SILVER");
    companion object {
        fun fromName(name: String): Menu? {
            return Menu.entries.find { it.name == name }
        }
    }
}