package com.android.banquetmanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : UserRepository {

    override suspend fun setUserPermissions(uid: String, permissions: Map<String, Boolean>): Boolean {
        return try {
            firebaseFirestore.collection("user").document(uid)
                .update("permissions", permissions)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUserPermissions(uid: String): Pair<Map<String, Boolean>?, String?> {
        return try {
            val document = firebaseFirestore.collection("user").document(uid).get().await()
            if (document.exists()) {
                val role = document.getString("role")
                val permissions = document.get("permissions") as? Map<String, Boolean>
                Pair(permissions, role)
            } else {
                Pair(null, null)
            }
        } catch (e: Exception) {
            Pair(null, null)
        }
    }
}
