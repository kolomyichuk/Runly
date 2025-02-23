package kolomyichuk.runly.ui.screens

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.maps.android.compose.GoogleMap
import kolomyichuk.runly.ui.components.ButtonStart
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
                    text = "0:00:00",
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

        if (!isRunning && !isPause){
            ButtonStart(
                onClick = {
                    isRunning = true
                    isPause = false
                },
                modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
                text = "Start",
                roundDp = 8.dp
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
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
                                isPause = false
                            },
                            imageVector = Icons.Filled.Stop,
                            iconColor = MaterialTheme.colorScheme.onSurface,
                            elevation = 10.dp,
                            iconSize = 28.dp,
                            buttonSize = 40.dp,
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            contentDescription = "Stop"
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isRunning) {
                        Button(
                            onClick = {
                                isRunning = false
                                isPause = true
                            },
                            modifier = Modifier.width(130.dp).height(40.dp)
                        ) {
                            Text(text = "Pause")
                        }
                    } else if(isPause) {
                        Button(
                            onClick = {
                                isRunning = true
                                isPause = false
                            },
                            modifier = Modifier.width(130.dp).height(40.dp)
                        ) {
                            Text(text = "Resume")
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircleIconButton(
                        onClick = {

                        },
                        imageVector = Icons.Outlined.LocationOn,
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        elevation = 10.dp,
                        iconSize = 28.dp,
                        buttonSize = 40.dp,
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        contentDescription = "Map view"
                    )
                }
            }
        }
    }
}
