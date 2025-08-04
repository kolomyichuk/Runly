package kolomyichuk.runly.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kolomyichuk.runly.R

@Composable
fun HomeScreenContent(
    homeViewModel: HomeViewModel,
    onRunClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(10.dp)
    ) {
        when (uiState) {

            is RunUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is RunUiState.Success -> {
                val runs = (uiState as RunUiState.Success).runs

                if (runs.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = runs,
                            key = { run ->
                                run.id
                            }
                        ) { run ->
                            HomeRunItem(
                                run = run,
                                onClick = { onRunClick(run.id) },
                                onDelete = { homeViewModel.requestDeleteRun(run) }
                            )
                        }
                    }
                } else {
                    HomeEmptyList()
                }
            }

            is RunUiState.Error -> {
                val message = when ((uiState as RunUiState.Error).type) {
                    ErrorType.NETWORK -> stringResource(R.string.error_network)
                    ErrorType.UNAUTHORIZED -> stringResource(R.string.error_unauthorized)
                    ErrorType.UNKNOWN -> stringResource(R.string.something_went_wrong)
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = message)
                }
            }
        }

    }
}

