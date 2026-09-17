package com.omersusin.steppy.settings.data.source

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.omersusin.steppy.settings.domain.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsStoreImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsStore, OnSharedPreferenceChangeListener {

    private val settings: MutableStateFlow<Settings>

    init {
        val parsedSettings = parseSettings(sharedPreferences)
        settings = MutableStateFlow(parsedSettings)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun getSettings(): Flow<Settings> {
        return settings.asStateFlow()
    }

    override fun updateSettings(settings: Settings) {
        sharedPreferences.edit()
            .putString("daily_goal", settings.dailyGoal.toString())
            .putString("step_length", settings.stepLength.toString())
            .putString("height", settings.height.toString())
            .putString("weight", settings.weight.toString())
            .putString("pace", settings.pace.toString())
            .apply()
    }

    private fun parseSettings(sharedPreferences: SharedPreferences): Settings =
        sharedPreferences.run {
            Settings(
                dailyGoal = getNumericString("daily_goal", 8000),
                stepLength = getNumericString("step_length", 72),
                height = getNumericString("height", 180),
                weight = getNumericString("weight", 70),
                pace = getNumericString("pace", 1.0)
            )
        }

    private fun SharedPreferences.getNumericString(key: String, defaultValue: Int): Int =
        getString(key, "")?.toIntOrNull() ?: defaultValue

    private fun SharedPreferences.getNumericString(key: String, defaultValue: Double): Double =
        getString(key, "")?.toDoubleOrNull() ?: defaultValue

    override fun onSharedPreferenceChanged(
        updatedSharedPreferences: SharedPreferences?,
        key: String?
    ) {
        settings.value = parseSettings(sharedPreferences)
    }
}
