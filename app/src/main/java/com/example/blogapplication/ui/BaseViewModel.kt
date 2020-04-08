package com.example.blogapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<StateEvent, ViewState> : ViewModel() {

    protected var _stateEvent: MutableLiveData<StateEvent> = MutableLiveData()

    protected var _viewState: MutableLiveData<ViewState> = MutableLiveData()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dataState: LiveData<DataState<ViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handelStatEvent(stateEvent)
            }

        }
    fun setStateEvent(event:StateEvent) {
        _stateEvent.value = event
    }

    fun getCurrenViewStateOrNew():ViewState{
        val value = _viewState.value?.let { it }?:initNewState()
        return value
    }

    abstract fun initNewState(): ViewState

    abstract fun handelStatEvent(stateEvent: StateEvent): LiveData<DataState<ViewState>>


}


//abstract class BaseViewModel<StateEvent, ViewState> : ViewModel() {
//    private val TAG = "BaseViewModel"
//
//    protected val _stateEvent: MutableLiveData<StateEvent> = MutableLiveData()
//    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()
//
//    val viewState: MutableLiveData<ViewState>
//        get() = _viewState
//    val dataState:LiveData<DataState<ViewState>> = Transformations
//        .switchMap(_stateEvent){ stateEvent ->
//            stateEvent?.let {
//                handelStatEvent(stateEvent)
//            }
//
//        }
//
//    fun stateEvent(event: StateEvent){
//        _stateEvent.value = event
//    }
//
//    fun setCurrentStateOrNew():ViewState{
//        val value = viewState.value?.let { it }?: initNewViewState()
//        return value
//    }
//
//    abstract fun initNewViewState(): ViewState
//
//    abstract fun handelStatEvent(stateEvent: StateEvent):LiveData<DataState<ViewState>>
//
//}