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
package com.example.androiddevchallenge.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.TimerViewModel

@Composable
fun TimerScreen(
    state: State,
    viewModel: TimerViewModel,
    onStartTimer: () -> Unit = {},
    onStopTimer: () -> Unit = {},
    timerOneSecond: Int,
    timerMinute: Int,
    timerTwoSecond: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (state) {
            is State.Countdown -> {
                val ratio = state.seconds / state.total.toFloat()
                val alpha by animateFloatAsState(targetValue = ratio)
                Box(contentAlignment = Alignment.Center) {
                    ProgressCircle(
                        modifier = Modifier
                            .size(280.dp)
                            .alpha(1 - alpha),
                        color = Color.Blue,
                        strokeWidth = 32.dp,
                        progress = state.seconds / state.total.toFloat(),
                    )
                    Image(
                        painter = painterResource(id = R.drawable.dial_running),
                        contentDescription = "Dial"
                    )
                    Text(
                        text = "$timerMinute:$timerOneSecond$timerTwoSecond",
                        fontSize = 22.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayButton(
                        modifier = Modifier.size(45.dp),
                        onClick = { },
                        enabled = true,
                        icon = Icons.Filled.Replay,
                        iconSize = 30.dp
                    )
                    PlayButton(
                        modifier = Modifier.padding(horizontal = 45.dp).size(60.dp),
                        onClick = onStopTimer,
                        enabled = true,
                        icon = Icons.Filled.Stop,
                        iconSize = 30.dp
                    )
                    PlayButton(
                        modifier = Modifier.size(45.dp),
                        onClick = { },
                        enabled = true,
                        icon = Icons.Filled.NotificationsNone,
                        iconSize = 30.dp
                    )
                }
            }
            is State.Stopped -> {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.dial_stopped),
                        contentDescription = "Dial"
                    )
                    Text(
                        text = "$timerMinute:$timerOneSecond$timerTwoSecond",
                        fontSize = 22.sp,
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        var minute by remember { mutableStateOf(0) }
                        var secondOne by remember { mutableStateOf(0) }
                        var secondTwo by remember { mutableStateOf(0) }

                        val minuteAlpha by animateFloatAsState(
                            if (minute == minute) 1f else 0.4f,
                            finishedListener = {
                                minute = timerMinute
                            }
                        )
                        val oneSecondAlpha by animateFloatAsState(
                            if (secondOne == timerOneSecond) 1f else 0.4f,
                            finishedListener = {
                                secondOne = timerOneSecond
                            }
                        )

                        val twoSecondAlpha by animateFloatAsState(
                            if (secondTwo == timerTwoSecond) 1f else 0.4f,
                            finishedListener = {
                                secondTwo = timerTwoSecond
                            }
                        )

                        Column {
                            IconButton(
                                onClick = { viewModel.minuteUpPress() },
                                enabled = true,
                                modifier = Modifier.offset(x = (-8).dp, y = 0.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "add a minute"
                                )
                            }

                            Text(
                                text = "$timerMinute",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .animateContentSize()
                                    .alpha(minuteAlpha),
                                style = MaterialTheme.typography.h5
                            )
                            IconButton(
                                onClick = { viewModel.minuteDownPress() },
                                enabled = true,
                                modifier = Modifier.offset(x = (-8).dp, y = 0.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "subtract a minute"
                                )
                            }
                        }

                        Column {
                            Text(
                                text = ":",
                                modifier = Modifier.padding(top = 45.dp),
                                style = MaterialTheme.typography.h5
                            )
                        }

                        Column {
                            IconButton(
                                onClick = { viewModel.secondOneUpPress() },
                                enabled = true,
                                modifier = Modifier.offset(x = (-8).dp, y = 0.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "add 10 seconds"
                                )
                            }
                            Text(
                                text = "$timerOneSecond",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .animateContentSize()
                                    .alpha(oneSecondAlpha),
                                style = MaterialTheme.typography.h5
                            )
                            IconButton(
                                onClick = { viewModel.secondOneDownPress() },
                                enabled = true,
                                modifier = Modifier.offset(x = (-8).dp, y = 0.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "subtract 10 seconds"
                                )
                            }
                        }

                        Column {
                            IconButton(
                                onClick = { viewModel.secondTwoUpPress() },
                                enabled = true,
                                modifier = Modifier.offset(x = (-8).dp, y = 0.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = "add one second"
                                )
                            }
                            Text(
                                text = "$timerTwoSecond",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp)
                                    .animateContentSize()
                                    .alpha(twoSecondAlpha),
                                style = MaterialTheme.typography.h5
                            )
                            IconButton(
                                onClick = { viewModel.secondTwoDownPress() },
                                enabled = true,
                                modifier = Modifier.offset(x = (-8).dp, y = 0.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = "subtract 1 second"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayButton(
                        modifier = Modifier.size(45.dp),
                        onClick = { },
                        enabled = true,
                        icon = Icons.Filled.Replay,
                        iconSize = 30.dp
                    )
                    PlayButton(
                        modifier = Modifier.padding(horizontal = 45.dp).size(60.dp),
                        onClick = onStartTimer,
                        enabled = true,
                        icon = Icons.Filled.PlayArrow,
                        iconSize = 30.dp
                    )
                    PlayButton(
                        modifier = Modifier.size(45.dp),
                        onClick = { },
                        enabled = true,
                        icon = Icons.Filled.NotificationsNone,
                        iconSize = 30.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    icon: ImageVector,
    iconSize: Dp
) {
    Button(
        onClick = { onClick() },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        elevation = ButtonDefaults.elevation(defaultElevation = 8.dp),
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = icon,
            contentDescription = null
        )
    }
}

sealed class State(val total: Int) {
    class Stopped(seconds: Int = 0) : State(seconds)

//    class Paused(seconds: Int = 0) : State(seconds)

    class Countdown(total: Int, val seconds: Int) : State(total)
}

@Composable
fun ProgressCircle(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
    }
    Canvas(
        modifier
            .progressSemantics(progress)
            .size(40.dp)
            .focusable()
    ) {
        // Start at 12 O'clock
        val startAngle = 270f
        val sweep = (1 - progress) * 360f
        drawCircularIndicator(startAngle, sweep, color, stroke)
    }
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}
