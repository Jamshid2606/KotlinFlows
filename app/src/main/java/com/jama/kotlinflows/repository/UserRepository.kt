package com.jama.kotlinflows.repository

import com.jama.kotlinflows.model.GithubUser
import com.jama.kotlinflows.networking.ApiService
import kotlinx.coroutines.flow.flow

class UserRepository(private val apiService: ApiService) {
    fun getGithubUsers() = apiService.getUsers()
}