package kolomyichuk.runly.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import kolomyichuk.runly.R
import kolomyichuk.runly.ui.navigation.Screen

private sealed class BottomNavItem(
    val screen: Screen,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(Screen.Home, R.string.home, Icons.Default.Home)
    data object Run :
        BottomNavItem(Screen.Run, R.string.run, Icons.AutoMirrored.Default.DirectionsRun)

    data object Profile : BottomNavItem(Screen.Profile, R.string.profile, Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    isRunActive: Boolean
) {
    val items = listOf(BottomNavItem.Home, BottomNavItem.Run, BottomNavItem.Profile)

    NavigationBar(modifier = Modifier.height(65.dp)) {
        val currentEntry by navController.currentBackStackEntryAsState()
        val currentScreen = currentEntry?.destination

        val transition = rememberInfiniteTransition(label = "BlinkingTransition")
        val alpha by transition.animateFloat(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(durationMillis = 800),
                repeatMode = RepeatMode.Reverse
            ), label = "Blinking Color"
        )

        items.forEach { item ->
            val isRunTab = item.screen == Screen.Run

            val backgroundColor = when {
                isRunTab && isRunActive -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = alpha)
                else -> Color.Transparent
            }

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(item.labelRes)) },
                label = { Text(stringResource(item.labelRes)) },
                selected = currentScreen?.hasRoute(item.screen::class) == true,
                onClick = { navController.navigate(item.screen) },
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
