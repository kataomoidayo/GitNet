package com.putu.gitnet.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class GitNetDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var FAVORITE_INSTANCE : GitNetDatabase? = null

        @JvmStatic
        fun getGitNetDatabase(context : Context): GitNetDatabase {
            if (FAVORITE_INSTANCE == null) {
                synchronized(GitNetDatabase::class.java) {
                    FAVORITE_INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GitNetDatabase::class.java,
                    "git_net_database"
                    ).build()
                }
            }
            return FAVORITE_INSTANCE as GitNetDatabase
        }
    }
}