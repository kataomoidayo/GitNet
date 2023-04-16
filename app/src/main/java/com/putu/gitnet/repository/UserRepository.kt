package com.putu.gitnet.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.putu.gitnet.database.GitNetDatabase
import com.putu.gitnet.database.UserDao
import com.putu.gitnet.database.UserEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {

    private val userDao : UserDao
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = GitNetDatabase.getGitNetDatabase(application)
        userDao = db.userDao()
    }

    fun addUserToFavorite(userEntity: UserEntity) {
        executorService.execute {
            userDao.addUserToFavorite(userEntity)
        }
    }

    fun deleteUserFromFavorite(id: Int?) {
        executorService.execute {
            userDao.deleteUserFromFavorite(id)
        }
    }

    fun checkFavoriteStatus(login: String?) : LiveData<List<UserEntity>> = userDao.checkFavoriteStatus(login)

    fun getAllFavoriteUser() : LiveData<List<UserEntity>> = userDao.getAllFavoriteUser()

}