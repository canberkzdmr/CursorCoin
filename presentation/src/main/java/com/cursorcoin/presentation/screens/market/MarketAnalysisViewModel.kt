package com.cursorcoin.presentation.screens.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursorcoin.domain.model.MarketData
import com.cursorcoin.domain.usecase.GetMarketDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketAnalysisViewModel @Inject constructor(
    private val getMarketDataUseCase: GetMarketDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MarketAnalysisState())
    val state = _state.asStateFlow()

    init {
        loadMarketData()
    }

    fun loadMarketData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                getMarketDataUseCase().collect { marketData ->
                    _state.update { 
                        it.copy(
                            marketData = marketData,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}

data class MarketAnalysisState(
    val marketData: MarketData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) 