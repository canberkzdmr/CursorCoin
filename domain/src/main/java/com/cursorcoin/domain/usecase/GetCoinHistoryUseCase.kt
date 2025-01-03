package com.cursorcoin.domain.usecase

import com.cursorcoin.core.Resource
import com.cursorcoin.core.UseCase
import com.cursorcoin.domain.model.CoinHistory
import com.cursorcoin.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetCoinHistoryUseCase @Inject constructor(
    private val repository: CoinRepository,
    @com.cursorcoin.core.di.IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<GetCoinHistoryUseCase.Params, List<CoinHistory>>(dispatcher) {

    override fun execute(parameters: Params): Flow<Resource<List<CoinHistory>>> =
        repository.getCoinHistory(parameters.coinId)
            .onStart { 
                if (parameters.forceRefresh || repository.shouldRefreshCoinHistory(parameters.coinId)) {
                    try {
                        repository.refreshCoinHistory(parameters.coinId)
                    } catch (e: Exception) {
                        // If refresh fails, we'll still emit cached data
                        e.printStackTrace()
                    }
                }
            }
            .map<List<CoinHistory>, Resource<List<CoinHistory>>> { Resource.Success(it) }
            .catch { emit(Resource.Error(it.message ?: "An unexpected error occurred")) }

    data class Params(
        val coinId: String,
        val forceRefresh: Boolean = false
    )
} 