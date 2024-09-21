package com.android.banquetmanager.utils

enum class FoodType(val displayName: String) {
    VEG("Vegetarian"),
    NON_VEG("Non Vegetarian");

    companion object {
        fun fromName(name: String): FoodType? {
            return FoodType.entries.find { it.name == name }
        }
    }
}