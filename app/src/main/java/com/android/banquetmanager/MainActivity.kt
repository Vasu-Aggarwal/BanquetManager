package com.android.banquetmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.banquetmanager.data.repository.BookingRepository
import com.android.banquetmanager.ui.screen.App
import com.android.banquetmanager.ui.theme.BanquetManagerTheme
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bookingRepository: BookingRepository

    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BanquetManagerTheme {
                App()
            }
        }
    }
}

