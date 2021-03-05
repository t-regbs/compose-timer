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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.TimerViewModel
import com.example.androiddevchallenge.ui.components.State
import com.example.androiddevchallenge.ui.components.Timer
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<TimerViewModel>()
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val query = viewModel.query.value
            val showEdt = viewModel.isEditTextShowing.value

            val timerMinute: Int by viewModel.minuteTimer.observeAsState(0)
            val timerOneSecond: Int by viewModel.secondOneTimer.observeAsState(0)
            val timerTwoSecond: Int by viewModel.secondTwoTimer.observeAsState(0)

            MyTheme {
//                MyApp()
                val state by viewModel.state.observeAsState(State.Stopped())
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                }
                Timer(
                    query = query,
                    onQueryChanged = viewModel::onQueryChanged,
                    showEdt = showEdt,
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

@Composable
fun TimerEditText(
    show: Boolean,
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .size(300.dp, 120.dp),
        contentAlignment = Alignment.Center
    ) {
        if (show) {
            TextField(
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp, 60.dp),
                value = query,
                onValueChange = { onQueryChanged(it) },
                label = { Text("Countdown Seconds") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}
