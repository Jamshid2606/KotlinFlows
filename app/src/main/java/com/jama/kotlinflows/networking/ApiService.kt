package com.jama.kotlinflows.networking

import com.jama.kotlinflows.model.GithubUser
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    fun getUsers(): Flow<List<GithubUser>>

}