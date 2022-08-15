package com.example.testapp.base

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class SharedViewModel @Inject constructor() : BaseViewModel() {

    var sharedLiveData: MutableLiveData<Triple<Class<*>, Any, String>> = MutableLiveData()

    fun send(fragmentReceiver: Class<*>, data: Any, identifier: String) {
        clearLiveData()
        sharedLiveData.value = Triple(fragmentReceiver, data, identifier)
    }

    fun clearLiveData() {
        sharedLiveData = MutableLiveData()
    }
}