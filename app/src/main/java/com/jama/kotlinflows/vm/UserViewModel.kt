package com.jama.kotlinflows.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jama.kotlinflows.model.GithubUser
import com.jama.kotlinflows.networking.ApiClient
import com.jama.kotlinflows.networking.ApiService
import com.jama.kotlinflows.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserViewModel:ViewModel() {
    private val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
    private val userRepository = UserRepository(apiService)
    private val liveData = MutableLiveData<Result<List<GithubUser>?>>()
    private val stateFlow = MutableStateFlow<Result<List<GithubUser>>>(Result.success(emptyList()))
    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            userRepository.getGithubUsers()
                .catch {
                    liveData.postValue(Result.failure(it))
                }
                .filter {list->
                    list.size>10
                }
                .map {list->
                    list.subList(0,5)
                    val otherList = ArrayList<GithubUser>()
                    list.forEach {
                        if (it.id%2==0){
                            otherList.add(it)
                        }
                    }
                    otherList
                }
                .collect{
                    liveData.postValue(Result.success(it))
                    stateFlow.emit(Result.success(it))
                }

        }
    }
    fun getUserLiveData():LiveData<Result<List<GithubUser>?>>{
        return liveData
    }

    fun getUserStateFlow():StateFlow<Result<List<GithubUser>?>>{
        return stateFlow
    }
}