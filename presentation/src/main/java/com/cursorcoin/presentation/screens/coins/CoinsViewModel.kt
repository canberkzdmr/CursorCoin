package com.cursorcoin.presentation.screens.coins

import com.cursorcoin.core.BaseViewModel
import com.cursorcoin.domain.model.Coin
import com.cursorcoin.domain.usecase.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : BaseViewModel<CoinsState, CoinsEvent>() {

    override fun createInitialState(): CoinsState = CoinsState()

    override fun handleEvent(event: CoinsEvent) {
        when (event) {
            is CoinsEvent.LoadCoins -> loadCoins(false)
            is CoinsEvent.RefreshCoins -> loadCoins(true)
        }
    }

    private fun loadCoins(forceRefresh: Boolean) {
        launch {
            getCoinsUseCase(forceRefresh)
                .onStart { setState { copy(isLoading = true, error = null) } }
                .catch { error -> setState { copy(isLoading = false, error = error.message) } }
                .collect { result ->
                    setState {
                        copy(
                            coins = result.data ?: emptyList(),
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
        }
    }
}

data class CoinsState(
    val coins: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CoinsEvent {
    object LoadCoins : CoinsEvent()
    object RefreshCoins : CoinsEvent()
} 