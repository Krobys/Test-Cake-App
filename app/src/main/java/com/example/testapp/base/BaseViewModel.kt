package com.example.testapp.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.testapp.data.network.response.Error

open class  BaseViewModel : ViewModel() {
    protected val disposable = CompositeDisposable()
    val errorLiveData: SingleLiveEvent<Error> = SingleLiveEvent()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun launchCoroutineScope(inputBlock: suspend ((coroutineScope: CoroutineScope)->Unit)): Job{
        return viewModelScope.launch(Dispatchers.IO) {
            inputBlock.invoke(this)
        }
    }
}