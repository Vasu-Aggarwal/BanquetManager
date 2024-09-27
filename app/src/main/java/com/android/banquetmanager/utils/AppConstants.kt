package com.android.banquetmanager.utils

object AppConstants {
    const val NORMAL_TEXT = 12
    const val SUBHEADING_TEXT = 14
    const val HEADING_TEXT = 16
    const val SHARED_PREF_KEY = "secure_prefs"
    const val SHARED_PREF_USER_PIN_KEY = "user_pin"
    const val APP_PERMISSIONS = "app_permissions"
    const val CAN_ADD_USERS = "canAddUsers"
    const val CAN_CHECK_PRICES = "canCheckPrices"
    const val CAN_READ_DATA = "canReadData"
    const val CAN_WRITE_DATA = "canWriteData"
    const val ROLE_NORMAL = "normal"  //Can read data
    const val ROLE_ADMIN = "admin"    //Can read, write, check balances
    const val ROLE_SUPER_ADMIN = "superAdmin" //Can read, write, check balances, Add other users and set permission
}