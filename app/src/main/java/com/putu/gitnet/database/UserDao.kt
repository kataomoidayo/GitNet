package com.putu.gitnet.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUserToFavorite(userEntity: UserEntity)

    @Query("DELETE FROM favorite_users WHERE favorite_users.id = :id")
    fun deleteUserFromFavorite(id : Int?)

    @Query("SELECT * FROM favorite_users WHERE favorite_users.login = :login")
    fun checkFavoriteStatus(login : String?): LiveData<List<UserEntity>>

    @Query("SELECT * FROM favorite_users ORDER BY id")
    fun getAllFavoriteUser(): LiveData<List<UserEntity>>

}