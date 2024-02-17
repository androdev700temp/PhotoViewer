package com.andro.photoviewer.common.domain

import kotlinx.coroutines.runBlocking

abstract class ResultUseCase<in P, out R> {

    suspend fun trySync(params: P): R? {
        return runCatching {
            doWork(params)
        }.getOrNull()
    }

    suspend fun executeSync(params: P): R = doWork(params)

    fun blockingTrySync(params: P): R? = runBlocking {
        trySync(params)
    }

    protected abstract suspend fun doWork(params: P): R
}