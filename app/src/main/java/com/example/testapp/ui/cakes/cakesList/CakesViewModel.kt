package com.example.testapp.ui.cakes.cakesList

import com.example.testapp.base.BaseViewModel
import com.example.testapp.base.SingleLiveEvent
import com.example.testapp.data.CakeRepository
import com.example.testapp.data.network.DataWrapper
import com.example.testapp.data.network.response.CakeResponse
import com.example.testapp.data.network.response.Error
import javax.inject.Inject

class CakesViewModel @Inject constructor(
    private val testRepository: CakeRepository
) : BaseViewModel() {

    val cakesLiveData: SingleLiveEvent<DataWrapper<CakeResponse>> =
        SingleLiveEvent()

    fun requestCakes() {
        launchCoroutineScope {
            testRepository.requestCakesList {
                it.fold(ifLeft = { error ->
                    errorLiveData.postValue(error)
                }, ifRight = { dataResponseWrapper ->
                    val resultList = dataResponseWrapper.data
                        .asSequence()
                        .distinct()
                        .sortedBy { cake -> cake.title }
                    dataResponseWrapper.data = CakeResponse().apply { addAll(resultList) }
                    cakesLiveData.postValue(dataResponseWrapper)
                })
            }
        }
    }
}