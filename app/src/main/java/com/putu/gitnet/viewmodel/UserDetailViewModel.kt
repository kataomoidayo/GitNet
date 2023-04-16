package com.putu.gitnet.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.putu.gitnet.database.UserEntity
import com.putu.gitnet.extra.Event
import com.putu.gitnet.model.UserModel
import com.putu.gitnet.repository.UserRepository
import com.putu.gitnet.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel() {

    private val _userDetail = MutableLiveData<UserModel>()
    val userDetail : LiveData<UserModel> =_userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> =_isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val userRepository: UserRepository = UserRepository(application)


    fun getUserDetail(login: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(login)
        client.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _userDetail.value = response.body()
                    }
                } else {
                    _snackbarText.value = Event("Error User Data Cannot Be Loaded")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Error User Data Cannot Be Loaded")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun addUserToFavorite(userEntity: UserEntity) {
        userRepository.addUserToFavorite(userEntity)
    }

    fun deleteUserFromFavorite(id: Int?) {
        userRepository.deleteUserFromFavorite(id)
    }

    fun checkFavoriteStatus(login: String?) : LiveData<List<UserEntity>> = userRepository.checkFavoriteStatus(login)

    companion object {
        const val TAG = "User_Detail_View_Model"
    }
}