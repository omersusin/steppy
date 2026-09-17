package com.omersusin.steppy.settings.data.repository

import com.omersusin.steppy.settings.data.source.SettingsStore
import com.omersusin.steppy.settings.domain.model.Settings
import com.omersusin.steppy.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsStore: SettingsStore
) : SettingsRepository {

    override fun getSettings() = settingsStore.getSettings()

    override suspend fun updateSettings(settings: Settings) {
        settingsStore.updateSettings(settings)
    }
}
