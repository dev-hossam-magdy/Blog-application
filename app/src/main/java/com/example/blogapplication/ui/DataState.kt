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

}


//sealed class DataState<T>(
//    val error: Event<StateError>? = null,
//    val loading: com.example.blogapplication.ui.Loading = Loading(
//        false
//    ),
//    var data: com.example.blogapplication.ui.Data<T>? = null
//) {
//
//    class Error<T>(response:Response):DataState<T>(error = Event(StateError(response)))
//
//    class Loading<T>(isLoading:Boolean,cachedData:T?):DataState<T>(
//        loading = Loading(isLoading),
//        data = Data(
//            Event.dataEvent(cachedData),
//            null
//        )
//    )
//    class Data<T>(data:T?,response: Response):DataState<T>(
//        data = Data(
//            Event.dataEvent(data),
//            Event.responseEvent(response)
//        )
//    )
//
//}