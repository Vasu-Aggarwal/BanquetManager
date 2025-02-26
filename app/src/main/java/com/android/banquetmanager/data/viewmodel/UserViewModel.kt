package com.android.banquetmanager.data.viewmodel

import androidx.lifecycle.ViewModel
import com.android.banquetmanager.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun setUserPermissions(uid: String, permissions: Map<String, Boolean>): Boolean {
        return userRepository.setUserPermissions(uid, permissions)
    }

    suspend fun getUserPermissions(uid: String): Pair<Map<String, Boolean>?, String?> {
        return userRepository.getUserPermissions(uid)
    }
}
