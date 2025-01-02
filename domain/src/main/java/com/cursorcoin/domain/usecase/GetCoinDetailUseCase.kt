package com.cursorcoin.domain.usecase

import com.cursorcoin.core.Resource
import com.cursorcoin.core.UseCase
import com.cursorcoin.domain.model.CoinDetail
import com.cursorcoin.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCoinDetailUseCase @Inject constructor(
    private val repository: CoinRepository,
    @com.cursorcoin.core.di.IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<String, CoinDetail>(dispatcher) {

    override fun execute(parameters: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading())
            val coin = repository.getCoinById(parameters)
            emit(Resource.Success(coin))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
} 