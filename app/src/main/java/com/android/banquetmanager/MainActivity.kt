package com.android.banquetmanager

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import com.android.banquetmanager.data.model.Event
import com.android.banquetmanager.data.repository.BookingRepository
import com.android.banquetmanager.ui.screen.CalendarScreen
import com.android.banquetmanager.ui.theme.BanquetManagerTheme
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalendarScreen()
//                    Column{
//                        Box(modifier = Modifier.padding(innerPadding)) {
//
//                            val event1 = Event(
//                                "Test",
//                                false,
//                                0.00,
//                                Timestamp.now(),
//                                false,
//                                0.00,
//                                0,
//                                false,
//                                0.00,
//                                "VEG",
//                                "Wedding",
//                                "GOLD",
//                                1000.0,
//                                10,
//                                1,
//                                ""
//                            )
//
//                            Button(onClick = {
//                                GlobalScope.launch {
//                                    bookingRepository.addBooking(
//                                        event1
//                                    )
//                                }
//                            }) {
//                                Text(text = "Click me")
//                            }
//                        }
//                    }
                }
            }
        }
    }
}

