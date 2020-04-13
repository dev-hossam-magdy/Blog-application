package com.example.blogapplication.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Index
import com.example.blogapplication.models.AuthToken
import com.example.blogapplication.persistence.daos.AuthTokenDao
import com.example.blogapplication.util.AbsentLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class SessionManager @Inject constructor(
    private val authTokenDao: AuthTokenDao,
    private val application: Application
) {

    private val TAG = "SessionManager"

    private var _cachedToken: MutableLiveData<AuthToken> = MutableLiveData()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    fun login(newValue:AuthToken){
        Log.e(TAG,"login")
        setAuthTokenValue(newValue)
    }
    private val handler:CoroutineExceptionHandler = CoroutineExceptionHandler{_,throwable->
        throwable.message?.let { message->
            Log.e(TAG,"error in logout ${message}")
        }

    }

    fun logout(){
        Log.e(TAG,"logout")

        GlobalScope.launch(IO+handler){
            cachedToken.value!!.accountPk?.let {pk->
                authTokenDao.nullifyToken(pk)
                setAuthTokenValue(null)
            }
        }

    }

    fun setAuthTokenValue(newValue: AuthToken?) {

        GlobalScope.launch(Main) {
            if (_cachedToken.value == newValue)
                return@launch
            _cachedToken.value = newValue
        }
    }

    fun isConnectedToTheInternet():Boolean = try {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.activeNetworkInfo.isConnected
    }catch (e:Exception){
        Log.e(TAG,"isConnectedToTheInternet: ${e.message}")
        false
    }


}