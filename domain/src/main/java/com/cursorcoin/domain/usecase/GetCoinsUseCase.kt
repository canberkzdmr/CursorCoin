package com.cursorcoin.domain.usecase

import com.cursorcoin.core.Resource
import com.cursorcoin.core.UseCase
import com.cursorcoin.domain.model.Coin
import com.cursorcoin.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository,
    @com.cursorcoin.core.di.IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Boolean, List<Coin>>(dispatcher) {

    override fun execute(parameters: Boolean): Flow<Resource<List<Coin>>> =
        repository.getCoins()
            .onStart { 
                if (parameters || repository.shouldRefreshCoins()) {
                    try {
                        repository.refreshCoins()
                    } catch (e: Exception) {
                        // If refresh fails, we'll still emit cached data
                        e.printStackTrace()
                    }
                }
            }
            .map<List<Coin>, Resource<List<Coin>>> { Resource.Success(it) }
            .catch { emit(Resource.Error(it.message ?: "An unexpected error occurred")) }
} 