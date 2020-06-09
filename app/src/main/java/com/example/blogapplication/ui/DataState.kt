package com.example.blogapplication.ui


sealed class DataState<T>(
    val isLoading: Boolean = false,
    val data: Event<T>? = null,
    val response: Event<Response>? = null
) {

    class Error<T>(response: Response) : DataState<T>(response = Event.responseEvent(response))

    class Loading<T>(isLoading: Boolean, cachedData: T?) : DataState<T>(
        isLoading = isLoading,
        data = Event.dataEvent(cachedData)
    )

    class Data<T>(data: T?, response: Response?) : DataState<T>(
        data = Event.dataEvent(data),
        response = Event.responseEvent(response)
    )

    class None<T>(response: Response):DataState<T>(response =Event.responseEvent(response) )
}

