package com.example.testapp.data

import arrow.core.Either
import arrow.core.right
import com.example.testapp.data.database.TestDatabase
import com.example.testapp.data.network.DataWrapper
import com.example.testapp.data.network.TestApi
import com.example.testapp.data.network.response.CakeResponse
import com.example.testapp.data.network.response.Error
import javax.inject.Inject

class CakeRepository @Inject constructor(
    private val testNetwork: TestApi,
    private val TestDatabase: TestDatabase
) {

    suspend fun requestCakesList(onLoadCallback: suspend ((Either<Error, DataWrapper<CakeResponse>>) -> Unit)) {
        repositoryTreatment(onLoadCallback = onLoadCallback,
            loadLocal = {
                return@repositoryTreatment CakeResponse().apply {
                    addAll(it.cakeDao().getCakes())
                }
            }, loadRemote = {
                return@repositoryTreatment it.getCakeList()
            }, onCacheFromRemote = { data, database ->
                database.cakeDao().run {
                    clearTable()
                    setCakes(users = data)
                }
            })
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private suspend fun <T> repositoryTreatment(
        loadRemote: suspend ((testNetwork: TestApi) -> Either<Error, T>),
        loadLocal: suspend ((TestDatabase: TestDatabase) -> T?),
        onCacheFromRemote: suspend ((T, TestDatabase: TestDatabase) -> Unit),
        onLoadCallback: suspend (Either<Error, DataWrapper<T>>) -> Unit
    ) {
        val localData = loadLocal.invoke(TestDatabase)
        localData?.let {
            onLoadCallback.invoke(wrapLocalData(localData, false))
        }


        val remoteData = loadRemote.invoke(testNetwork)

        remoteData.fold(ifLeft = {
            val remoteDataWrapper = wrapRemoteData(remoteData)
            onLoadCallback.invoke(remoteDataWrapper)
        }, ifRight = {
            onCacheFromRemote.invoke(it, TestDatabase)

            loadLocal.invoke(TestDatabase)?.let {
                onLoadCallback.invoke(wrapLocalData(it, true))
            }

        })


    }

    private fun <T> wrapLocalData(data: T, isFromRemote: Boolean): Either<Error, DataWrapper<T>> {
        return Either.Right(DataWrapper(isFromRemote, data))
    }

    private fun <T> wrapRemoteData(data: Either<Error, T>): Either<Error, DataWrapper<T>> {
        return data.fold(ifLeft = {
            Either.Left(it)
        }, ifRight = {
            Either.Right(DataWrapper(isFromRemote = true, data = it))
        })
    }
}