package com.putu.gitnet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.putu.gitnet.extra.Event
import com.putu.gitnet.model.UserModel
import com.putu.gitnet.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _followingList = MutableLiveData<List<UserModel>>()
    val following: LiveData<List<UserModel>> = _followingList

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getFollowing(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getUserFollowing(username).enqueue(object : Callback<List<UserModel>> {
            override fun onResponse(call: Call<List<UserModel>>, response: Response<List<UserModel>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followingList.value = response.body()
                } else {
                    _snackbarText.value = Event("Error Following List Cannot Be Loaded")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Error Following List Cannot Be Loaded")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "Following_View_Model"
    }
}