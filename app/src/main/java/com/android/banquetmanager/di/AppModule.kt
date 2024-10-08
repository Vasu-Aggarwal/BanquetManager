package com.android.banquetmanager.di

import com.android.banquetmanager.data.repository.BookingRepository
import com.android.banquetmanager.data.repository.BookingRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideBookingRepository(firestore: FirebaseFirestore): BookingRepository {
        return BookingRepositoryImpl(firestore)
    }
}
