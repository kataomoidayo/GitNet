package com.putu.gitnet.retrofit

import com.putu.gitnet.BuildConfig
import com.putu.gitnet.model.ResponseModel
import com.putu.gitnet.model.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    @Headers("Authorization: token $thisisKey", "UserResponse-Agent: request")
    fun getUserList(): Call<List<UserModel>>

    @GET("search/users")
    @Headers("Authorization: token $thisisKey", "UserResponse-Agent: request")
    fun getUser(@Query("q") username: String): Call<ResponseModel>

    @GET("users/{username}")
    @Headers("Authorization: token $thisisKey", "UserResponse-Agent: request")
    fun getUserDetail(@Path("username") username: String): Call<UserModel>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $thisisKey", "UserResponse-Agent: request")
    fun getUserFollowers(@Path("username") username: String): Call<List<UserModel>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $thisisKey", "UserResponse-Agent: request")
    fun getUserFollowing(@Path("username") username: String): Call<List<UserModel>>

    companion object{
        private const val thisisKey = BuildConfig.API_KEY
    }
}