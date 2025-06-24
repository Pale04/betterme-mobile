package com.betterdevs.betterme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.betterdevs.betterme.ui.navigation.AppNavigation
import com.betterdevs.betterme.ui.navigation.Destination
import com.betterdevs.betterme.ui.navigation.Destinations
import com.betterdevs.betterme.ui.theme.BetterMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BetterMeTheme (darkTheme = false) {
                AppScaffold()
            }
        }
    }
}

@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf<Destination>(Destinations.Login) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            currentScreen = if (route?.contains(Destinations.Statistics.path) == true) {
                Destinations.Statistics
            } else {
                Destinations.getAllDestinations().find { it.path == route } ?: Destinations.Login
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar()
        },
        bottomBar = {
            if (currentScreen.showBottomBar) {
                AppBottomBar(navController)
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AppTopBar() {
    Box (
        modifier = Modifier.fillMaxWidth()
            .background( Color(0xFFDEF9C4) )
    ) {
        Image(
            painter = painterResource(R.drawable.betterme_logo),
            contentDescription = "BetterMe logo",
            modifier = Modifier
                .height(40.dp)
                .padding(top = 8.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val itemIsSelected: (Destination) -> Boolean = { destination ->
        currentDestination?.hierarchy?.any { it.route == destination.path } == true
    }
    val onItemClick: (Destination) -> Unit = { destination ->
        navController.navigate(destination.path) {
            popUpTo(navController.graph.id) {
                inclusive = true
                saveState = false
            }
            launchSingleTop = true
            restoreState = false
        }
    }

    NavigationBar {
        NavigationBarItem (
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Ver publicaciones"
                )
            },
            label = { Text (text = stringResource(R.string.general_posts_button_text)) },
            selected = itemIsSelected(Destinations.Posts),
            onClick = { onItemClick(Destinations.Posts) }
        )
        NavigationBarItem (
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Ver seguimiento de salud"
                )
            },
            label = { Text (text = stringResource(R.string.general_statistics_button_text)) },
            selected = itemIsSelected(Destinations.StatisticSelection),
            onClick = { onItemClick(Destinations.StatisticSelection) }
        )
        NavigationBarItem (
            icon = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Ver Perfil"
                )
            },
            label = { Text (text = stringResource(R.string.general_profile_button_text)) },
            selected = itemIsSelected(Destinations.Profile),
            onClick = { onItemClick(Destinations.Profile) }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BetterMePreview() {
    BetterMeTheme {
        AppScaffold()
    }
}