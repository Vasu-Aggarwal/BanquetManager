package com.android.banquetmanager.utils

enum class Menu(val displayName: String) {
    DIAMOND("DIAMOND"),
    Gold("GOLD"),
    GOLD_PLUS("GOLD+"),
    SILVER("SILVER"),
    SILVER_PLUS("SILVER+");
    companion object {
        fun fromName(name: String): Menu? {
            return Menu.entries.find { it.name == name }
        }
    }
}