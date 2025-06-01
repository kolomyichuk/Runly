package kolomyichuk.runly.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onRunClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.home)
        )
        HomeScreenContent(
            homeViewModel = homeViewModel,
            onRunClick = onRunClick
        )
    }
}

@Composable
private fun HomeScreenContent(
    homeViewModel: HomeViewModel,
    onRunClick: (Int) -> Unit
) {
    val runs = homeViewModel.runs.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                        onClick = { onRunClick(run.id) }
                    )
                }
            }
        } else {
            HomeEmptyList()
        }
    }
}


