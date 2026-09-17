package com.omersusin.steppy.core.domain.usecase

import com.omersusin.steppy.core.domain.model.Day
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetDay {

    operator fun invoke(date: LocalDate): Flow<Day>
}
