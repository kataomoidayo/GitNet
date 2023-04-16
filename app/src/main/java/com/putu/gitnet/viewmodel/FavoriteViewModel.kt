package com.putu.gitnet.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.putu.gitnet.repository.UserRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val userRepository : UserRepository = UserRepository(application)

    fun getAllFavoriteUser() = userRepository.getAllFavoriteUser()

}