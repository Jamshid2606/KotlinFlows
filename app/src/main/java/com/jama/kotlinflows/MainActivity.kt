package com.jama.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jama.kotlinflows.databinding.ActivityMainBinding
import com.jama.kotlinflows.vm.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.getUserLiveData()
            .observe(this, Observer {
                when{
                    it.isFailure->{
                        Log.d("LIVEDATA", "onCreate: ${it.exceptionOrNull()}")
                    }
                    it.isSuccess->{
                        Log.d("LIVEDATA", "onCreate: ${it.getOrNull()}")
                    }
                }
            })
        launch{
            viewModel.getUserStateFlow()
                .collect{
                    when{
                        it.isFailure->{
                            Log.d("STATEFLOW", "onCreate: ${it.exceptionOrNull()}")
                        }
                        it.isSuccess->{
                            Log.d("STATEFLOW", "onCreate: ${it.getOrNull()}")
                        }
                    }
                }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}