package com.cursorcoin.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class UseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    operator fun invoke(parameters: P): Flow<Resource<R>> = execute(parameters)
        .catch { e -> emit(Resource.Error(e.message ?: "An unexpected error occurred")) }
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameters: P): Flow<Resource<R>>
}

abstract class NoParamUseCase<R>(private val coroutineDispatcher: CoroutineDispatcher) {
    operator fun invoke(): Flow<Resource<R>> = execute()
        .catch { e -> emit(Resource.Error(e.message ?: "An unexpected error occurred")) }
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(): Flow<Resource<R>>
} 