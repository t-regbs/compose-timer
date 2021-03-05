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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.androiddevchallenge.ui.TimerViewModel
import com.example.androiddevchallenge.ui.components.State
import com.example.androiddevchallenge.ui.components.TimerScreen
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<TimerViewModel>()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val timerMinute: Int by viewModel.minuteTimer.observeAsState(0)
            val timerOneSecond: Int by viewModel.secondOneTimer.observeAsState(0)
            val timerTwoSecond: Int by viewModel.secondTwoTimer.observeAsState(0)

            MyTheme {
//                MyApp()
                val state by viewModel.state.observeAsState(State.Stopped())
                TimerScreen(
                    state = state,
                    viewModel = viewModel,
                    onStartTimer = viewModel::startTimer,
                    onStopTimer = viewModel::stopTimer,
                    timerMinute = timerMinute,
                    timerOneSecond = timerOneSecond,
                    timerTwoSecond = timerTwoSecond
                )
            }
        }
    }
}

@InternalCoroutinesApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
//        TimerScreen()
    }
}

@InternalCoroutinesApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
//        TimerScreen()
    }
}
