package com.android.banquetmanager.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EncryptedSharedPreferencesModule {

    private const val PREFS_FILE = "secure_prefs"
    private const val PIN_KEY = "user_pin"

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            PREFS_FILE,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    fun provideSavePin(sharedPreferences: SharedPreferences): (String) -> Unit {
        return { pin: String ->
            sharedPreferences.edit().putString(PIN_KEY, pin).apply()
        }
    }

    @Provides
    fun provideGetPin(sharedPreferences: SharedPreferences): () -> String? {
        return {
            sharedPreferences.getString(PIN_KEY, null)
        }
    }
}
