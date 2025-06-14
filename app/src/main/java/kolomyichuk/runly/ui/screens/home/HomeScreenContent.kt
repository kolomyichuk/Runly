package kolomyichuk.runly.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreenContent(
    homeViewModel: HomeViewModel,
    onRunClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val runs = homeViewModel.runs.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(10.dp)
    ) {
        if (runs.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = runs.value,
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
}