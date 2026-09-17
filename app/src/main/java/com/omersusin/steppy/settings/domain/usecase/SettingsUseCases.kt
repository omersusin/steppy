package com.omersusin.steppy.settings.domain.usecase

import com.omersusin.steppy.core.domain.model.Day
import com.omersusin.steppy.core.domain.model.DaySettings
import com.omersusin.steppy.core.domain.model.of
import com.omersusin.steppy.core.domain.repository.DayRepository
import com.omersusin.steppy.settings.domain.model.Settings
import com.omersusin.steppy.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class SettingsUseCases(
    private val settingsRepository: SettingsRepository,
    private val dayRepository: DayRepository
) {

    fun getSettings(): Flow<Settings> = settingsRepository.getSettings()

    suspend fun updateSettings(settings: Settings) {
        val today = LocalDate.now()
        val existing = dayRepository.getDay(today).firstOrNull()
        val daySettings = DaySettings(
            date = today,
            goal = settings.dailyGoal,
            height = settings.height,
            weight = settings.weight,
            stepLength = settings.stepLength,
            pace = settings.pace
        )
        if (existing == null) {
            dayRepository.upsertDay(Day.of(today, settings, steps = 0))
        } else {
            dayRepository.updateDaySettings(daySettings)
        }
    }
}
