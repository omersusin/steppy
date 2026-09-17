package com.omersusin.steppy.core.domain.usecase

import com.omersusin.steppy.core.domain.repository.DayRepository
import com.omersusin.steppy.settings.domain.repository.SettingsRepository

class DayUseCases(
    dayRepository: DayRepository,
    settingsRepository: SettingsRepository
) {

    val getDay: GetDay = GetDayImpl(dayRepository, settingsRepository)
    val incrementStepCount: IncrementStepCount = IncrementStepCountImpl(dayRepository, getDay)
}
