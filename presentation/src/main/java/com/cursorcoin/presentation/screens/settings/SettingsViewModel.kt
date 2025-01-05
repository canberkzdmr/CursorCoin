package com.cursorcoin.presentation.screens.settings

import com.cursorcoin.core.BaseViewModel
import com.cursorcoin.domain.usecase.ManageSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val manageSettingsUseCase: ManageSettingsUseCase
) : BaseViewModel<SettingsState, SettingsEvent>() {

    override fun createInitialState(): SettingsState = SettingsState()

    init {
        loadSettings()
    }

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleUseLocalData -> toggleUseLocalData()
        }
    }

    private fun loadSettings() {
        launch {
            manageSettingsUseCase.getUseLocalData()
                .onStart { setState { copy(isLoading = true) } }
                .catch { error -> setState { copy(isLoading = false, error = error.message) } }
                .collect { useLocalData ->
                    setState {
                        copy(
                            useLocalData = useLocalData,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun toggleUseLocalData() {
        launch {
            try {
                manageSettingsUseCase.setUseLocalData(!state.value.useLocalData)
                setState { copy(useLocalData = !useLocalData) }
            } catch (e: Exception) {
                setState { copy(error = e.message) }
            }
        }
    }
}

data class SettingsState(
    val useLocalData: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class SettingsEvent {
    object ToggleUseLocalData : SettingsEvent()
} 