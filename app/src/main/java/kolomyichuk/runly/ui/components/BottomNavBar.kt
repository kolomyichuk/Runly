package kolomyichuk.runly.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kolomyichuk.runly.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    data object Home : BottomNavItem("home", R.string.home, Icons.Default.Home)
    data object Run : BottomNavItem("run", R.string.run, Icons.AutoMirrored.Default.DirectionsRun)
    data object Profile : BottomNavItem("profile", R.string.profile, Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    isRunActive: Boolean
) {
    val items = listOf(BottomNavItem.Home, BottomNavItem.Run, BottomNavItem.Profile)

    NavigationBar(modifier = Modifier.height(65.dp)) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        items.forEach { item ->
            val isRunTab = item.route == BottomNavItem.Run.route

            val backgroundColor = when {
                isRunTab && isRunActive -> MaterialTheme.colorScheme.secondaryContainer
                else -> Color.Transparent
            }

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(item.labelRes)) },
                label = { Text(stringResource(item.labelRes)) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = Color.Transparent,
                ),
                modifier = Modifier.background(backgroundColor)
            )
        }
    }
}
