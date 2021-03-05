/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.ui.components.State
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _state = MutableLiveData<State>(State.Stopped())
    val state: LiveData<State> = _state
    private var currentTimer: Job? = null

    val query = mutableStateOf("")

    private val _minuteTimer = MutableLiveData(0)
    val minuteTimer: LiveData<Int> = _minuteTimer

    private val _secondOneTimer = MutableLiveData(0)
    val secondOneTimer: LiveData<Int> = _secondOneTimer

    private val _secondTwoTimer = MutableLiveData(0)
    val secondTwoTimer: LiveData<Int> = _secondTwoTimer

    fun minuteUpPress() {
        _minuteTimer.value?.let {
            if (it == 5) {
                _minuteTimer.value = 0
            } else {
                _minuteTimer.value = it + 1
            }
        }
    }

    fun minuteDownPress() {
        _minuteTimer.value?.let {
            if (it == 0) {
                _minuteTimer.value = 5
            } else {
                _minuteTimer.value = it - 1
            }
        }
    }

    fun secondOneUpPress() {
        _secondOneTimer.value?.let {
            if (it == 5) {
                _secondOneTimer.value = 0
            } else {
                _secondOneTimer.value = it + 1
            }
        }
    }

    fun secondOneDownPress() {
        _secondOneTimer.value?.let {
            if (it == 0) {
                _secondOneTimer.value = 5
            } else {
                _secondOneTimer.value = it - 1
            }
        }
    }

    fun secondTwoUpPress() {
        _secondTwoTimer.value?.let {
            if (it == 9) {
                _secondTwoTimer.value = 0
            } else {
                _secondTwoTimer.value = it + 1
            }
        }
    }

    fun secondTwoDownPress() {
        _secondTwoTimer.value?.let {
            if (it == 0) {
                _secondTwoTimer.value = 9
            } else {
                _secondTwoTimer.value = it - 1
            }
        }
    }

    private fun secondDown() {
        if (_secondTwoTimer.value != 0) {
            _secondTwoTimer.value?.let { _secondTwoTimer.value = it - 1 }
            if (
                _secondOneTimer.value == 0 &&
                _secondTwoTimer.value == 0 &&
                _minuteTimer.value == 0
            ) {
                _state.value = State.Stopped(0)
            }
        } else {
            _secondTwoTimer.value?.let { _secondTwoTimer.value = 9 }
            if (_secondOneTimer.value != 0) {
                _secondOneTimer.value?.let { _secondOneTimer.value = it - 1 }
            } else {
                _secondOneTimer.value?.let { _secondOneTimer.value = 5 }
                if (_minuteTimer.value != 0) {
                    _minuteTimer.value?.let { _minuteTimer.value = it - 1 }
                } else {
                    _state.value = State.Stopped(0)
                }
            }
        }
    }

    @InternalCoroutinesApi
    fun startTimer() {
        val currentState = _state.value
        if (currentState !is State.Stopped) {
            return
        }
//        val seconds = currentState.total
        val seconds = (_minuteTimer.value!!.times(60)) + (_secondOneTimer.value?.times(10)!!) + _secondTwoTimer.value!!
        if (seconds <= 0) {
            return
        }
        _state.value = State.Countdown(seconds, seconds)
        this.currentTimer = viewModelScope.launch {
            timer(seconds).collect {
                _state.value = if (it == 0) {
                    State.Stopped(0)
                } else {
                    secondDown()
                    State.Countdown(seconds, it)
                }
            }
        }
    }

//    fun pauseTimer() {
//        _state.value = State.Paused()
//    }

    fun stopTimer() {
        currentTimer?.cancel()
        _state.value = State.Stopped(0)
        _minuteTimer.value?.let { _minuteTimer.value = 0 }
        _secondOneTimer.value?.let { _secondOneTimer.value = 0 }
        _secondTwoTimer.value?.let { _secondTwoTimer.value = 0 }
    }

    private fun timer(seconds: Int): Flow<Int> = flow {
        for (s in (seconds - 1) downTo 0) {
            delay(1000L)
            emit(s)
        }
    }
}
