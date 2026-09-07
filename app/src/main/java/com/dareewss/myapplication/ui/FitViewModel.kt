package com.dareewss.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dareewss.myapplication.data.MacroLog
import com.dareewss.myapplication.data.UserProfile
import com.dareewss.myapplication.data.UserRepository
import com.dareewss.myapplication.data.computeMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FitUiState(
    val profile: UserProfile? = null,
    val log: MacroLog = MacroLog(),
    val completedWorkoutId: String? = null
) {
    val metrics get() = profile?.computeMetrics()
    val isSetup get() = profile != null
}

class FitViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository(application)

    private val _state = MutableStateFlow(
        FitUiState(
            profile = repo.loadProfile(),
            log = repo.loadTodayLog(),
            completedWorkoutId = repo.completedWorkoutId()
        )
    )
    val state: StateFlow<FitUiState> = _state.asStateFlow()

    fun saveProfile(profile: UserProfile) {
        repo.saveProfile(profile)
        _state.update { it.copy(profile = profile) }
    }

    fun logMeal(protein: Float, carbs: Float, fat: Float) {
        val next = _state.value.log.copy(
            proteinG = _state.value.log.proteinG + protein,
            carbsG = _state.value.log.carbsG + carbs,
            fatG = _state.value.log.fatG + fat
        )
        repo.saveLog(next)
        _state.update { it.copy(log = next) }
    }

    fun completeWorkout(id: String) {
        repo.completeWorkout(id)
        _state.update { it.copy(completedWorkoutId = id) }
    }

    fun editProfile() {
        repo.clearSetup()
        _state.update { it.copy(profile = null) }
    }
}
