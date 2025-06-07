package kolomyichuk.runly.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        homeViewModel.snackBarMessages.collect { snackBarData ->
            val result = snackBarHostState.showSnackbar(
                message = snackBarData.message,
                actionLabel = snackBarData.actionLabel,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                homeViewModel.undoDelete()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopBarApp(
                title = stringResource(R.string.home)
            )
        }
    ) { innerPadding ->
        HomeScreenContent(
            homeViewModel = homeViewModel,
            onRunClick = onRunClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
private fun HomeScreenContent(
    homeViewModel: HomeViewModel,
    onRunClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val runs = homeViewModel.runs.collectAsStateWithLifecycle()

    val message = stringResource(R.string.run_deleted)
    val actionLabel = stringResource(R.string.undo)

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
                        onDelete = { homeViewModel.deleteRun(it, message, actionLabel) },
                    )
                }
            }
        } else {
            HomeEmptyList()
        }
    }
}


