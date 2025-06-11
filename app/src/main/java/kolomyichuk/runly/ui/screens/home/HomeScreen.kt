package kolomyichuk.runly.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.components.TopBarApp

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onRunClick: (Int) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        homeViewModel.homeEffects.collect { effect ->
            when (effect) {
                is HomeEffect.ShowDeleteSnackBar -> {
                    val result = snackBarHostState.showSnackbar(
                        message = context.getString(R.string.run_deleted),
                        actionLabel = context.getString(R.string.undo),
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        homeViewModel.undoDeleteRun()
                    } else {
                        homeViewModel.confirmDeleteRun()
                    }
                }
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