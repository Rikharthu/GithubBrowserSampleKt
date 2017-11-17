package com.example.uberv.githubbrowsersamplekt.api

import android.arch.lifecycle.LiveData
import com.example.uberv.githubbrowsersamplekt.models.User
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): LiveData<ApiResponse<User>>
}