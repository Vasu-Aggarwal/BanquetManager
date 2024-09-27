package com.android.banquetmanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore) : UserRepository {
}