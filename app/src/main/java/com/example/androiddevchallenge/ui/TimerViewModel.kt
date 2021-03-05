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

    val isEditTextShowing = mutableStateOf(true)

    /**
     * Update value when EditText content changed
     * @param text new content in EditText
     */
    fun updateValue(text: String) {
        // Just in case the number is too big
        if (text.length > 5) return
        // Remove non-numeric elements
        var value = text.replace("\\D".toRegex(), "")
        // Zero cannot appear in the first place
        if (value.startsWith("0")) value = value.substring(1)
        // Set a default value to prevent NumberFormatException
        if (value.isBlank()) value = "0"
//        totalTime.value = value.toLong()
//        timeLeft.value = value.toLong()

        _state.value = State.Stopped(value.toInt())
    }

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

    fun secondDown() {
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

//    fun pauseTimer() {
//        _countdownState.value = CountdownState.PAUSED
//    }

    @InternalCoroutinesApi
    fun startTimer() {
        val currentState = _state.value
        if (currentState !is State.Stopped) {
            return
        }
        val seconds = currentState.total
        if (seconds <= 0) {
            return
        }
        _state.value = State.Countdown(seconds, seconds)
        this.currentTimer = viewModelScope.launch {
            timer(seconds).collect {
                _state.value = if (it == 0) {
                    State.Stopped(0)
                } else {
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

    fun showEditText(show: Boolean) {
        isEditTextShowing.value = show
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    private fun setQuery(query: String) {
        this.query.value = query
//        savedStateHandle.set(STATE_KEY_QUERY, query)
        updateValue(query)
    }

    private fun timer(seconds: Int): Flow<Int> = flow {
        for (s in (seconds - 1) downTo 0) {
            delay(1000L)
            emit(s)
        }
    }
}
