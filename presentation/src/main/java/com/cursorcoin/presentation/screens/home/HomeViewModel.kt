package com.cursorcoin.presentation.screens.home

import com.cursorcoin.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeState, HomeEvent>() {
    override fun createInitialState(): HomeState = HomeState()

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        // Implement data loading logic here
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: String? = null
)

sealed class HomeEvent {
    object LoadData : HomeEvent()
} 