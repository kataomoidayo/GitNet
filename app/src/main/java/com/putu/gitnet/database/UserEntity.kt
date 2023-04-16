package com.putu.gitnet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")

class UserEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int? = null,

    @ColumnInfo("bio")
    val bio: String? = null,

    @ColumnInfo("login")
    val login: String? = null,

    @ColumnInfo("company")
    val company: String? = null,

    @ColumnInfo("public_repos")
    val publicRepos: Int? = null,

    @ColumnInfo("followers")
    val followers: Int? = null,

    @ColumnInfo("avatar_url")
    val avatarUrl: String? = null,

    @ColumnInfo("following")
    val following: Int? = null,

    @ColumnInfo("name")
    val name: String? = null,

    @ColumnInfo("html_url")
    val htmlUrl: String? = null

)