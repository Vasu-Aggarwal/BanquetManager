package com.android.banquetmanager.data.repository

interface UserRepository {
    suspend fun setUserPermissions(uid: String, permissions: Map<String, Boolean>): Boolean
    suspend fun getUserPermissions(uid: String): Pair<Map<String, Boolean>?, String?>
}
