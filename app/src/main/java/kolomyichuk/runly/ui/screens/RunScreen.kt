package kolomyichuk.runly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.maps.android.compose.GoogleMap
import kolomyichuk.runly.ui.components.CircleIconButton

@Composable
fun RunScreen() {
    var isRunning by rememberSaveable {
        mutableStateOf(false)
    }
    var isPause by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "0 km",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "Distance",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                )
            }


            Column(
                modifier = Modifier.padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "--:-- /km",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Current Pace",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp
                )
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "0:00:05",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Time",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (isRunning || isPause) {
                    CircleIconButton(
                        onClick = {
                            isRunning = false
                        },
                        imageVector = Icons.Filled.Stop,
                        iconColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = 10.dp,
                        iconSize = 28.dp,
                        buttonSize = 40.dp,
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        contentDescription = "Stop"
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (isRunning) {
                    Button(
                        onClick = {
                            isRunning = false
                            isPause = true
                        }
                    ) {
                        Text(text = "Pause")
                    }
                } else if (isPause) {
                    Button(
                        onClick = {
                            isRunning = true
                            isPause = false
                        }
                    ) {
                        Text(text = "Resume")
                    }
                } else {
                    Button(
                        onClick = {
                            isRunning = true
                            isPause = false
                        }
                    ) {
                        Text(text = "Start")
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (isRunning){
                    CircleIconButton(
                        onClick = {

                        },
                        imageVector = Icons.Outlined.LocationOn,
                        iconColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = 0.dp,
                        iconSize = 28.dp,
                        buttonSize = 40.dp,
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        contentDescription = "Map view"
                    )
                }
            }
        }
    }
}