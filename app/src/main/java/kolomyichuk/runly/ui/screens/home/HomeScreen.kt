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
    homeViewModel: HomeViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBarApp(
            title = stringResource(R.string.home)
        )
        HomeScreenContent(
            homeViewModel = homeViewModel
        )
    }

}

// TODO Can be private
@Composable
fun HomeScreenContent(
    homeViewModel: HomeViewModel
) {
    // TODO I guess the initial values is already an empty list as define in ViewModel
    // TODO There is not need to pass initialValue
    val runs = homeViewModel.runs.collectAsStateWithLifecycle(initialValue = emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // TODO Let's pass 'key' to improve the performance
            items(runs.value) { run ->
                RunCard(run)
            }
        }
    }
}


