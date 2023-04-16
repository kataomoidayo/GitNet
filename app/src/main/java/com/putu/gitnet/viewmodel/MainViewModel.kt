package com.putu.gitnet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.putu.gitnet.extra.Event
import com.putu.gitnet.model.ResponseModel
import com.putu.gitnet.model.UserModel
import com.putu.gitnet.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<UserModel>>()
    val listUser: LiveData<List<UserModel>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> =_isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        getUserList()
    }

    fun getUserList() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserList()
        client.enqueue(object : Callback<List<UserModel>> {
            override fun onResponse(call: Call<List<UserModel>>, response: Response<List<UserModel>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()
                    _snackbarText.value = Event("Success, User List Loaded")
                } else {
                    _snackbarText.value = Event("Error User List Cannot Be Loaded")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Error User List Cannot Be Loaded")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchUser(username: String) {
        if (username != "") {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getUser(username)
            client.enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        if (response.body()?.totalCount == 0) {
                            _snackbarText.value = Event("User Not Found")
                        }
                        _listUser.value = response.body()?.items
                    } else {
                        _snackbarText.value = Event("onFailure: ${response.message()}")
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    _isLoading.value = false
                    _snackbarText.value = Event("onFailure: ${t.message.toString()}")
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } else {
            getUserList()
        }
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}