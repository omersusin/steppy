package com.omersusin.steppy.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.omersusin.steppy.SteppyApplication
import com.omersusin.steppy.core.data.repository.DayRepositoryImpl
import com.omersusin.steppy.core.domain.usecase.DayUseCases
import com.omersusin.steppy.settings.data.repository.SettingsRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class SummaryUiState(
    val steps: Int = 0,
    val goal: Int = 0,
    val distanceKm: Double = 0.0,
    val calories: Int = 0,
) {
    val progressPercent: Int
        get() = if (goal == 0) 0 else (steps * 100 / goal).coerceAtMost(100)
    val hasGoal: Boolean get() = goal > 0
}

class SummaryViewModel(application: SteppyApplication) : ViewModel() {

    private val dayUseCases = DayUseCases(
        dayRepository = DayRepositoryImpl(application.steppyDatabase.dayDao),
        settingsRepository = SettingsRepositoryImpl(application.settingsStore),
    )

    val uiState: StateFlow<SummaryUiState> =
        dayUseCases.getDay(LocalDate.now())
            .map { day ->
                SummaryUiState(
                    steps = day.steps,
                    goal = day.goal,
                    distanceKm = day.distanceTravelled,
                    calories = day.calorieBurned.toInt(),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SummaryUiState(),
            )

    companion object {
        fun factory(application: SteppyApplication) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SummaryViewModel(application) as T
        }
    }
}
