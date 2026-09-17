package com.omersusin.steppy.settings.data.source

import com.omersusin.steppy.settings.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsStore {

    fun getSettings(): Flow<Settings>
}
