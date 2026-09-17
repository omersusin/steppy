package com.omersusin.steppy.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.omersusin.steppy.SteppyApplication
import com.omersusin.steppy.core.data.repository.DayRepositoryImpl
import com.omersusin.steppy.settings.data.repository.SettingsRepositoryImpl
import com.omersusin.steppy.settings.domain.model.Settings
import com.omersusin.steppy.settings.domain.usecase.SettingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val dailyGoal: Int = 0,
    val stepLength: Int = 0,
    val height: Int = 0,
    val weight: Int = 0,
    val pace: Double = 1.0,
    val saved: Boolean = false,
)

class SettingsViewModel(application: SteppyApplication) : ViewModel() {

    private val useCases = SettingsUseCases(
        settingsRepository = SettingsRepositoryImpl(application.settingsStore),
        dayRepository = DayRepositoryImpl(application.steppyDatabase.dayDao),
    )

    private val saved = MutableStateFlow(false)

    val uiState: StateFlow<SettingsUiState> =
        combine(useCases.getSettings(), saved) { settings, isSaved ->
            SettingsUiState(
                dailyGoal = settings.dailyGoal,
                stepLength = settings.stepLength,
                height = settings.height,
                weight = settings.weight,
                pace = settings.pace,
                saved = saved,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState(),
            )

    fun save(dailyGoal: Int, stepLength: Int, height: Int, weight: Int) {
        viewModelScope.launch {
            saved.value = false
            useCases.updateSettings(
                Settings(
                    dailyGoal = dailyGoal,
                    stepLength = stepLength,
                    height = height,
                    weight = weight,
                    pace = 1.0,
                )
            )
            saved.value = true
        }
    }

    companion object {
        fun factory(application: SteppyApplication) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SettingsViewModel(application) as T
        }
    }
}
