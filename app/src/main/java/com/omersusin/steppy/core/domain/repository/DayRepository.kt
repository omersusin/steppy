package com.omersusin.steppy.core.domain.repository

import kotlinx.coroutines.flow.Flow
import com.omersusin.steppy.core.domain.model.Day
import com.omersusin.steppy.core.domain.model.DaySettings
import java.time.LocalDate

interface DayRepository {

    fun getFirstDay(): Flow<Day?>

    fun getDay(date: LocalDate): Flow<Day?>

    suspend fun getAllDays(): List<Day>

    fun getDays(range: ClosedRange<LocalDate>): Flow<List<Day>>

    suspend fun upsertDay(day: Day)

    suspend fun updateDaySettings(daySettings: DaySettings)
}
