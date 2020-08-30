package com.chesire.nekome.core.extensions

import androidx.lifecycle.MutableLiveData
import com.chesire.nekome.core.flags.AsyncState

/**
 * Posts to the receiver with [AsyncState.Success].
 */
fun <T, E> MutableLiveData<AsyncState<T, E>>.postSuccess(data: T) {
    postValue(AsyncState.Success(data))
}

/**
 * Posts to the receiver with [AsyncState.Error].
 */
fun <T, E> MutableLiveData<AsyncState<T, E>>.postError(error: E) {
    postValue(AsyncState.Error(error))
}

/**
 * Posts to the receiver with [AsyncState.Error] containing a data packet of [data].
 */
fun <T, E> MutableLiveData<AsyncState<T, E>>.postError(data: T, error: E) {
    postValue(AsyncState.Error(data, error))
}

/**
 * Posts to the receiver with [AsyncState.Loading].
 */
fun <T, E> MutableLiveData<AsyncState<T, E>>.postLoading() {
    postValue(AsyncState.Loading())
}
