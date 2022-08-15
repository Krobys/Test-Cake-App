package com.example.testapp.base

import androidx.lifecycle.LifecycleOwner

interface SharedFragmentChild {

    val sharedParentActivity: SharedActivityParent?

    fun initReceiver(viewLifecycleOwner: LifecycleOwner){ // call in onViewCreated
        sharedParentActivity?.sharedViewModel?.sharedLiveData?.observe(viewLifecycleOwner, {
            getDataForReceiver(it)
        })
    }

    private fun getDataForReceiver(pairData: Triple<Class<*>, Any, String>) {
        if (pairData.first == this.javaClass) {
            sharedDataReceived(pairData.second, pairData.third)
        }
    }

    fun sendDataToReceiver(classReceiver: Class<*>, data: Any, identifier: String = "default") { //send shared data
        sharedParentActivity
            ?.sharedViewModel
            ?.send(classReceiver, data, identifier)
    }

    fun sharedDataReceived(data: Any, identifier: String) {} //override for receive shared data

    fun clearSharedData(){
        sharedParentActivity?.sharedViewModel?.clearLiveData()
    }

}