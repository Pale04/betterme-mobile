package com.betterdevs.betterme.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.betterdevs.betterme.domain_model.StatisticCategory
import com.betterdevs.betterme.ui.create_account.CreateAccountScreen
import com.betterdevs.betterme.ui.login.LoginScreen
import com.betterdevs.betterme.ui.statistics.StatisticSelectionScreen
import com.betterdevs.betterme.ui.statistics.StatisticsScreen
import com.betterdevs.betterme.ui.posts.PostsScreen
import com.betterdevs.betterme.ui.posts.post_creation.PostCreationScreen
import com.betterdevs.betterme.ui.profile.ProfileScreen
import com.betterdevs.betterme.ui.profile.edit_profile.EditProfileScreen
import com.betterdevs.betterme.ui.profile.edit_email.EditEmailScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = Destinations.Login.path) {
        composable(Destinations.Login.path) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Destinations.StatisticSelection.path) {
                        popUpTo(Destinations.Login.path) { inclusive = true }
                    }
                },
                onCreateAccountClick = {
                    navController.navigate(Destinations.CreateAccount.path) {
                        popUpTo(Destinations.Login.path) { inclusive = true }
                    }
                },
                modifier = modifier
            )
        }
        composable(Destinations.CreateAccount.path) {
            CreateAccountScreen(
                onIHaveAnAccountClick = {
                    navController.navigate(Destinations.Login.path) {
                        popUpTo(Destinations.CreateAccount.path) { inclusive = true }
                    }
                },
                modifier = modifier
            )
        }
        composable(Destinations.Posts.path) {
            PostsScreen(
                onCreatePostClick = {
                    navController.navigate(Destinations.PostCreation.path)
                },
                modifier = modifier
            )
        }
        composable(Destinations.PostCreation.path) {
            PostCreationScreen(
                modifier = modifier
            )
        }
        composable(Destinations.StatisticSelection.path) {
            StatisticSelectionScreen(
                onCategoryClick = { category ->
                    navController.navigate("${Destinations.Statistics.path}/${category.name}")
                },
                modifier = modifier
            )
        }
        composable(
            route = Destinations.Statistics.routeWithArgs,
            arguments = listOf(
                navArgument(Destinations.Statistics.ARG_CATEGORY) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val categoryString = backStackEntry.arguments?.getString(Destinations.Statistics.ARG_CATEGORY)
            val category = StatisticCategory.valueOf(categoryString ?: "")
            StatisticsScreen(category, modifier)
        }
        composable(Destinations.Profile.path) {
            ProfileScreen(
                onEditProfileClick = {
                    navController.navigate(Destinations.EditProfile.path)
                },
                modifier = modifier
            )
        }
        composable(Destinations.EditProfile.path) {
            EditProfileScreen(
                onChangeEmailClick = {
                    navController.navigate(Destinations.EditEmail.path)
                },
                modifier = modifier
            )
        }
        composable(Destinations.EditEmail.path) {
            EditEmailScreen(
                modifier = modifier
            )
        }
    }
}