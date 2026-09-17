package com.omersusin.steppy.settings.domain.repository

import com.omersusin.steppy.settings.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateSettings(settings: Settings)
}
