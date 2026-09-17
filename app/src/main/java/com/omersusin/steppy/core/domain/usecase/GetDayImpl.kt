package com.omersusin.steppy.core.domain.usecase

import com.omersusin.steppy.core.domain.model.Day
import com.omersusin.steppy.core.domain.model.of
import com.omersusin.steppy.core.domain.repository.DayRepository
import com.omersusin.steppy.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate

class GetDayImpl(
    private val dayRepository: DayRepository,
    private val settingsRepository: SettingsRepository,
) : GetDay {

    override fun invoke(date: LocalDate): Flow<Day> {
        val settingsFlow = settingsRepository.getSettings()
        val dayFlow = dayRepository.getDay(date)

        return settingsFlow.combine(dayFlow) { settings, day ->
            day ?: Day.of(date, settings, steps = 0)
        }
    }
}
