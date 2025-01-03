package com.cursorcoin.presentation.screens.settings

import com.cursorcoin.core.BaseViewModel
import com.cursorcoin.domain.usecase.ManageSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val manageSettingsUseCase: ManageSettingsUseCase
) : BaseViewModel<SettingsState, SettingsEvent>() {

    override fun createInitialState(): SettingsState = SettingsState()

    init {
        launch {
            manageSettingsUseCase.getUseLocalData()
                .collect { useLocalData ->
                    setState { copy(useLocalData = useLocalData) }
                }
        }
    }

    override fun handleEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleUseLocalData -> toggleUseLocalData()
        }
    }

    private fun toggleUseLocalData() {
        launch {
            manageSettingsUseCase.setUseLocalData(!state.value.useLocalData)
        }
    }
}

data class SettingsState(
    val useLocalData: Boolean = false
)

sealed class SettingsEvent {
    object ToggleUseLocalData : SettingsEvent()
} 