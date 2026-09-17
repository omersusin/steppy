package com.omersusin.steppy

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.room.Room
import com.omersusin.steppy.core.data.source.SteppyDatabase
import com.omersusin.steppy.settings.data.source.SettingsStore
import com.omersusin.steppy.settings.data.source.SettingsStoreImpl
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class SteppyApplication : Application() {

    lateinit var settingsStore: SettingsStore
    lateinit var steppyDatabase: SteppyDatabase

    val currentDate = MutableStateFlow<LocalDate>(LocalDate.now())

    override fun onCreate() {
        super.onCreate()

        registerMidnightTimer()

        val sharedPreferences = getSharedPreferences("steppy_settings", MODE_PRIVATE)
        settingsStore = SettingsStoreImpl(sharedPreferences)

        steppyDatabase = Room.databaseBuilder(
            applicationContext,
            SteppyDatabase::class.java,
            SteppyDatabase.DATABASE_NAME
        ).build()
    }

    private fun registerMidnightTimer() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
        registerReceiver(midnightBroadcastReceiver, intentFilter)
    }

    private val midnightBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val today = LocalDate.now()
            if (today != currentDate.value) {
                currentDate.value = today
            }
        }
    }
}
